package ufes.pad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ufes.pad.model.Imagem;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {

	
	String inserirQuery = "INSERT INTO imagem(path,lesao_id) VALUES (:path, :les_id)";	
	@Query(nativeQuery=true, value=inserirQuery)
	@Transactional
	@Modifying
	void inserir (@Param("les_id") Long les_id,
			@Param("path") String path
			);	
	
	String atualizarQuery = "UPDATE imagem SET path=:path, WHERE id = :img_id";
	@Query(nativeQuery=true, value=atualizarQuery)
	@Transactional
	@Modifying
	void atualizar (@Param("img_id") Long img_id,
			@Param("path") String path
			);	
	
	
}

