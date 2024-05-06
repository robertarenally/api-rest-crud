package br.com.gestao.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.gestao.commons.ResponseWrapper;
import br.com.gestao.dto.EnderecoDTO;
import br.com.gestao.dto.UsuarioDTO;
import br.com.gestao.entities.Endereco;
import br.com.gestao.entities.Usuario;
import br.com.gestao.repositories.EnderecoRepository;
import br.com.gestao.repositories.UsuarioRepository;

class UsuarioServiceTest {

	@InjectMocks
	private UsuarioService usuarioService;

	@Mock
	private EnderecoRepository enderecoRepository;
	
	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private ModelMapper modelMapper;

	Endereco endereco;
	List<Endereco> enderecos = new ArrayList<>();
	List<Endereco> enderecosEmpty = new ArrayList<>();
	
	private Page<Endereco> pageEndereco;
	private Page<Endereco> pageEnderecoEmpty;
	
	Usuario usuario;
	List<Usuario> usuarios = new ArrayList<>();
	List<Usuario> usuariosEmpty = new ArrayList<>();
	
	private Page<Usuario> pageUsuario;
	private Page<Usuario> pageUsuarioEmpty;
	
	@BeforeEach
	public void setUp() {

		// monta a resposta do repository
		endereco = Endereco.builder().id(1L).cep("59248970").logradouro("Estrada do Cajueiro, s/n").numero("168")
				.estado("RN").cidade("Lagoa Salgada").build();
		enderecos.add(endereco);
		pageEndereco = new PageImpl<>(enderecos);
		pageEnderecoEmpty = new PageImpl<>(enderecosEmpty);
		
		usuario = Usuario.builder().id(1L).nome("Manoel Rafael Osvaldo Assis").enderecos(enderecos).build();
		usuarios.add(usuario);
		
		pageUsuario = new PageImpl<>(usuarios);
		pageUsuarioEmpty = new PageImpl<>(usuariosEmpty);
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testeBuscaUsuarioPorIdComSucesso() throws Exception {

		// Configurar comportamento simulado do repository
		when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(usuario));

		// Configuração do comportamento simulado do ModelMapper
		when(modelMapper.map(any(Usuario.class), eq(UsuarioDTO.class))).thenAnswer(invocation -> {
			Usuario usuario1 = invocation.getArgument(0);
			Endereco endereco1 = usuario1.getEnderecos().get(0);
			EnderecoDTO enderecoDTO = EnderecoDTO.builder().id(endereco1.getId()).cep(endereco1.getCep())
					.logradouro(endereco1.getLogradouro()).numero(endereco1.getNumero()).estado(endereco1.getEstado())
					.cidade(endereco1.getCidade()).principal(endereco1.getPrincipal()).build();
			List<EnderecoDTO> enderecos = new ArrayList<>();
			enderecos.add(enderecoDTO);
			UsuarioDTO usuarioDTO = UsuarioDTO.builder().id(usuario1.getId()).nome(usuario1.getNome())
					.enderecos(enderecos).build();
			return usuarioDTO;
		});

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<UsuarioDTO>> responseEntity = usuarioService.findById(usuario.getId());

		// Verificação do resultado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(usuario.getId(), responseEntity.getBody().getData().getId());
		assertEquals(null, responseEntity.getBody().getMessage());
	}
	
