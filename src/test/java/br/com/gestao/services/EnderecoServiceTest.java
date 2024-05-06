package br.com.gestao.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.gestao.commons.ResponseWrapper;
import br.com.gestao.dto.EnderecoDTO;
import br.com.gestao.entities.Endereco;
import br.com.gestao.repositories.EnderecoRepository;

class EnderecoServiceTest {

	@InjectMocks
	private EnderecoService enderecoService;

	@Mock
	private EnderecoRepository enderecoRepository;

	@Mock
	private ModelMapper modelMapper;

	Endereco endereco;
	List<Endereco> enderecos = new ArrayList<>();
	List<Endereco> enderecosEmpty = new ArrayList<>();
	
	private Page<Endereco> pageEndereco;
	private Page<Endereco> pageEnderecoEmpty;

	@BeforeEach
	public void setUp() {

		// monta a resposta do repository
		endereco = Endereco.builder().id(1L).cep("59248970").logradouro("Estrada do Cajueiro, s/n").numero("168")
				.estado("RN").cidade("Lagoa Salgada").build();
		enderecos.add(endereco);
		pageEndereco = new PageImpl<>(enderecos);
		pageEnderecoEmpty = new PageImpl<>(enderecosEmpty);
		
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testeBuscaEnderecoPorIdComSucesso() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(endereco));

		// Configuração do comportamento simulado do ModelMapper
		when(modelMapper.map(any(), eq(EnderecoDTO.class))).thenAnswer(invocation -> {
			Endereco endereco1 = invocation.getArgument(0);
			EnderecoDTO enderecoDTO = EnderecoDTO.builder().id(endereco1.getId()).cep(endereco1.getCep())
					.logradouro(endereco1.getLogradouro()).numero(endereco1.getNumero()).estado(endereco1.getEstado())
					.cidade(endereco1.getCidade()).principal(endereco1.getPrincipal()).build();
			return enderecoDTO;
		});

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<EnderecoDTO>> responseEntity = enderecoService.findById(endereco.getId());

