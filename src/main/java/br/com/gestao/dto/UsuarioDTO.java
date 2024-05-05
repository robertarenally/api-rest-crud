package br.com.gestao.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

import br.com.gestao.commons.deserializers.DateDeserializer;
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
@Schema(name = "Usuario")
public class UsuarioDTO {

	private Long id;
	
	private String nome;
	
	@JsonDeserialize(using = DateDeserializer.class)
	@JsonSerialize(using = DateSerializer.class)
	private Date dataNascimento;
	
	@JsonManagedReference
	@Builder.Default
	private List<EnderecoDTO> enderecos = new ArrayList<>();
}
