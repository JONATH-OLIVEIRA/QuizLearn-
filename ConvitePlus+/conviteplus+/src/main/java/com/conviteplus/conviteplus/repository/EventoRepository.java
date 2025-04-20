package com.conviteplus.conviteplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.conviteplus.conviteplus.model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
	List<Evento> findByAnfitriao_Id(Long anfitriaoId);

	Evento findByCodigoConvite(String codigoConvite);

	@Query("SELECT e FROM Evento e LEFT JOIN FETCH e.convidados WHERE e.id = :eventoId")
	Evento buscarEventoComConvidados(@Param("eventoId") Long eventoId);
	
	// Método usando a nomenclatura do Spring Data JPA
    Evento findByAnfitriao_IdAndNomeEventoIgnoreCase(Long anfitriaoId, String nomeEvento);

}