package br.com.gestao.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import br.com.gestao.commons.ResponseWrapper;
import br.com.gestao.dto.EnderecoDTO;
import br.com.gestao.dto.UsuarioDTO;
import br.com.gestao.services.UsuarioService;

// Para carregar apenas o contexto necessário para testar o UsuarioController
@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc
class UsuarioControllerTest {

	@Autowired
	private MockMvc mockMvc;

	//mockando o serviço
	@MockBean
	private UsuarioService usuarioService; 
	
	// Para criar a instância do controller e deixar disponível o endpoint do teste
	@InjectMocks
	private UsuarioController usuarioController;
	
	// ObjectMapper para converter objetos para JSON
	@Autowired
	private ObjectMapper objectMapper;
	
	private List<UsuarioDTO> usuariosResponse = new ArrayList<>();
	
	private UsuarioDTO usuarioResponse;
	
	private EnderecoDTO enderecoResponse;
	
	private List<EnderecoDTO> enderecosResponse = new ArrayList<>();
	
	private Page<EnderecoDTO> pageEndereco;
	
	private Page<UsuarioDTO> pageUsuario;
	
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
		
		usuarioResponse = UsuarioDTO.builder().id(1L).nome("Manoel Rafael Osvaldo Assis").enderecos(enderecosResponse).build();

		usuariosResponse.add(usuarioResponse);

		pageEndereco = new PageImpl<>(enderecosResponse, PageRequest.of(1, 50), enderecosResponse.size());
		
