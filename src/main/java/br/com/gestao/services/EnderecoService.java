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
import br.com.gestao.entities.Endereco;
import br.com.gestao.exceptions.NotFoundException;
import br.com.gestao.repositories.EnderecoRepository;

@Service
public class EnderecoService {

	private final EnderecoRepository enderecoRepository;
	private final ModelMapper modelMapper;

	public EnderecoService(EnderecoRepository enderecoRepository, ModelMapper modelMapper) {
		this.enderecoRepository = enderecoRepository;
		this.modelMapper = modelMapper;
	}

	// LISTAR POR ID
	public EnderecoDTO findById(Long id) {
		Endereco endereco = enderecoRepository.findById(id).orElseThrow(() -> new NotFoundException("Cadastro ID: " + id + " Não encontrado!!"));
		return this.modelMapper.map(endereco, EnderecoDTO.class);
	}

	// LISTAR TODOS
	public List<EnderecoDTO> findAll() {
		return enderecoRepository.findAll().stream().map(valorCC -> modelMapper.map(valorCC, EnderecoDTO.class)).collect(Collectors.toList());
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
}
