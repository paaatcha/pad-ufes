package ufes.pad.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ufes.pad.model.Lesao;

@Repository
public interface LesaoRepository extends JpaRepository<Lesao, Long> {
			
	@Query("SELECT les.id FROM Lesao les WHERE les.local_atendimento LIKE %:local%")
	List<Long> filtraLesoesPorLocal (@Param("local") String local);
	
	
	@Query("SELECT les.id FROM Lesao les WHERE les.local_atendimento LIKE %:local% AND les.data_atendimento between :dataInicio and :dataFim")
	List<Long> filtraLesoesPorLocalData (@Param("local") String local, @Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim);

}
 