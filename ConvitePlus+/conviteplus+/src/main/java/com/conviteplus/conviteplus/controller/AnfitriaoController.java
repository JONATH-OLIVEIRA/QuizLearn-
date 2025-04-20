package com.conviteplus.conviteplus.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.conviteplus.conviteplus.dto.AnfitriaoComEventosDTO;
import com.conviteplus.conviteplus.dto.AtualizarAnfitriaoDTO;
import com.conviteplus.conviteplus.dto.AtualizarSenhaDTO;
import com.conviteplus.conviteplus.model.Anfitriao;
import com.conviteplus.conviteplus.model.Evento;
import com.conviteplus.conviteplus.service.AnfitriaoService;
import com.conviteplus.conviteplus.service.EventoService;

@Controller
@RequestMapping("/api/anfitrioes")
public class AnfitriaoController {
	private final AnfitriaoService anfitriaoService;
	private final EventoService eventoService;
	
	private static final Logger logger = LoggerFactory.getLogger(EventoController.class);

	@Autowired
	public AnfitriaoController(AnfitriaoService anfitriaoService, EventoService eventoService) {
		this.anfitriaoService = anfitriaoService;
		this.eventoService = eventoService;
	}

	@PostMapping
	public ResponseEntity<Anfitriao> criarAnfitriao(@RequestBody Anfitriao anfitriao) {
		// Retorna status 201 CREATED ao criar o novo anfitrião
		Anfitriao novoAnfitriao = anfitriaoService.salvar(anfitriao);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoAnfitriao);
	}

	@GetMapping
	public ResponseEntity<List<Anfitriao>> listarAnfitrioes() {
		// Retorna status 200 OK com a lista de anfitriões
		return ResponseEntity.ok(anfitriaoService.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Anfitriao> buscarAnfitriaoPorId(@PathVariable Long id) {
		// Retorna 404 NOT FOUND caso o anfitrião não seja encontrado
		Anfitriao anfitriao = anfitriaoService.buscarPorId(id);
		return anfitriao != null ? ResponseEntity.ok(anfitriao) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<Anfitriao> buscarAnfitriaoPorEmail(@PathVariable String email) {
		// Retorna 404 NOT FOUND caso o email não seja encontrado
		Anfitriao anfitriao = anfitriaoService.buscarPorEmail(email);
		return anfitriao != null ? ResponseEntity.ok(anfitriao) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/comEventosEPresentes")
	public ResponseEntity<List<AnfitriaoComEventosDTO>> listarAnfitrioesComEventosEPresentes() {
		// Retorna status 200 OK com os anfitriões e seus eventos/presentes
		return ResponseEntity.ok(anfitriaoService.listarAnfitrioesComEventosEPresentes());
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
		String email = loginData.get("email");
		String senha = loginData.get("senha");

		if (email == null || senha == null) {
			// Retorna 400 BAD REQUEST caso os dados estejam incompletos
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message", "Email e senha são obrigatórios"));
		}

		Anfitriao anfitriao = anfitriaoService.login(email, senha);
		if (anfitriao == null) {
			// Retorna 401 UNAUTHORIZED caso o login falhe
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Usuário ou senha inválidos"));
		}

		// Inclui dados do anfitrião na resposta junto com os eventos
		return ResponseEntity.ok(Map.of("message", "Login realizado com sucesso", "anfitriao",
				Map.of("id", anfitriao.getId(), "nome", anfitriao.getNome(), "email", anfitriao.getEmail()), "eventos",
				anfitriao.getEventos()));
	}

	@GetMapping("/{id}/eventos")
	public ResponseEntity<List<Evento>> listarEventosPorAnfitriao(@PathVariable Long id) {
		// Retorna 404 NOT FOUND caso o anfitrião não seja encontrado
		Anfitriao anfitriao = anfitriaoService.buscarPorId(id);
		if (anfitriao == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		// Retorna 200 OK com os eventos do anfitrião
		return ResponseEntity.ok(anfitriao.getEventos());
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarAnfitriao(@PathVariable Long id, @RequestBody AtualizarAnfitriaoDTO dto) {
		try {
			return ResponseEntity.ok(anfitriaoService.atualizarAnfitriao(id, dto));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}/senha")
	public ResponseEntity<?> atualizarSenha(@PathVariable Long id, @RequestBody AtualizarSenhaDTO dto) {
		try {
			// Chama o serviço para atualizar a senha
			anfitriaoService.atualizarSenha(id, dto);
			return ResponseEntity.ok("Senha atualizada com sucesso!");
		} catch (RuntimeException e) {
			// Retorna mensagem de erro amigável com BAD_REQUEST
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarAnfitriao(@PathVariable Long id) {
	    logger.info("Iniciando exclusão do anfitrião com ID: {}", id);
	    boolean removido = anfitriaoService.deletar(id);
	    if (removido) {
	        logger.info("Anfitrião excluído com sucesso: ID {}", id);
	        return ResponseEntity.ok(Map.of("message", "Conta excluída com sucesso")); // Mensagem no corpo da resposta
	    } else {
	        logger.warn("Anfitrião não encontrado para exclusão: ID {}", id);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Anfitrião não encontrado"));
	    }
	}



}
