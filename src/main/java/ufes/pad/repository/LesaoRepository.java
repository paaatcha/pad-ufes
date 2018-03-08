package ufes.pad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufes.pad.model.Lesao;

@Repository
public interface LesaoRepository extends JpaRepository<Lesao, Long> {

	
	/*
	@Query("select u from Usuario u where u.email=:email")
	public Usuario buscaPorEmail(@Param("email") String email);
	
	@Query("select u from Usuario u where u.nome_usuario=:nome_usuario")
	public Usuario buscaPorNomeUsuario(@Param("nome_usuario") String nome_usuario);
	
	
	@Query("selec * from Usuario")
	public List<Usuario> buscarTodosUsuarios();
	*/
	
}
