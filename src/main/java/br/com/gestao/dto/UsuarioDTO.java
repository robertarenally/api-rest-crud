package br.com.gestao.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Usuario")
public class UsuarioDTO {

	private Long id;
	
	private String nome;
	
	private Date dataNascimento;
	
	private List<EnderecoDTO> enderecos = new ArrayList<>();
}