		// Verificação do resultado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(endereco.getId(), responseEntity.getBody().getData().getId());
		assertEquals(null, responseEntity.getBody().getMessage());
	}

	@Test
	void testeBuscaEnderecoPorIdComErro() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<EnderecoDTO>> responseEntity = enderecoService.findById(endereco.getId());

		// Verificação do resultado
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	void testeBuscaTodosEnderecosComSucesso() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findAll()).thenReturn(enderecos);

		// Configuração do comportamento simulado do ModelMapper
		when(modelMapper.map(any(), eq(EnderecoDTO.class))).thenAnswer(invocation -> {
			Endereco endereco1 = invocation.getArgument(0);
			EnderecoDTO enderecoDTO = EnderecoDTO.builder().id(endereco1.getId()).cep(endereco1.getCep())
					.logradouro(endereco1.getLogradouro()).numero(endereco1.getNumero()).estado(endereco1.getEstado())
					.cidade(endereco1.getCidade()).principal(endereco1.getPrincipal()).build();
			return enderecoDTO;
		});

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<List<EnderecoDTO>>> responseEntity = enderecoService.findAll();

		// Verificação do resultado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody().getData());
		assertEquals(null, responseEntity.getBody().getMessage());
	}
	
	@Test
	void testeBuscaTodosEnderecosComErro() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findAll()).thenReturn(enderecosEmpty);

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<List<EnderecoDTO>>> responseEntity = enderecoService.findAll();

		// Verificação do resultado
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	void testeBuscaTodosEnderecosPeloCepComSucesso() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findByCep(Mockito.any(), Mockito.any())).thenReturn(pageEndereco);

		// Configuração do comportamento simulado do ModelMapper
		when(modelMapper.map(any(), eq(EnderecoDTO.class))).thenAnswer(invocation -> {
			Endereco endereco1 = invocation.getArgument(0);
			EnderecoDTO enderecoDTO = EnderecoDTO.builder().id(endereco1.getId()).cep(endereco1.getCep())
					.logradouro(endereco1.getLogradouro()).numero(endereco1.getNumero()).estado(endereco1.getEstado())
					.cidade(endereco1.getCidade()).principal(endereco1.getPrincipal()).build();
			return enderecoDTO;
		});

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> responseEntity = enderecoService.findByCep(1, 50, endereco.getCep());

		// Verificação do resultado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(endereco.getCep(), responseEntity.getBody().getData().getContent().get(0).getCep());
		assertEquals(null, responseEntity.getBody().getMessage());
	}
	
	@Test
	void testeBuscaTodosEnderecosPeloCepComErro() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findByCep(Mockito.any(), Mockito.any())).thenReturn(pageEnderecoEmpty);

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> responseEntity = enderecoService.findByCep(1, 50, endereco.getCep());

		// Verificação do resultado
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	void testeBuscaTodosEnderecosPelaCidadeComSucesso() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findByCidade(Mockito.any(), Mockito.any())).thenReturn(pageEndereco);

		// Configuração do comportamento simulado do ModelMapper
		when(modelMapper.map(any(), eq(EnderecoDTO.class))).thenAnswer(invocation -> {
			Endereco endereco1 = invocation.getArgument(0);
			EnderecoDTO enderecoDTO = EnderecoDTO.builder().id(endereco1.getId()).cep(endereco1.getCep())
					.logradouro(endereco1.getLogradouro()).numero(endereco1.getNumero()).estado(endereco1.getEstado())
					.cidade(endereco1.getCidade()).principal(endereco1.getPrincipal()).build();
			return enderecoDTO;
		});

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> responseEntity = enderecoService.findByCidade(1, 50, endereco.getCidade());

		// Verificação do resultado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(endereco.getCidade(), responseEntity.getBody().getData().getContent().get(0).getCidade());
		assertEquals(null, responseEntity.getBody().getMessage());
	}
	
	@Test
	void testeBuscaTodosEnderecosPelaCidadeComErro() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findByCidade(Mockito.any(), Mockito.any())).thenReturn(pageEnderecoEmpty);

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> responseEntity = enderecoService.findByCidade(1, 50, endereco.getCidade());

		// Verificação do resultado
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	void testeBuscaTodosEnderecosPeloEstadoComSucesso() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findByEstado(Mockito.any(), Mockito.any())).thenReturn(pageEndereco);

		// Configuração do comportamento simulado do ModelMapper
		when(modelMapper.map(any(), eq(EnderecoDTO.class))).thenAnswer(invocation -> {
			Endereco endereco1 = invocation.getArgument(0);
			EnderecoDTO enderecoDTO = EnderecoDTO.builder().id(endereco1.getId()).cep(endereco1.getCep())
					.logradouro(endereco1.getLogradouro()).numero(endereco1.getNumero()).estado(endereco1.getEstado())
					.cidade(endereco1.getCidade()).principal(endereco1.getPrincipal()).build();
			return enderecoDTO;
		});

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> responseEntity = enderecoService.findByEstado(1, 50, endereco.getCidade());

		// Verificação do resultado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(endereco.getEstado(), responseEntity.getBody().getData().getContent().get(0).getEstado());
		assertEquals(null, responseEntity.getBody().getMessage());
	}
	
	@Test
	void testeBuscaTodosEnderecosPeloEstadoComErro() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findByEstado(Mockito.any(), Mockito.any())).thenReturn(pageEnderecoEmpty);

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> responseEntity = enderecoService.findByEstado(1, 50, endereco.getEstado());

		// Verificação do resultado
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	void testeBuscaTodosEnderecosComPaginacaoRetornaSucesso() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.listAllByPages(Mockito.any())).thenReturn(pageEndereco);

		// Configuração do comportamento simulado do ModelMapper
		when(modelMapper.map(any(), eq(EnderecoDTO.class))).thenAnswer(invocation -> {
			Endereco endereco1 = invocation.getArgument(0);
			EnderecoDTO enderecoDTO = EnderecoDTO.builder().id(endereco1.getId()).cep(endereco1.getCep())
					.logradouro(endereco1.getLogradouro()).numero(endereco1.getNumero()).estado(endereco1.getEstado())
					.cidade(endereco1.getCidade()).principal(endereco1.getPrincipal()).build();
			return enderecoDTO;
		});

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> responseEntity = enderecoService.findAll(1, 50);

		// Verificação do resultado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody().getData().getContent());
		assertEquals(null, responseEntity.getBody().getMessage());
	}
	
	@Test
	void testeBuscaTodosEnderecosComPaginacaoRetornaErro() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.listAllByPages(Mockito.any())).thenReturn(pageEnderecoEmpty);

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>> responseEntity = enderecoService.findAll(1, 50);

		// Verificação do resultado
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
}
