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

	// LISTAR POR ID
	public UsuarioDTO findById(Long id) {
		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Cadastro ID: " + id + " Não encontrado!!"));
		return this.modelMapper.map(usuario, UsuarioDTO.class);
	}

	// LISTAR TODOS
	public List<UsuarioDTO> findAll() {
		return usuarioRepository.findAll().stream().map(valorCC -> modelMapper.map(valorCC, UsuarioDTO.class)).collect(Collectors.toList());
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

	// SALVAR (INSERIR/ALTERAR)
	public ResponseEntity<UsuarioDTO> salvar(final UsuarioDTO usuarioDTO) {
		Usuario itemSalvar = this.modelMapper.map(usuarioDTO, Usuario.class);
		List<Endereco> enderecos = itemSalvar.getEnderecos();
		
		//salva os enderecos
		if (enderecos != null) {
			enderecos.forEach(c -> c = this.enderecoRepository.save(c));
			itemSalvar.setEnderecos(enderecos);
		}
		//salva o usuario:
		itemSalvar = usuarioRepository.save(itemSalvar);
		
		//atribui o usuario com id criado aos mesmos endereços
		for (Endereco endereco : enderecos) {
			endereco.setUsuario(itemSalvar);
        }
		
		// faz update dos endereços
		if (enderecos != null) {
			enderecos.forEach(c -> c = this.enderecoRepository.save(c));
			itemSalvar.setEnderecos(enderecos);
		}
		return new ResponseEntity<>(this.modelMapper.map(itemSalvar, UsuarioDTO.class), HttpStatus.OK);
	}

	// DELETAR
	public ResponseEntity<Boolean> delete(Long id) {
		if (usuarioRepository.existsById(id)) {
			usuarioRepository.deleteById(id);
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		return new ResponseEntity<>(false, HttpStatus.OK);
	}
}
