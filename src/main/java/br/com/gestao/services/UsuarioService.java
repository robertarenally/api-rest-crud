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

import br.com.gestao.dto.EnderecoDTO;
import br.com.gestao.dto.UsuarioDTO;
import br.com.gestao.entities.Endereco;
import br.com.gestao.entities.Usuario;
import br.com.gestao.exceptions.NotFoundException;
import br.com.gestao.exceptions.OtherErrorException;
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

	// LISTAR POR ID
	public UsuarioDTO findById(Long id) {
		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Cadastro ID: " + id + " Não encontrado!!"));
		return this.modelMapper.map(usuario, UsuarioDTO.class);
	}

	// LISTAR TODOS
	public List<UsuarioDTO> findAll() {
		return usuarioRepository.findAll().stream().map(valorCC -> modelMapper.map(valorCC, UsuarioDTO.class)).collect(Collectors.toList());
	}
	
	// LISTAR TODOS OS ENDEREÇOS DE UM USUARIO
	public ResponseEntity<Page<EnderecoDTO>> findEnderecoByIdUsuario(Long id,Integer pagina, Integer quantidade) {
		Sort sort = Sort.by("id").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);
		
		Page<Endereco> page = this.enderecoRepository.findByUsuarioId(id, pageRequest);
		
		Page<EnderecoDTO> dtoPage = null;
		if (page != null) {
			dtoPage = page.map(item -> this.modelMapper.map(item, EnderecoDTO.class));
		}
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}
	
	// LISTAR TODOS OS USUARIO QUE TEM UM NOME PARECIDO COM O VALOR DO NOME CONSULTADO
	public ResponseEntity<Page<UsuarioDTO>> findByNomeLike(Integer pagina, Integer quantidade, String nome) {
		Sort sort = Sort.by("nome").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);

		Page<Usuario> page = this.usuarioRepository.findByNomeLike("%" + nome + "%", pageRequest);

		Page<UsuarioDTO> dtoPage = null;
		if (page != null) {
			dtoPage = page.map(item -> this.modelMapper.map(item, UsuarioDTO.class));
		}
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}

	// LISTAR TODOS E MOSTRAR O RESULTADO UTILIZANDO PAGINAÇÃO
	public ResponseEntity<Page<UsuarioDTO>> findAll(Integer pagina, Integer quantidade) {
		Sort sort = Sort.by("id").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);
		
		Page<Usuario> page = this.usuarioRepository.listAllByPages(pageRequest);
		
		Page<UsuarioDTO> dtoPage = null;
		if (page != null) {
			dtoPage = page.map(item -> this.modelMapper.map(item, UsuarioDTO.class));
		}
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}

	// SALVAR (INSERIR/ALTERAR) O CADASTRO DO USUARIO, INCLUINDO O ENDERECO
	@Transactional
	public ResponseEntity<UsuarioDTO> salvarUsuario(final UsuarioDTO usuarioDTO) {
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
		return new ResponseEntity<>(this.modelMapper.map(itemSalvar, UsuarioDTO.class), HttpStatus.OK);
	}
	
	// SALVAR (INSERIR/ALTERAR) O CADASTRO DE UM ENDERECO PARA UM USUARIO JA CADASTRADO
	@Transactional
	public ResponseEntity<EnderecoDTO> salvarEndereco(final EnderecoDTO enderecoDTO, final Long idUsuario) {
		Endereco itemSalvar = this.modelMapper.map(enderecoDTO, Endereco.class);
		
		// encontra o usuario:
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new NotFoundException("Cadastro ID: " + idUsuario + " Não encontrado!!"));

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
		// salva o endereco:
		itemSalvar.setUsuario(usuario);
		itemSalvar = enderecoRepository.save(itemSalvar);
		return new ResponseEntity<>(this.modelMapper.map(itemSalvar, EnderecoDTO.class), HttpStatus.OK);
	}

	// DELETAR USUARIO
	public ResponseEntity<Boolean> delete(Long id) {
		if (usuarioRepository.existsById(id)) {
			usuarioRepository.deleteById(id);
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		return new ResponseEntity<>(false, HttpStatus.OK);
	}
}
