package ufes.pad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufes.pad.model.LesaoGeral;

@Repository
public interface LesaoGeralRepository extends JpaRepository<LesaoGeral, Long> {	

	LesaoGeral findById (Long id);
	
	int countByDiagnosticoLike (String lesao);
	
	/*
	@Query(nativeQuery=true, value="select * from lesao le where le.paciente_id=:paciente_id")
	List<Lesao> buscaLesaoPacienteId (@Param("paciente_id") String paciente_id);	
	
	
	
	
	String inserirQuery = "INSERT INTO lesao(diagnostico_clinico, diagnostico_histo,"
			+ " diametro_maior, diametro_menor, obs, regiao, paciente_id, procedimento)"
			+ " VALUES (:diag_cli,:diag_hist,:dia_maior,:dia_menor,:obs,:reg,:pac_id,:proc)";
	
	@Query(nativeQuery=true, value=inserirQuery )
	@Transactional
	@Modifying
	void inserir (@Param("pac_id") Long pac_id,
			@Param("diag_cli") String diag_cli,
			@Param("diag_hist") String diag_hist,
			@Param("dia_maior") float dia_maior,
			@Param("dia_menor") float dia_menor,
			@Param("obs") String obs,
			@Param("reg") String reg,
			@Param("proc") String proc);
	
	String atualizarQuery = "UPDATE lesao SET diagnostico_clinico=:diag_cli,"
			+ "diagnostico_histo=:diag_hist, diametro_menor=:dia_menor, diametro_maior=:dia_maior"
			+ "obs=:obs, regiao=:reg, paciente_id=:pac_id, procedimento=:proc WHERE id:les_id";
	@Query(nativeQuery=true, value=atualizarQuery)
	@Transactional
	@Modifying
	void atualizar (@Param("les_id") Long les_id,
			@Param("diag_cli") String diag_cli,
			@Param("diag_hist") String diag_hist,
			@Param("dia_maior") float dia_maior,
			@Param("dia_menor") float dia_menor,
			@Param("obs") String obs,
			@Param("reg") String reg,
			@Param("pac_id") Long pac_id,
			@Param("proc") String proc);	
	 */			
}
