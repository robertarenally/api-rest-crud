package br.com.gestao.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gestao.commons.Const;
import br.com.gestao.dto.EnderecoDTO;
import br.com.gestao.services.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@RequestMapping("/enderecos")
@Tag(name = "Endereco", description = "Operações no cadastro do Endereco")
public class EnderecoController {

	private final EnderecoService enderecoService;

	public EnderecoController(EnderecoService enderecoService) {
		this.enderecoService = enderecoService;
	}

	@GetMapping("/{id}")
	@Operation(summary = "Pesquisar por ID do cadastro de Endereco")
	public ResponseEntity<EnderecoDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(enderecoService.findById(id));
	}

	@GetMapping
	@Operation(summary = "Listar todos os cadastros de Endereco")
	public ResponseEntity<List<EnderecoDTO>> findAll() {
		return ResponseEntity.ok(enderecoService.findAll());
	}
	
	@Operation(summary = "Listar todos os endereços por cep")
	@GetMapping(value = "/listar-por-cep", produces = Const.JSON_TYPE)
	public ResponseEntity<Page<EnderecoDTO>> findByCep(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer pagina,
			@RequestParam(value = "quantity", required = false, defaultValue = "50") Integer quantidade,
			@RequestParam(value = "cep", required = false, defaultValue = "") String cep) {
		return this.enderecoService.findByCep(pagina, quantidade, cep);
	}
	
	@Operation(summary = "Listar todos os endereços por cidade")
	@GetMapping(value = "/listar-por-cidade", produces = Const.JSON_TYPE)
	public ResponseEntity<Page<EnderecoDTO>> findByCidade(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer pagina,
			@RequestParam(value = "quantity", required = false, defaultValue = "50") Integer quantidade,
			@RequestParam(value = "city", required = false, defaultValue = "") String cidade) {
		return this.enderecoService.findByCidade(pagina, quantidade, cidade);
	}
	
	@Operation(summary = "Listar todos os endereços por estado")
	@GetMapping(value = "/listar-por-estado", produces = Const.JSON_TYPE)
	public ResponseEntity<Page<EnderecoDTO>> findByestado(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer pagina,
			@RequestParam(value = "quantity", required = false, defaultValue = "50") Integer quantidade,
			@RequestParam(value = "state", required = false, defaultValue = "") String estado) {
		return this.enderecoService.findByEstado(pagina, quantidade, estado);
	}
	
	@Operation(summary = "Listar todos os endereços e mostrar o resultado utilizando paginação")
	@GetMapping(value = "/listar-todos", produces = Const.JSON_TYPE)
	public ResponseEntity<Page<EnderecoDTO>> findAll(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer pagina,
			@RequestParam(value = "quantity", required = false, defaultValue = "50") Integer quantidade) {
		return this.enderecoService.findAll(pagina, quantidade);
	}
}
