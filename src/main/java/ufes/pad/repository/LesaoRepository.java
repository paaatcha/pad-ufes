package ufes.pad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufes.pad.model.Lesao;

@Repository
public interface LesaoRepository extends JpaRepository<Lesao, Long> {	

	//List<Lesao> findByPacienteId (String paciente_id);
	
}
