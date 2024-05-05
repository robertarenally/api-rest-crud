package br.com.gestao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.gestao.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	//USUARIO.NOME LIKE '% NOME %'
	Page<Usuario> findByNomeLike(String nome, PageRequest pageRequest);
	
	@Query("select u from Usuario u")
	Page<Usuario> listAllByPages(Pageable pageable);
}
