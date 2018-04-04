package ufes.pad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufes.pad.model.ImagemGeral;

@Repository
public interface ImagemGeralRepository extends JpaRepository<ImagemGeral, Long> {
	
}

