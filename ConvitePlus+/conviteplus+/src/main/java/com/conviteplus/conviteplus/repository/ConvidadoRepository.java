package com.conviteplus.conviteplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.conviteplus.conviteplus.model.Convidado;

@Repository
public interface ConvidadoRepository extends JpaRepository<Convidado, Long>{

	Convidado findByTelefoneAndEventoId(String telefone, Long eventoId);
	 List<Convidado> findByEventoId(Long eventoId);
}