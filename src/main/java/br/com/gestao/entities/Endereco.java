package br.com.gestao.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.gestao.commons.Const;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Const.TB_ENDERECO)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "cep", length = 8, nullable = false)
	private String cep;
	
	@Column(name = "logradouro", length = 200, nullable = true)
	private String logradouro;
	
	@Column(name = "cidade", length = 200, nullable = true)
	private String cidade;
	
	@Column(name = "estado", length = 200, nullable = true)
	private String estado;
	
	@Column(name = "numero", length = 3, nullable = true)
	private String numero;
	
	//FLAG QUE INDICA SE E OU NAO O ENDEREO PRINCIPAL
	//VALOR DEFAULT FALSE
	@Column(name = "principal")
	@Builder.Default
	private Boolean principal = Boolean.FALSE;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_usuario")
	@JsonManagedReference // indica que no relacionamento bi-direcional esse é o "pai"
	private Usuario usuario;
	
	@PrePersist
	void prePersist() {
		if(this.principal == null)
			this.principal = Boolean.FALSE;
		if(this.cep != null)
			this.cep = cep.replaceAll("[^a-zA-Z0-9 ]", "");
	}

	@PreUpdate
	void preUpdate() {
		if(this.principal == null)
			this.principal = Boolean.FALSE;
		if(this.cep != null)
			this.cep = cep.replaceAll("[^a-zA-Z0-9 ]", "");
	}
	
	@PostLoad
	void postLoad() {
		if(this.principal == null)
			this.principal = Boolean.FALSE;
	}
}
