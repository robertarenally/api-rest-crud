package br.com.gestao.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.gestao.commons.Const;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Const.TB_USUARIO)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "nome_completo", length = 200, nullable = true)
	private String nome;
	
	@Column(name = "data_nascimento")
	private Date dataNascimento;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario", targetEntity = Endereco.class, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Builder.Default
	@JsonBackReference // indica que no relacionamento bi-direcional esse é o "filho"
	private List<Endereco> enderecos = new ArrayList<>();
}