	@Test
	void testeBuscaUsuarioPorIdComErro() throws Exception {

		// Configurar comportamento simulado do repository
		when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<UsuarioDTO>> responseEntity = usuarioService.findById(usuario.getId());

		// Verificação do resultado
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	void testeBuscaTodosUsuariosRetornaSucesso() throws Exception {

		// Configurar comportamento simulado do repository
		when(usuarioRepository.findAll()).thenReturn(usuarios);

		// Configuração do comportamento simulado do ModelMapper
		when(modelMapper.map(any(), eq(UsuarioDTO.class))).thenAnswer(invocation -> {
			Usuario usuario1 = invocation.getArgument(0);
			Endereco endereco1 = usuario1.getEnderecos().get(0);
			EnderecoDTO enderecoDTO = EnderecoDTO.builder().id(endereco1.getId()).cep(endereco1.getCep())
					.logradouro(endereco1.getLogradouro()).numero(endereco1.getNumero()).estado(endereco1.getEstado())
					.cidade(endereco1.getCidade()).principal(endereco1.getPrincipal()).build();
			List<EnderecoDTO> enderecos = new ArrayList<>();
			enderecos.add(enderecoDTO);
			UsuarioDTO usuarioDTO = UsuarioDTO.builder().id(usuario1.getId()).nome(usuario1.getNome())
					.enderecos(enderecos).build();
			return usuarioDTO;
		});

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<List<UsuarioDTO>>> responseEntity = usuarioService.findAll();

		// Verificação do resultado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody().getData());
		assertEquals(null, responseEntity.getBody().getMessage());
	}
	
	@Test
	void testeBuscaTodosUsuariosRetornaErro() throws Exception {

		// Configurar comportamento simulado do repository
		when(usuarioRepository.findAll()).thenReturn(usuariosEmpty);

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<List<UsuarioDTO>>> responseEntity = usuarioService.findAll();

		// Verificação do resultado
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	void testeBuscaTodosUsuariosComMesmoNomeRetornaSucesso() throws Exception {

		// Configurar comportamento simulado do repository
		when(usuarioRepository.findByNomeLike(Mockito.any(), Mockito.any())).thenReturn(pageUsuario);

		// Configuração do comportamento simulado do ModelMapper
		when(modelMapper.map(any(), eq(UsuarioDTO.class))).thenAnswer(invocation -> {
			Usuario usuario1 = invocation.getArgument(0);
			Endereco endereco1 = usuario1.getEnderecos().get(0);
			EnderecoDTO enderecoDTO = EnderecoDTO.builder().id(endereco1.getId()).cep(endereco1.getCep())
					.logradouro(endereco1.getLogradouro()).numero(endereco1.getNumero()).estado(endereco1.getEstado())
					.cidade(endereco1.getCidade()).principal(endereco1.getPrincipal()).build();
			List<EnderecoDTO> enderecos = new ArrayList<>();
			enderecos.add(enderecoDTO);
			UsuarioDTO usuarioDTO = UsuarioDTO.builder().id(usuario1.getId()).nome(usuario1.getNome())
					.enderecos(enderecos).build();
			return usuarioDTO;
		});

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<UsuarioDTO>>> responseEntity = usuarioService.findByNomeLike(1, 50, usuario.getNome());

		// Verificação do resultado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(usuario.getNome(), responseEntity.getBody().getData().getContent().get(0).getNome());
		assertEquals(null, responseEntity.getBody().getMessage());
	}
	
	@Test
	void testeBuscaTodosUsuariosComMesmoNomeRetornaErro() throws Exception {

		// Configurar comportamento simulado do repository
		when(usuarioRepository.findByNomeLike(Mockito.any(), Mockito.any())).thenReturn(pageUsuarioEmpty);

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<UsuarioDTO>>> responseEntity = usuarioService.findByNomeLike(1, 50, usuario.getNome());

		// Verificação do resultado
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	void testeBuscaTodosEnderecosDeUmUsuarioRetornaSucesso() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findByUsuarioId(Mockito.any(), Mockito.any())).thenReturn(pageEndereco);

		// Configuração do comportamento simulado do ModelMapper
		when(modelMapper.map(any(), eq(EnderecoDTO.class))).thenAnswer(invocation -> {
			Endereco endereco1 = invocation.getArgument(0);
			EnderecoDTO enderecoDTO = EnderecoDTO.builder().id(endereco1.getId()).cep(endereco1.getCep())
					.logradouro(endereco1.getLogradouro()).numero(endereco1.getNumero()).estado(endereco1.getEstado())
					.cidade(endereco1.getCidade()).principal(endereco1.getPrincipal()).build();
			return enderecoDTO;
		});

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>>  responseEntity = usuarioService.findEnderecoByIdUsuario(usuario.getId(), 1, 50);

		// Verificação do resultado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody().getData().getContent());
		assertEquals(null, responseEntity.getBody().getMessage());
	}
	
	@Test
	void testeBuscaTodosEnderecosDeUmUsuarioRetornaErro() throws Exception {

		// Configurar comportamento simulado do repository
		when(enderecoRepository.findByUsuarioId(Mockito.any(), Mockito.any())).thenReturn(pageEnderecoEmpty);

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<EnderecoDTO>>>  responseEntity = usuarioService.findEnderecoByIdUsuario(usuario.getId(), 1, 50);

		// Verificação do resultado
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	void testeBuscaTodosUsuariosComPaginacaoRetornaSucesso() throws Exception {

		// Configurar comportamento simulado do repository
		when(usuarioRepository.listAllByPages(Mockito.any())).thenReturn(pageUsuario);

		// Configuração do comportamento simulado do ModelMapper
		when(modelMapper.map(any(), eq(UsuarioDTO.class))).thenAnswer(invocation -> {
			Usuario usuario1 = invocation.getArgument(0);
			Endereco endereco1 = usuario1.getEnderecos().get(0);
			EnderecoDTO enderecoDTO = EnderecoDTO.builder().id(endereco1.getId()).cep(endereco1.getCep())
					.logradouro(endereco1.getLogradouro()).numero(endereco1.getNumero()).estado(endereco1.getEstado())
					.cidade(endereco1.getCidade()).principal(endereco1.getPrincipal()).build();
			List<EnderecoDTO> enderecos = new ArrayList<>();
			enderecos.add(enderecoDTO);
			UsuarioDTO usuarioDTO = UsuarioDTO.builder().id(usuario1.getId()).nome(usuario1.getNome())
					.enderecos(enderecos).build();
			return usuarioDTO;
		});

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<UsuarioDTO>>> responseEntity = usuarioService.findAll(1, 50);

		// Verificação do resultado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody().getData().getContent());
		assertEquals(null, responseEntity.getBody().getMessage());
	}
	
	@Test
	void testeBuscaTodosUsuariosComPaginacaoRetornaErro() throws Exception {

		// Configurar comportamento simulado do repository
		when(usuarioRepository.listAllByPages(Mockito.any())).thenReturn(pageUsuarioEmpty);

		// Execução do método a ser testado
		ResponseEntity<ResponseWrapper<Page<UsuarioDTO>>> responseEntity = usuarioService.findAll(1, 50);

		// Verificação do resultado
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
}
