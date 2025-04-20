package com.conviteplus.conviteplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.conviteplus.conviteplus.model.Anfitriao;

@Repository
public interface AnfitriaoRepository extends JpaRepository<Anfitriao, Long>{

	 Anfitriao findByEmail(String email);
	 Anfitriao findByNomeAndEmail(String nome, String email);
	 
}
