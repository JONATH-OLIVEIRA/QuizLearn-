package com.conviteplus.conviteplus.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conviteplus.conviteplus.dto.EventoDTO;
import com.conviteplus.conviteplus.model.Evento;
import com.conviteplus.conviteplus.repository.EventoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventoService {

	private final EventoRepository eventoRepository;

	@Autowired
	public EventoService(EventoRepository eventoRepository) {
		this.eventoRepository = eventoRepository;
	}

	// Salvar evento, com validações centralizadas
	public Evento salvar(Evento evento) {
		if (evento.getSugestoesPresentes() == null) {
			evento.setSugestoesPresentes(new ArrayList<>()); // Garante que não seja nulo
		}

		if (evento.getAnfitriao() == null) {
			throw new RuntimeException("Anfitrião não encontrado.");
		}

		return eventoRepository.save(evento);
	}

	public Evento buscarEventoOriginalPorId(Long id) {
		return eventoRepository.findById(id).orElseThrow(() -> new RuntimeException("Evento não encontrado"));
	}

	private void validarAnfitriao(Evento evento) {
		if (evento.getAnfitriao() == null) {
			throw new RuntimeException("Anfitrião não encontrado.");
		}
	}

	private void validarNomeEvento(Evento evento) {
		List<Evento> eventosDoAnfitriao = eventoRepository.findByAnfitriao_Id(evento.getAnfitriao().getId());
		boolean nomeJaExiste = eventosDoAnfitriao.stream().anyMatch(
				e -> e.getNomeEvento().equalsIgnoreCase(evento.getNomeEvento()) && !e.getId().equals(evento.getId()));

		if (nomeJaExiste) {
			throw new RuntimeException("Evento já cadastrado com esse nome para este anfitrião.");
		}
	}

	// Buscar evento por ID com tratamento de erro
	public EventoDTO buscarPorId(Long id) {
		Evento evento = eventoRepository.findById(id).orElseThrow(() -> new RuntimeException("Evento não encontrado"));

		// Mapear o Evento para EventoDTO
		return new EventoDTO(evento);
	}

	// Deletar evento
	public boolean deletar(Long id) {
		if (eventoRepository.existsById(id)) {
			eventoRepository.deleteById(id); // Deleta o evento do banco
			return true;
		}
		return false; // Retorna falso se o evento não for encontrado
	}

	// Listar eventos por anfitrião e retornar como DTOs
	public List<EventoDTO> listarPorAnfitriao(Long anfitriaoId) {
		List<Evento> eventos = eventoRepository.findByAnfitriao_Id(anfitriaoId);
		return eventos.stream().map(EventoDTO::new) // Convertendo Evento para EventoDTO
				.toList();
	}

	// Gerar código de convite único
	public String gerarCodigoConvite() {
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder codigo;
		Random random = new Random();

		do {
			codigo = new StringBuilder();
			for (int i = 0; i < 8; i++) {
				codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
			}
		} while (eventoRepository.findByCodigoConvite(codigo.toString()) != null); // Garante unicidade

		return codigo.toString();
	}

	// Buscar evento pelo código do convite
	public Evento buscarPorCodigoConvite(String codigoConvite) {
		Evento evento = eventoRepository.findByCodigoConvite(codigoConvite);
		if (evento == null) {
			throw new EntityNotFoundException("Evento com código de convite " + codigoConvite + " não encontrado");
		}
		return evento;
	}
}
