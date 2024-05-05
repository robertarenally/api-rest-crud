package br.com.gestao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.gestao.entities.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

	Page<Endereco> findByCep(String cep, PageRequest pageRequest);
	
	Page<Endereco> findByCidade(String cep, PageRequest pageRequest);
	
	Page<Endereco> findByEstado(String cep, PageRequest pageRequest);
	
	@Query("select e from Endereco e")
	Page<Endereco> listAllByPages(Pageable pageable);
}
