package ufes.pad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ufes.pad.model.Paciente;
import ufes.pad.model.Usuario;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {	
	
	@Query("select p from Paciente p where p.cartao_sus=:cartao_sus")
	public Paciente buscaPorCartaoSus(@Param("cartao_sus") String cartao_sus);	
	
	
	@Query("select p from Paciente p where p.nome_completo=:nome_completo")
	public Usuario buscaPorNomeUsuario(@Param("nome_completo") String nome_completo);
	
	/*
	@Query("selec * from Usuario")
	public List<Usuario> buscarTodosUsuarios();
	*/
	
}
