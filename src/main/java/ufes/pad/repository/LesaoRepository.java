package ufes.pad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ufes.pad.model.Lesao;

@Repository
public interface LesaoRepository extends JpaRepository<Lesao, Long> {	

	Lesao findById (String id);
	
	@Query(nativeQuery=true, value="select * from lesao le where le.paciente_id=:paciente_id")
	List<Lesao> buscaLesaoPacienteId (@Param("paciente_id") String paciente_id);	
	
}
