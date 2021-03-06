package ufes.pad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ufes.pad.model.PacienteGeral;

@Repository
public interface PacienteGeralRepository extends JpaRepository<PacienteGeral, Long> {	
	
	@Query("select p from PacienteGeral p where p.cartao_sus=:cartao_sus")
	public PacienteGeral buscaPorCartaoSus(@Param("cartao_sus") String cartao_sus);
	
	public List<PacienteGeral> findByAuditadoFalse ();
	
	@Query(value="SELECT * FROM paciente_geral WHERE (paciente_geral.id IN (SELECT lesao_geral.paciente_id FROM lesao_geral WHERE lesao_geral.diagnostico LIKE %?1%) )", nativeQuery=true)
	public List<PacienteGeral> filtraPorLesao (String lesao);
	
	@Query(value="SELECT * FROM paciente_geral WHERE (paciente_geral.id IN (SELECT lesao_geral.paciente_id FROM lesao_geral WHERE (lesao_geral.obs <> ?1 AND lesao_geral.obs IS NOT NULL) ) )", nativeQuery=true)
	public List<PacienteGeral> filtraPorObs (String obs);
	
}

