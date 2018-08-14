package ufes.pad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ufes.pad.model.PacienteGeral;

@Repository
public interface PacienteGeralRepository extends JpaRepository<PacienteGeral, Long> {	
	
	@Query("select p from PacienteGeral p where p.cartao_sus=:cartao_sus")
	public PacienteGeral buscaPorCartaoSus(@Param("cartao_sus") String cartao_sus);
	
}

