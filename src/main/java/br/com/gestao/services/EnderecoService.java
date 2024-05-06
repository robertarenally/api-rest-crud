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

import br.com.gestao.commons.ResponseWrapper;
import br.com.gestao.dto.EnderecoDTO;
import br.com.gestao.entities.Endereco;
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
	public ResponseEntity<ResponseWrapper<EnderecoDTO>> findById(Long id) {
		Endereco endereco = enderecoRepository.findById(id).orElse(null);
		if (endereco != null) {
			return new ResponseEntity<>(new ResponseWrapper<>(this.modelMapper.map(endereco, EnderecoDTO.class), null),HttpStatus.OK);
		} else {
			ResponseWrapper<EnderecoDTO> responseWrapper = new ResponseWrapper<>();
			responseWrapper.setMessage("Cadastro ID: " + id + " Não encontrado!!");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
		}
	}

	// LISTAR TODOS
	public ResponseEntity<ResponseWrapper<List<EnderecoDTO>>> findAll() {
		List<EnderecoDTO> enderecos = enderecoRepository.findAll().stream().map(valorCC -> modelMapper.map(valorCC, EnderecoDTO.class)).collect(Collectors.toList());
		if(enderecos != null)
		{
			return new ResponseEntity<>(new ResponseWrapper<>(enderecos, null), HttpStatus.OK);
		}else {
			ResponseWrapper<List<EnderecoDTO>> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Base de dados não tem nenhum usuário cadastrado");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
		}
	}

	// LISTAR TODOS OS ENDEREÇOS POR CEP
	public ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> findByCep(Integer pagina, Integer quantidade, String cep) {
		Sort sort = Sort.by("cep").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);

		Page<Endereco> page = this.enderecoRepository.findByCep(cep, pageRequest);

		if (page != null && !page.isEmpty()) {
			Page<EnderecoDTO> dtoPage = page.map(item -> this.modelMapper.map(item, EnderecoDTO.class));
			return new ResponseEntity<>(new ResponseWrapper<>(dtoPage, null), HttpStatus.OK);
		}else {
			ResponseWrapper<Page<EnderecoDTO>> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Não foram encontrados endereços para esse cep");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
		}
	}

	// LISTAR TODOS OS ENDEREÇOS POR CIDADE
	public ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> findByCidade(Integer pagina, Integer quantidade, String cidade) {
		Sort sort = Sort.by("cidade").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);

		Page<Endereco> page = this.enderecoRepository.findByCidade(cidade, pageRequest);

		if (page != null && !page.isEmpty()) {
			Page<EnderecoDTO> dtoPage = page.map(item -> this.modelMapper.map(item, EnderecoDTO.class));
			return new ResponseEntity<>(new ResponseWrapper<>(dtoPage, null), HttpStatus.OK);
		}else {
			ResponseWrapper<Page<EnderecoDTO>> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Não foram encontrados endereços para essa cidade");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
		}
	}

	// LISTAR TODOS OS ENDEREÇOS POR ESTADO
	public ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> findByEstado(Integer pagina, Integer quantidade, String estado) {
		Sort sort = Sort.by("estado").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);

		Page<Endereco> page = this.enderecoRepository.findByEstado(estado, pageRequest);

		if (page != null && !page.isEmpty()) {
			Page<EnderecoDTO> dtoPage = page.map(item -> this.modelMapper.map(item, EnderecoDTO.class));
			return new ResponseEntity<>(new ResponseWrapper<>(dtoPage, null), HttpStatus.OK);
		}else {
			ResponseWrapper<Page<EnderecoDTO>> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Não foram encontrados endereços para esse estado");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
		}
	}

	// LISTAR TODOS E MOSTRAR O RESULTADO UTILIZANDO PAGINAÇÃO
	public ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> findAll(Integer pagina, Integer quantidade) {
		Sort sort = Sort.by("id").ascending();
		PageRequest pageRequest = PageRequest.of(pagina - 1, quantidade, sort);

		Page<Endereco> page = this.enderecoRepository.listAllByPages(pageRequest);

		if (page != null && !page.isEmpty()) {
			Page<EnderecoDTO> dtoPage = page.map(item -> this.modelMapper.map(item, EnderecoDTO.class));
			return new ResponseEntity<>(new ResponseWrapper<>(dtoPage, null), HttpStatus.OK);
		}else {
			ResponseWrapper<Page<EnderecoDTO>> responseWrapper = new ResponseWrapper<>();
	        responseWrapper.setMessage("Não foram encontrados endereços na base de dados");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
		}
	}
}
