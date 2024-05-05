package br.com.gestao.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gestao.commons.Const;
import br.com.gestao.dto.EnderecoDTO;
import br.com.gestao.dto.UsuarioDTO;
import br.com.gestao.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@RequestMapping("/usuarios")
@Tag(name = "Usuario", description = "Operações no cadastro do Usuario")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Pesquisar por ID do cadastro de Usuario")
	public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(usuarioService.findById(id));
	}

	@GetMapping
	@Operation(summary = "Listar todos os cadastros de Usuario")
	public ResponseEntity<List<UsuarioDTO>> findAll() {
		return ResponseEntity.ok(usuarioService.findAll());
	}
	
	@GetMapping("/{id}/enderecos")
	@Operation(summary = "Listar todos os endereços de um usuário")
	public ResponseEntity<Page<EnderecoDTO>> findById(@PathVariable Long id,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer pagina,
			@RequestParam(value = "quantity", required = false, defaultValue = "50") Integer quantidade) {
		return usuarioService.findEnderecoByIdUsuario(id, pagina, quantidade);
	}
	
	@Operation(summary = "Listar todos os Usuarios e mostrar o resultado utilizando paginação")
	@GetMapping(value = "/listar-todos", produces = Const.JSON_TYPE)
	public ResponseEntity<Page<UsuarioDTO>> findAll(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer pagina,
			@RequestParam(value = "quantity", required = false, defaultValue = "50") Integer quantidade) {
		return this.usuarioService.findAll(pagina, quantidade);
	}
	
	@Operation(summary = "Listar todos os Usuarios que tem um nome parecido com o Nome consultado")
	@GetMapping(value = "/listar-por-nome", produces = Const.JSON_TYPE)
	public ResponseEntity<Page<UsuarioDTO>> findByNomeLike(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer pagina,
			@RequestParam(value = "quantity", required = false, defaultValue = "50") Integer quantidade,
			@RequestParam(value = "name", required = false, defaultValue = "") String nome) {
		return this.usuarioService.findByNomeLike(pagina, quantidade, nome);
	}

	@Operation(summary = "Salvar um cadastro completo do Usuario, incluindo os seus endereços")
	@PostMapping(value = "/salvar", headers = { Const.HEADER_ACCEPT_JSON }, produces = Const.JSON_TYPE, consumes = Const.JSON_TYPE)
	public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody UsuarioDTO body) {
		return usuarioService.salvarUsuario(body);
	}
	
	@Operation(summary = "Alterar o endereço para um usuario especifico")
	@PostMapping(value = "/{id}/enderecos/salvar", headers = { Const.HEADER_ACCEPT_JSON }, produces = Const.JSON_TYPE, consumes = Const.JSON_TYPE)
	public ResponseEntity<EnderecoDTO> salvarEndereco(@PathVariable Long id, @RequestBody EnderecoDTO endereco) {
		return usuarioService.salvarEndereco(endereco, id);
	}
	

	@Operation(summary = "Alterar um cadastro completo do Usuario, incluindo os seus endereços")
	@PutMapping(value = "/alterar", headers = { Const.HEADER_ACCEPT_JSON }, produces = Const.JSON_TYPE, consumes = Const.JSON_TYPE)
	ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody UsuarioDTO body) {
		return usuarioService.salvarUsuario(body);
	}
	
	@Operation(summary = "Alterar o endereço para um usuario especifico")
	@PutMapping(value = "/{id}/enderecos/alterar", headers = { Const.HEADER_ACCEPT_JSON }, produces = Const.JSON_TYPE, consumes = Const.JSON_TYPE)
	public ResponseEntity<EnderecoDTO> updateEndereco(@PathVariable Long id, @RequestBody EnderecoDTO endereco) {
		return usuarioService.salvarEndereco(endereco, id);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir cadastro de Usuario")
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {
		return usuarioService.delete(id);
	}
}
