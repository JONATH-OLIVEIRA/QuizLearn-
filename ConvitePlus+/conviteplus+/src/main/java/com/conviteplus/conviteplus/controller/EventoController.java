package com.conviteplus.conviteplus.controller;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conviteplus.conviteplus.dto.EventoDTO;
import com.conviteplus.conviteplus.dto.EventoRequest;
import com.conviteplus.conviteplus.dto.PresenteDTO;
import com.conviteplus.conviteplus.model.Anfitriao;
import com.conviteplus.conviteplus.model.Convidado;
import com.conviteplus.conviteplus.model.Evento;
import com.conviteplus.conviteplus.service.AnfitriaoService;
import com.conviteplus.conviteplus.service.ConvidadoService;
import com.conviteplus.conviteplus.service.EventoService;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

	private final EventoService eventoService;
	private final AnfitriaoService anfitriaoService;
	private final ConvidadoService convidadoService;

	private static final Logger logger = LoggerFactory.getLogger(EventoController.class);

	@Autowired
	public EventoController(EventoService eventoService, AnfitriaoService anfitriaoService,
			ConvidadoService convidadoService) {
		this.eventoService = eventoService;
		this.anfitriaoService = anfitriaoService;
		this.convidadoService = convidadoService;
	}

	// Criar evento
	@PostMapping("/{anfitriaoId}")
	public ResponseEntity<?> criarEvento(@PathVariable Long anfitriaoId, @RequestBody EventoRequest eventoRequest) {
		logger.info("Iniciando criação do evento para anfitrião com ID: {}", anfitriaoId);

		try {
			// Buscar o anfitrião
			Anfitriao anfitriao = anfitriaoService.buscarPorId(anfitriaoId);
			if (anfitriao == null) {
				logger.warn("Anfitrião não encontrado com o ID: {}", anfitriaoId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Anfitrião não encontrado"));
			}

			// Criar o novo evento
			Evento novoEvento = new Evento();
			novoEvento.setNomeEvento(eventoRequest.getNomeEvento());
			novoEvento.setEnderecoEvento(eventoRequest.getEnderecoEvento());
			novoEvento.setQuantConvidados(eventoRequest.getQuantConvidados());
			novoEvento.setAnfitriao(anfitriao);
			novoEvento.setDataEvento(eventoRequest.getDataEvento());
			novoEvento.setHoraEvento(eventoRequest.getHoraEvento());
			novoEvento.setImagemUrl(eventoRequest.getImagemUrl());
			novoEvento.setSugestoesPresentes(new ArrayList<>()); // Inicializa como lista vazia

			String codigoConvite = eventoService.gerarCodigoConvite();
			novoEvento.setCodigoConvite(codigoConvite);

			// Salvar evento
			Evento eventoSalvo = eventoService.salvar(novoEvento);
			logger.info("Evento criado com sucesso: {}", eventoSalvo);

			// Retornar como DTO
			return ResponseEntity.status(HttpStatus.CREATED).body(new EventoDTO(eventoSalvo));
		} catch (Exception e) {
			logger.error("Erro ao criar evento: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "Erro ao criar evento"));
		}
	}

	// Buscar evento por ID

	@GetMapping("/{id}")
    public ResponseEntity<?> buscarEventoPorId(@PathVariable Long id) {
        try {
            // Retornar o DTO em vez do modelo
            EventoDTO eventoDTO = eventoService.buscarPorId(id);
            return ResponseEntity.ok(eventoDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao buscar evento"));
        }
    }
	

	// Atualizar evento
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarEvento(@PathVariable Long id, @RequestBody EventoRequest eventoRequest) {
	    if (eventoRequest.getImagemUrl() != null && eventoRequest.getImagemUrl().length() > 1024) {
	        throw new IllegalArgumentException("O URL da imagem excede o limite permitido de 1024 caracteres.");
	    }

	    try {
	        // Buscar o evento existente no banco de dados
	        Evento eventoExistente = eventoService.buscarEventoOriginalPorId(id); // Retorna o modelo Evento
	        if (eventoExistente == null) {
	            logger.warn("Evento não encontrado com ID: {}", id);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Evento não encontrado"));
	        }

	        // Buscar o anfitrião usando o anfitriaoId do EventoRequest
	        Anfitriao anfitriao = anfitriaoService.buscarPorId(eventoRequest.getAnfitriaoId());
	        if (anfitriao == null) {
	            logger.warn("Anfitrião não encontrado com ID: {}", eventoRequest.getAnfitriaoId());
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Anfitrião não encontrado"));
	        }

	        // Atualizar os campos do modelo Evento
	        eventoExistente.setNomeEvento(eventoRequest.getNomeEvento());
	        eventoExistente.setEnderecoEvento(eventoRequest.getEnderecoEvento());
	        eventoExistente.setQuantConvidados(eventoRequest.getQuantConvidados());
	        eventoExistente.setAnfitriao(anfitriao);
	        eventoExistente.setDataEvento(eventoRequest.getDataEvento());
	        eventoExistente.setHoraEvento(eventoRequest.getHoraEvento());

	        // Atualizar a imagem do evento, se fornecida
	        if (eventoRequest.getImagemUrl() != null && !eventoRequest.getImagemUrl().isEmpty()) {
	            eventoExistente.setImagemUrl(eventoRequest.getImagemUrl());
	        }

	        // Salvar o evento atualizado no banco
	        Evento eventoAtualizado = eventoService.salvar(eventoExistente);

	        logger.info("URL da imagem recebida: {}", eventoRequest.getImagemUrl());
	        logger.info("Tamanho da URL: {}", eventoRequest.getImagemUrl().length());
	        logger.info("Evento atualizado com sucesso: {}", eventoAtualizado);

	        // Retornar o evento atualizado como DTO
	        return ResponseEntity.ok(new EventoDTO(eventoAtualizado)); // Converte o modelo para DTO na resposta
	    } catch (Exception e) {
	        logger.error("Erro ao atualizar evento: {}", e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("message", "Erro ao atualizar evento"));
	    }
	}


	// Deletar evento
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarEvento(@PathVariable Long id) {
		logger.info("Iniciando exclusão do evento com ID: {}", id);
		boolean removido = eventoService.deletar(id);
		if (removido) {
			logger.info("Evento excluído com sucesso: ID {}", id);
			return ResponseEntity.noContent().build();
		} else {
			logger.warn("Evento não encontrado para exclusão: ID {}", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Evento não encontrado"));
		}
	}

	// Aceitar convite
	@PostMapping("/accept/{codigoConvite}")
	public ResponseEntity<?> aceitarConvite(@PathVariable String codigoConvite, @RequestBody Convidado convidado) {
		logger.info("Aceitando convite com código: {}", codigoConvite);
		try {
			Evento evento = eventoService.buscarPorCodigoConvite(codigoConvite);
			if (evento == null) {
				logger.warn("Evento não encontrado para o código: {}", codigoConvite);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Evento não encontrado"));
			}

			convidado.setEvento(evento);
			convidadoService.salvar(convidado);

			Map<String, String> response = new HashMap<>();
			response.put("message", "Convite aceito com sucesso!");
			response.put("eventoId", evento.getId().toString());
			logger.info("Convite aceito com sucesso para o evento: {}", evento);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			logger.error("Erro ao aceitar convite: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "Erro ao aceitar convite"));
		}
	}

	// Listar eventos por anfitrião
	@GetMapping("/{anfitriaoId}/eventos")
	public ResponseEntity<?> listarEventos(@PathVariable Long anfitriaoId) {
		logger.info("Listando eventos para o anfitrião com ID: {}", anfitriaoId);
		try {
			List<EventoDTO> eventos = eventoService.listarPorAnfitriao(anfitriaoId);
			if (eventos.isEmpty()) {
				logger.warn("Nenhum evento encontrado para o anfitrião com ID: {}", anfitriaoId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Nenhum evento encontrado"));
			}
			return ResponseEntity.ok(eventos);
		} catch (Exception e) {
			logger.error("Erro ao listar eventos: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "Erro ao listar eventos"));
		}
	}
}
