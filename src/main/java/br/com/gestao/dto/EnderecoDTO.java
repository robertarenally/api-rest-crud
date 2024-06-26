package br.com.gestao.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Endereco")
public class EnderecoDTO {

	private Long id;
	
	private String cep;
	
	private String logradouro;
	
	private String cidade;
	
	private String estado;
	
	private String numero;
	
	private Boolean principal;
	
	@JsonBackReference
	private UsuarioDTO usuario;
}
