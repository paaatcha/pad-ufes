package ufes.pad.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ufes.pad.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {	
	
	@Query("select p from Paciente p where p.cartao_sus=:cartao_sus")
	public Paciente buscaPorCartaoSus(@Param("cartao_sus") String cartao_sus);	
	
	
	@Query("select p from Paciente p where p.nome_completo=:nome_completo")
	public Paciente buscaPorNomeUsuario(@Param("nome_completo") String nome_completo);	
	
	@Query("select p from Paciente p where p.id=:id")
	public Paciente buscaPorId(@Param("id") Long id);
			
	@Query(nativeQuery=true, value="select * from paciente order by paciente.nome_completo")
	List<Paciente> listarPacientes ();	
	
	//select * from lesao where diagnostico_histo LIKE "%%" and diagnostico_clinico LIKE "%%"
	//@Query("select p from Paciente p where p.local_atendimento like %:local% and p.nome_completo like %:nome% order by p.nome_completo")	
	//List<Paciente> filtrarPacientes (@Param("local") String local, @Param("nome") String nome);
	
	//@Query (value="SELECT p FROM Paciente p WHERE p.nome_completo like %:nome% AND p.id IN (SELECT les.id FROM Lesao les WHERE les.local_atendimento like %:local%) ORDER BY p.nome_completo", nativeQuery = true)
	//@Query (value="SELECT * FROM paciente WHERE paciente.nome_completo like %?1% AND paciente.id IN (SELECT lesao.id FROM lesao WHERE lesao.local_atendimento like %?2%) ORDER BY paciente.nome_completo", nativeQuery = true)
	//@Query (value="SELECT * FROM paciente WHERE paciente.id IN (SELECT lesao.paciente_id FROM lesao WHERE lesao.local_atendimento like %?1%)", nativeQuery=true)
	@Query(value="SELECT * FROM paciente WHERE (paciente.id IN (SELECT lesao.paciente_id FROM lesao WHERE (lesao.local_atendimento LIKE %?1% AND lesao.diagnostico_clinico LIKE %?3% ))) AND paciente.nome_completo LIKE %?2% ORDER BY paciente.nome_completo", nativeQuery=true)
	List<Paciente> filtrarPacientes (String local, String Nome, String diag);	
	
	
	//@Query("select p from Paciente p where p.local_atendimento like %:local% and p.nome_completo like %:nome% and p.data_atendimento between :dataInicio and :dataFim order by p.nome_completo")	
	//List<Paciente> filtrarPacientesComData (@Param("local") String local, @Param("nome") String nome, @Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim);	
	
	
	@Query (value="SELECT * FROM paciente WHERE (paciente.id IN (SELECT lesao.paciente_id FROM lesao WHERE lesao.local_atendimento like %?1% AND (lesao.data_atendimento BETWEEN ?3 AND ?4) AND (lesao.diagnostico_clinico LIKE %?5% ))) AND paciente.nome_completo LIKE %?2% ORDER BY paciente.nome_completo", nativeQuery=true)
	List<Paciente> filtrarPacientesComData (String local, String nome, Date dataInicio, Date dataFim, String diag);
	
	@Query (value="SELECT * FROM paciente WHERE paciente.local_atendimento = ?1", nativeQuery=true)
	List<Paciente> filtrarPorCidade (String local);

}
  