		pageUsuario = new PageImpl<>(usuariosResponse, PageRequest.of(1, 50), usuariosResponse.size());
    }
	
	@Test
	void testeBuscaTodosUsuarios() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<List<UsuarioDTO>>> response = new ResponseEntity<>(new ResponseWrapper<>(usuariosResponse, null), HttpStatus.OK);
		when(usuarioService.findAll()).thenReturn(response);
		
		// Executar a solicitação HTTP GET
		mockMvc.perform(get("/usuarios"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}
	
	@Test
	void testeBuscaUsuarioById() throws Exception {

		// Configurar comportamento simulado do serviço

		ResponseEntity<ResponseWrapper<UsuarioDTO>> response = new ResponseEntity<>(new ResponseWrapper<>(usuarioResponse, null), HttpStatus.OK);
		when(usuarioService.findById(Mockito.any())).thenReturn(response);
		
		// Executar a solicitação HTTP GET
		mockMvc.perform(get("/usuarios/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}
	
	@Test
	void testeBuscaEnderecosDoUsuarioId() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> response = new ResponseEntity<>(new ResponseWrapper<>(pageEndereco, null), HttpStatus.OK);
		when(usuarioService.findEnderecoByIdUsuario(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
		
		// Executar a solicitação HTTP GET
		mockMvc.perform(get("/usuarios/1/enderecos"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}
	
	@Test
	void testeBuscaTodosUsuariosComPaginacao() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<Page<UsuarioDTO>>> response = new ResponseEntity<>(new ResponseWrapper<>(pageUsuario, null), HttpStatus.OK);
		when(usuarioService.findAll(Mockito.any(), Mockito.any())).thenReturn(response);
		
		// Executar a solicitação HTTP GET
		mockMvc.perform(get("/usuarios/listar-todos"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}
	
	@Test
	void testeBuscaTodosUsuariosPeloNomeComPaginacao() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<Page<UsuarioDTO>>> response = new ResponseEntity<>(new ResponseWrapper<>(pageUsuario, null), HttpStatus.OK);
		when(usuarioService.findByNomeLike(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
		
		// Executar a solicitação HTTP GET
		mockMvc.perform(get("/usuarios/listar-por-nome?name=Santana"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}
	
	@Test
	void testeSalvarNovoUsuario() throws Exception {
		
		//motando o body request
		UsuarioDTO bodyRequest = UsuarioDTO.builder().id(1L).nome("Manoel Rafael Osvaldo Assis").enderecos(enderecosResponse).build();
		
		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<UsuarioDTO>> response = new ResponseEntity<>(new ResponseWrapper<>(usuarioResponse, null), HttpStatus.CREATED);
		when(usuarioService.salvarUsuario(Mockito.any(UsuarioDTO.class))).thenReturn(response);
		
		MvcResult mvcResult = mockMvc.perform(post("/usuarios/salvar")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Content-Type", "application/json")
                .accept("application/json;charset=UTF-8")
				.content(objectMapper.writeValueAsString(bodyRequest)))
				.andExpect(status().isCreated())
                .andReturn(); 
        
        String responseBody = mvcResult.getResponse().getContentAsString();
		ResponseWrapper<UsuarioDTO> resultado = objectMapper.readValue(responseBody, new TypeReference<ResponseWrapper<UsuarioDTO>>() {});
        assertThat(resultado.getData().getId()).isNotNull();
	}
	
	@Test
	void testeAlterarUsuarioExistente() throws Exception {
		
		//motando o body request
		UsuarioDTO bodyRequest = UsuarioDTO.builder().id(1L).nome("Manoel Rafael Osvaldo Assis").enderecos(enderecosResponse).build();
		
		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<UsuarioDTO>> response = new ResponseEntity<>(new ResponseWrapper<>(usuarioResponse, null), HttpStatus.CREATED);
		when(usuarioService.salvarUsuario(Mockito.any(UsuarioDTO.class))).thenReturn(response);
		
		MvcResult mvcResult = mockMvc.perform(put("/usuarios/alterar")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Content-Type", "application/json")
                .accept("application/json;charset=UTF-8")
				.content(objectMapper.writeValueAsString(bodyRequest)))
				.andExpect(status().isCreated())
                .andReturn(); 
		
		String responseBody = mvcResult.getResponse().getContentAsString();
		ResponseWrapper<UsuarioDTO> resultado = objectMapper.readValue(responseBody, new TypeReference<ResponseWrapper<UsuarioDTO>>() {});
        assertThat(resultado.getData().getId()).isNotNull();

	}
	
	@Test
	void testeSalvarNovoEnderecoParaUsuarioExistente() throws Exception {
		
		//motando o body request
		EnderecoDTO bodyRequest = EnderecoDTO.builder()
				.id(1L)
				.cep("59248970")
				.logradouro("Estrada do Cajueiro, s/n")
				.numero("168")
				.estado("RN")
				.cidade("Lagoa Salgada")
				.build();
		
		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<EnderecoDTO>> response = new ResponseEntity<>(new ResponseWrapper<>(enderecoResponse, null), HttpStatus.CREATED);
		when(usuarioService.salvarEndereco(Mockito.any(EnderecoDTO.class),Mockito.any())).thenReturn(response);
		
		MvcResult mvcResult = mockMvc.perform(post("/usuarios/1/enderecos/salvar")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Content-Type", "application/json")
                .accept("application/json;charset=UTF-8")
				.content(objectMapper.writeValueAsString(bodyRequest)))
				.andExpect(status().isCreated())
                .andReturn(); 
        
        String responseBody = mvcResult.getResponse().getContentAsString();
		ResponseWrapper<EnderecoDTO> resultado = objectMapper.readValue(responseBody, new TypeReference<ResponseWrapper<EnderecoDTO>>() {});
        assertThat(resultado.getData().getId()).isNotNull();
	}
	
	@Test
	void testeAlterarEnderecoParaUsuarioExistente() throws Exception {
		
		//motando o body request
		EnderecoDTO bodyRequest = EnderecoDTO.builder()
				.id(1L)
				.cep("59248970")
				.logradouro("Estrada do Cajueiro, s/n")
				.numero("168")
				.estado("RN")
				.cidade("Lagoa Salgada")
				.build();
		
		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<EnderecoDTO>> response = new ResponseEntity<>(new ResponseWrapper<>(enderecoResponse, null), HttpStatus.CREATED);
		when(usuarioService.salvarEndereco(Mockito.any(EnderecoDTO.class),Mockito.any())).thenReturn(response);
		
		MvcResult mvcResult = mockMvc.perform(put("/usuarios/1/enderecos/alterar")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Content-Type", "application/json")
                .accept("application/json;charset=UTF-8")
				.content(objectMapper.writeValueAsString(bodyRequest)))
				.andExpect(status().isCreated())
                .andReturn(); 
        
        String responseBody = mvcResult.getResponse().getContentAsString();
		ResponseWrapper<EnderecoDTO> resultado = objectMapper.readValue(responseBody, new TypeReference<ResponseWrapper<EnderecoDTO>>() {});
        assertThat(resultado.getData().getId()).isNotNull();
	}
	
	@Test
	void testeDeletaUsuarioComSucesso() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<String>> response = new ResponseEntity<>(new ResponseWrapper<>("Sucesso!!", null), HttpStatus.OK);
		when(usuarioService.deleteUsuario(Mockito.any())).thenReturn(response);
		
		mockMvc.perform(delete("/usuarios/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)); 
	}

	@Test
	void testeDeletaUsuarioComErro() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<String>> response = new ResponseEntity<>(new ResponseWrapper<>("usuario com id <1> não encontrado !!!", null), HttpStatus.NOT_FOUND);
		when(usuarioService.deleteUsuario(Mockito.any())).thenReturn(response);
		
		mockMvc.perform(delete("/usuarios/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)); 

	}
	
	//
	@Test
	void testeDeletaEnderecoDoUsuarioComSucesso() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<String>> response = new ResponseEntity<>(new ResponseWrapper<>("usuario com id <1> não encontrado !!!", null), HttpStatus.OK);
		when(usuarioService.deleteEndereco(Mockito.any(),Mockito.any())).thenReturn(response);
		
		mockMvc.perform(delete("/usuarios/{idUsuario}/endereco/{idEndereco}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void testeDeletaEnderecoDoUsuarioComErro() throws Exception {

		// Configurar comportamento simulado do serviço
		ResponseEntity<ResponseWrapper<String>> response = new ResponseEntity<>(new ResponseWrapper<>("usuario com id <1> não encontrado !!!", null), HttpStatus.NOT_FOUND);
		when(usuarioService.deleteEndereco(Mockito.any(),Mockito.any())).thenReturn(response);

		mockMvc.perform(delete("/usuarios/{idUsuario}/endereco/{idEndereco}", 1L, 1L)
				  .contentType(MediaType.APPLICATION_JSON)
				  .accept(MediaType.APPLICATION_JSON))
				  .andExpect(status().isNotFound())
				  .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
}
