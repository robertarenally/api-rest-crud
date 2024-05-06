package br.com.gestao.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gestao.commons.ResponseWrapper;
import br.com.gestao.dto.EnderecoDTO;
import br.com.gestao.dto.UsuarioDTO;
import br.com.gestao.entities.Endereco;
import br.com.gestao.entities.Usuario;
import br.com.gestao.exceptions.NotFoundException;
import br.com.gestao.repositories.EnderecoRepository;
import br.com.gestao.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final EnderecoRepository enderecoRepository;
	private final ModelMapper modelMapper;

	public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper, EnderecoRepository enderecoRepository) {
		this.usuarioRepository = usuarioRepository;
		this.enderecoRepository = enderecoRepository;
		this.modelMapper = modelMapper;
	}

	// LISTAR USUARIO POR ID
	public ResponseEntity<ResponseWrapper<UsuarioDTO>> findById(Long id) {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		if(usuario != null)
		{
			return new ResponseEntity<>(new ResponseWrapper<>(this.modelMapper.map(usuario, UsuarioDTO.class), null), HttpStatus.OK);
		}else {
			ResponseWrapper<UsuarioDTO> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Usuário não encontrado com o ID: " + id);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
		}
	}

	// LISTAR TODOS
	public ResponseEntity<ResponseWrapper<List<UsuarioDTO>>> findAll() {
		List<UsuarioDTO> usuarios = usuarioRepository.findAll().stream().map(valorCC -> modelMapper.map(valorCC, UsuarioDTO.class)).collect(Collectors.toList());
		if(usuarios != null)
		{
			return new ResponseEntity<>(new ResponseWrapper<>(usuarios, null), HttpStatus.OK);
		}else {
			ResponseWrapper<List<UsuarioDTO>> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Base de dados não tem nenhum usuário cadastrado");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
		}
	}
	
	// LISTAR TODOS OS ENDEREÇOS DE UM USUARIO
	public ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> findEnderecoByIdUsuario(Long id,Integer pagina, Integer quantidade) {
		Sort sort = Sort.by("id").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);
		
		Page<Endereco> page = this.enderecoRepository.findByUsuarioId(id, pageRequest);

		if (page != null && !page.isEmpty()) {
			Page<EnderecoDTO> dtoPage = page.map(item -> this.modelMapper.map(item, EnderecoDTO.class));
			return new ResponseEntity<>(new ResponseWrapper<>(dtoPage, null), HttpStatus.OK);
		}else {
			ResponseWrapper<Page<EnderecoDTO>> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Não foram encontrados endereços para esse usuário");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
		}
	}
	
	// LISTAR TODOS OS USUARIO QUE TEM UM NOME PARECIDO COM O VALOR DO NOME CONSULTADO
	public ResponseEntity<ResponseWrapper<Page<UsuarioDTO>>> findByNomeLike(Integer pagina, Integer quantidade, String nome) {
		Sort sort = Sort.by("nome").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);

		Page<Usuario> page = this.usuarioRepository.findByNomeLike("%" + nome + "%", pageRequest);

		if (page != null && !page.isEmpty()) {
			Page<UsuarioDTO> dtoPage = page.map(item -> this.modelMapper.map(item, UsuarioDTO.class));
			return new ResponseEntity<>(new ResponseWrapper<>(dtoPage, null), HttpStatus.OK);
		}else {
			ResponseWrapper<Page<UsuarioDTO>> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Não foram encontrados usuários com esse nome");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
		}
	}

	// LISTAR TODOS E MOSTRAR O RESULTADO UTILIZANDO PAGINAÇÃO
	public ResponseEntity<ResponseWrapper<Page<UsuarioDTO>>> findAll(Integer pagina, Integer quantidade) {
		Sort sort = Sort.by("id").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);
		
		Page<Usuario> page = this.usuarioRepository.listAllByPages(pageRequest);

		if (page != null && !page.isEmpty()) {
			Page<UsuarioDTO> dtoPage = page.map(item -> this.modelMapper.map(item, UsuarioDTO.class));
			return new ResponseEntity<>(new ResponseWrapper<>(dtoPage, null), HttpStatus.OK);
		}else {
			ResponseWrapper<Page<UsuarioDTO>> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Não foram encontrados usuários na base de dados");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
		}
	}

	// SALVAR (INSERIR/ALTERAR) O CADASTRO DO USUARIO, INCLUINDO O ENDERECO
	@Transactional
	public ResponseEntity<ResponseWrapper<UsuarioDTO>> salvarUsuario(final UsuarioDTO usuarioDTO) {
		Usuario itemSalvar = this.modelMapper.map(usuarioDTO, Usuario.class);
		List<Endereco> enderecos = itemSalvar.getEnderecos();
		
		//salva o usuario:
		itemSalvar = usuarioRepository.save(itemSalvar);
		
		//atribui o usuario com id criado aos mesmos endereços
		for (Endereco endereco : enderecos) {
			endereco.setUsuario(itemSalvar);
        }
		
		//salva os enderecos
		if (enderecos != null) {
			enderecos.forEach(c -> c = this.enderecoRepository.save(c));
			itemSalvar.setEnderecos(enderecos);
		}
		
		if(itemSalvar != null && itemSalvar.getId() != null)
		{
			return new ResponseEntity<>(new ResponseWrapper<>(this.modelMapper.map(itemSalvar, UsuarioDTO.class), null), HttpStatus.CREATED);
		}else {
			ResponseWrapper<UsuarioDTO> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Erro ao cadastrar o usuário na base de dados");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
		}
	}
	
	// SALVAR (INSERIR/ALTERAR) O CADASTRO DE UM ENDERECO PARA UM USUARIO JA CADASTRADO
	@Transactional
	public ResponseEntity<ResponseWrapper<EnderecoDTO>> salvarEndereco(final EnderecoDTO enderecoDTO, final Long idUsuario) {
		Endereco itemSalvar = this.modelMapper.map(enderecoDTO, Endereco.class);
		
		// encontra o usuario:
		Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
		
		if(usuario == null)
		{
			ResponseWrapper<EnderecoDTO> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Usuário com id <" + idUsuario +"> não encontrado na base de dados");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
		}

		if(itemSalvar.getPrincipal() != null && Boolean.TRUE.equals(itemSalvar.getPrincipal()))
		{
			// Lista todos os enderecos de um usuario;
			List<Endereco> enderecos = this.enderecoRepository.findByUsuarioId(idUsuario);
			// Mapeie cada endereço para o usuário específico e sete todos os endereços como não principais
			List<Endereco> enderecosMapeados = enderecos.stream().map(endereco -> {
				endereco.setUsuario(usuario);
				endereco.setPrincipal(Boolean.FALSE);
				return endereco;
			}).collect(Collectors.toList());
			
			// salva os enderecos
			if (enderecosMapeados != null) {
				enderecosMapeados.forEach(c -> c = this.enderecoRepository.save(c));
			}
		}	
		//atribui o usuário ao endereço para manter o relacionamento
		itemSalvar.setUsuario(usuario);
		// salva o endereco:
		itemSalvar = enderecoRepository.save(itemSalvar);
		
		if(itemSalvar != null && itemSalvar.getId() != null)
		{
			return new ResponseEntity<>(new ResponseWrapper<>(this.modelMapper.map(itemSalvar, EnderecoDTO.class), null), HttpStatus.CREATED);
		}else {
			ResponseWrapper<EnderecoDTO> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Erro ao cadastrar o endereço para o usuário com id <" + idUsuario +"> na base de dados");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
		}
	}

	// DELETAR USUARIO
	public ResponseEntity<ResponseWrapper<String>> deleteUsuario(Long id) {
		if (usuarioRepository.existsById(id)) {
			usuarioRepository.deleteById(id);
			return new ResponseEntity<>(new ResponseWrapper<>("Sucesso!!", null), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(new ResponseWrapper<>("usuario com id <"+id+"> não encontrado !!!", null), HttpStatus.NOT_FOUND);
		}
	}
	
	// DELETAR ENDERECO DE UM USUARIO
	public ResponseEntity<ResponseWrapper<String>> deleteEndereco(Long idUsuario, Long idEndereco) {
		if (usuarioRepository.existsById(idUsuario)) {
			if(enderecoRepository.existsById(idEndereco))
			{
				enderecoRepository.deleteById(idEndereco);
				return new ResponseEntity<>(new ResponseWrapper<>("Sucesso!!", null), HttpStatus.OK);
			}else {
				return new ResponseEntity<>(new ResponseWrapper<>("endereço com id <"+idEndereco+"> não encontrado !!!", null), HttpStatus.NOT_FOUND);
			}			
		}else {
			return new ResponseEntity<>(new ResponseWrapper<>("usuario com id <"+idUsuario+"> não encontrado !!!", null), HttpStatus.NOT_FOUND);
		}
	}
}
