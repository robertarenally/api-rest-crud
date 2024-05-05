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

import br.com.gestao.dto.EnderecoDTO;
import br.com.gestao.dto.UsuarioDTO;
import br.com.gestao.entities.Endereco;
import br.com.gestao.entities.Usuario;
import br.com.gestao.exceptions.NotFoundException;
import br.com.gestao.exceptions.OtherErrorException;
import br.com.gestao.repositories.EnderecoRepository;
import br.com.gestao.repositories.UsuarioRepository;

@Service
public class EnderecoService {

	private final UsuarioRepository usuarioRepository;
	private final EnderecoRepository enderecoRepository;
	private final ModelMapper modelMapper;

	public EnderecoService(UsuarioRepository usuarioRepository, EnderecoRepository enderecoRepository,
			ModelMapper modelMapper) {
		this.usuarioRepository = usuarioRepository;
		this.enderecoRepository = enderecoRepository;
		this.modelMapper = modelMapper;
	}

	// LISTAR POR ID
	public EnderecoDTO findById(Long id) {
		Endereco endereco = enderecoRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Cadastro ID: " + id + " Não encontrado!!"));
		return this.modelMapper.map(endereco, EnderecoDTO.class);
	}

	// LISTAR TODOS
	public List<EnderecoDTO> findAll() {
		return enderecoRepository.findAll().stream().map(valorCC -> modelMapper.map(valorCC, EnderecoDTO.class))
				.collect(Collectors.toList());
	}

	// LISTAR TODOS OS ENDEREÇOS POR CEP
	public ResponseEntity<Page<EnderecoDTO>> findByCep(Integer pagina, Integer quantidade, String cep) {
		Sort sort = Sort.by("cep").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);

		Page<Endereco> page = this.enderecoRepository.findByCep(cep, pageRequest);

		Page<EnderecoDTO> dtoPage = null;
		if (page != null) {
			dtoPage = page.map(item -> this.modelMapper.map(item, EnderecoDTO.class));
		}
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}

	// LISTAR TODOS OS ENDEREÇOS POR CIDADE
	public ResponseEntity<Page<EnderecoDTO>> findByCidade(Integer pagina, Integer quantidade, String cidade) {
		Sort sort = Sort.by("cep").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);

		Page<Endereco> page = this.enderecoRepository.findByCidade(cidade, pageRequest);

		Page<EnderecoDTO> dtoPage = null;
		if (page != null) {
			dtoPage = page.map(item -> this.modelMapper.map(item, EnderecoDTO.class));
		}
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}

	// LISTAR TODOS OS ENDEREÇOS POR ESTADO
	public ResponseEntity<Page<EnderecoDTO>> findByEstado(Integer pagina, Integer quantidade, String estado) {
		Sort sort = Sort.by("cep").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);

		Page<Endereco> page = this.enderecoRepository.findByEstado(estado, pageRequest);

		Page<EnderecoDTO> dtoPage = null;
		if (page != null) {
			dtoPage = page.map(item -> this.modelMapper.map(item, EnderecoDTO.class));
		}
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}

	// LISTAR TODOS E MOSTRAR O RESULTADO UTILIZANDO PAGINAÇÃO
	public ResponseEntity<Page<EnderecoDTO>> findAll(Integer pagina, Integer quantidade) {
		Sort sort = Sort.by("id").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);

		Page<Endereco> page = this.enderecoRepository.listAllByPages(pageRequest);

		Page<EnderecoDTO> dtoPage = null;
		if (page != null) {
			dtoPage = page.map(item -> this.modelMapper.map(item, EnderecoDTO.class));
		}
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}

	// SALVAR (INSERIR/ALTERAR) APENAS O ENDEREÇO DE VINCULADO A UM USUÁRIO
	public ResponseEntity<EnderecoDTO> salvar(final EnderecoDTO enderecoDTO) {
		Endereco itemSalvar = this.modelMapper.map(enderecoDTO, Endereco.class);
		Usuario usuario1 = itemSalvar.getUsuario();

		if (usuario1 != null && usuario1.getId() != null) {
			Usuario usuario2 = this.usuarioRepository.findById(usuario1.getId())
					.orElseThrow(() -> new NotFoundException("Cadastro ID: " + usuario1.getId() + " Não encontrado!!"));
			itemSalvar.setUsuario(usuario2);
		} else {
			throw new OtherErrorException("Não foi possível encontrar o usuário vinculado a esse endereço");
		}

		itemSalvar = enderecoRepository.save(itemSalvar);
		return new ResponseEntity<>(this.modelMapper.map(itemSalvar, EnderecoDTO.class), HttpStatus.OK);
	}

	// DELETAR
	public ResponseEntity<Boolean> delete(Long id) {
		if (enderecoRepository.existsById(id)) {
			enderecoRepository.deleteById(id);
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		return new ResponseEntity<>(false, HttpStatus.OK);
	}
}
