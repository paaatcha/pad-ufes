package ufes.pad.repository;

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
	@Query("select p from Paciente p where p.local_atendimento like %:local% and p.nome_completo like %:nome% order by p.nome_completo")	
	List<Paciente> filtrarPacientes (@Param("local") String local, @Param("nome") String nome);
	
	/*String atualizarQuery = "UPDATE paciente SET agrotoxico=:agrotoxico,"
			+ "agua_encanada=:agua_encanada, alergia=:alergia, anticoagulante=:anticoagulante,"
			+ "atv_principal=:atv_principal, bebida=:bebida, calca_cumprida=:calca_cumprida,"
			+ "cartao_sus=[value-9], chapeu=:chapeu, data_atendimento=:data_atendimento,"
			+ "`data_nascimento`=[value-12],`destrofia_solar`=[value-13],`diabetes`=[value-14],"
			+ "`endereco`=[value-15],`escolaridade`=[value-16],`estado_civil`=[value-17],"
			+ "`estado_nascimento`=[value-18],`exp_sol`=[value-19],`filtro_solar`=[value-20],"
			+ "`fumo`=[value-21],`hist_cancer`=[value-22],`hist_cancer_pele`=[value-23],"
			+ "`hora_exp_sol`=[value-24],`idade_inicio_atv`=[value-25],`local_atendimento`=[value-26],"
			+ "`local_nascimento`=[value-27],`manga_cumprida`=[value-28],`nome_completo`=[value-29],"
			+ "`nome_mae`=[value-30],`num_comodos`=[value-31],`num_pessoas_casa`=[value-32],"
			+ "`num_vezes_atendido`=[value-33],`obs`=[value-34],`origem_familiar_mae`=[value-35],"
			+ "`origem_familiar_pai`=[value-36],`pres_art_diastolica`=[value-37],"
			+ "`pres_art_sistolica`=[value-38],`prontuario`=[value-39],`rede_esgoto`=[value-40],"
			+ "`renda`=[value-41],`sexo`=[value-42],`tempo_endereco`=[value-43],`tipo_pele`=[value-44],"
			+ "`HAS`=[value-45] WHERE id=:id_pac";
	*/
}

