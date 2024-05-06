package br.com.gestao.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gestao.commons.ResponseWrapper;
import br.com.gestao.dto.EnderecoDTO;
import br.com.gestao.services.EnderecoService;

// Para carregar apenas o contexto necessário para testar o UsuarioController
@WebMvcTest(EnderecoController.class)
@AutoConfigureMockMvc
class EnderecoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	//mockando o serviço
	@MockBean
	private EnderecoService enderecoService; 
	
	// Para criar a instância do controller e deixar disponível o endpoint do teste
	@InjectMocks
	private UsuarioController usuarioController;
	
	// ObjectMapper para converter objetos para JSON
	@Autowired
	private ObjectMapper objectMapper;
	
	private EnderecoDTO enderecoResponse;
	
	private List<EnderecoDTO> enderecosResponse = new ArrayList<>();
	
	private Page<EnderecoDTO> pageEndereco;
	
	@BeforeEach
    public void setUp() {
		// monta a resposta do serviço
		enderecoResponse = EnderecoDTO.builder()
				.id(1L)
				.cep("59248970")
				.logradouro("Estrada do Cajueiro, s/n")
				.numero("168")
				.estado("RN")
				.cidade("Lagoa Salgada")
				.build();
		
		enderecosResponse.add(enderecoResponse);
		
		pageEndereco = new PageImpl<>(enderecosResponse, PageRequest.of(1, 50), enderecosResponse.size());
    }
	
	@Test
	void testeBuscaEnderecoPorId() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<EnderecoDTO>> response = new ResponseEntity<>(new ResponseWrapper<>(enderecoResponse, null),HttpStatus.OK);
		when(enderecoService.findById(Mockito.any())).thenReturn(response);
		
		// Executar a solicitação HTTP GET
		mockMvc.perform(get("/enderecos/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}
	
	@Test
	void testeBuscaTodosEnderecos() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<List<EnderecoDTO>>> response = new ResponseEntity<>(new ResponseWrapper<>(enderecosResponse, null), HttpStatus.OK);
		when(enderecoService.findAll()).thenReturn(response);
		
		// Executar a solicitação HTTP GET
		mockMvc.perform(get("/enderecos/"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}
	
	@Test
	void testeBuscaTodosEnderecosPorCep() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> response = new ResponseEntity<>(new ResponseWrapper<>(pageEndereco, null), HttpStatus.OK);
		when(enderecoService.findByCep(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
		
		// Executar a solicitação HTTP GET
		mockMvc.perform(get("/enderecos/listar-por-cep?cep=69900025"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}
	
	@Test
	void testeBuscaTodosEnderecosPorCidade() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> response = new ResponseEntity<>(
				new ResponseWrapper<>(pageEndereco, null), HttpStatus.OK);
		when(enderecoService.findByCidade(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
		
		// Executar a solicitação HTTP GET
		mockMvc.perform(get("/enderecos/listar-por-cidade?city=Santana"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}
	
	@Test
	void testeBuscaTodosEnderecosPorEstado() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> response = new ResponseEntity<>(
				new ResponseWrapper<>(pageEndereco, null), HttpStatus.OK);
		when(enderecoService.findByEstado(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
		
		// Executar a solicitação HTTP GET
		mockMvc.perform(get("/enderecos/listar-por-estado?state=AP"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}
	
	@Test
	void testeBuscaTodosEnderecosEhMostrarResultadoPorPagina() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> response = new ResponseEntity<>(
				new ResponseWrapper<>(pageEndereco, null), HttpStatus.OK);
		when(enderecoService.findAll(Mockito.any(), Mockito.any())).thenReturn(response);
		
		// Executar a solicitação HTTP GET
		mockMvc.perform(get("/enderecos/listar-todos"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}

}
