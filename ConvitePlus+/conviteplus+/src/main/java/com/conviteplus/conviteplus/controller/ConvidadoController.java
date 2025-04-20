package com.conviteplus.conviteplus.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.conviteplus.conviteplus.model.Convidado;
import com.conviteplus.conviteplus.service.ConvidadoService;

@RestController
@RequestMapping("/convidados")
public class ConvidadoController {

	private final ConvidadoService convidadoService;

	@Autowired
	public ConvidadoController(ConvidadoService convidadoService) {
		this.convidadoService = convidadoService;
	}

	// ✅ Atualizar confirmação (confirmação ou recusa do convite)
	@PutMapping("/{id}/confirmacao")
	public ResponseEntity<String> atualizarConfirmacao(@PathVariable Long id,
			@RequestBody Map<String, Boolean> payload) {

		Boolean resposta = payload.get("resposta");
		String resultado = convidadoService.atualizarConfirmacao(id, resposta);

		if (resultado == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(resultado);
	}

	// ✅ Cadastrar novo convidado
	@PostMapping
	public ResponseEntity<Convidado> salvarConvidado(@RequestBody Convidado convidado) {
		Convidado salvo = convidadoService.salvar(convidado);
		return ResponseEntity.ok(salvo);
	}

	// ✅ Buscar convidado por ID
	@GetMapping("/{id}")
	public ResponseEntity<Convidado> buscarPorId(@PathVariable Long id) {
		Convidado convidado = convidadoService.buscarPorId(id);
		if (convidado == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(convidado);
	}

	// ✅ Deletar convidado
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		boolean deletado = convidadoService.deletar(id);
		if (!deletado) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/acesso")
	public ResponseEntity<Convidado> buscarPorTelefoneEEvento(@RequestParam String telefone,
			@RequestParam Long eventoId) {

		Convidado convidado = convidadoService.buscarPorTelefoneEEvento(telefone, eventoId);

		if (convidado == null) {
			System.out.println("❌ Convidado não encontrado.");
			return ResponseEntity.status(404).build();
		}

		System.out.println(
				"🟡 Convidado encontrado: " + convidado.getNome() + ", aceitou: " + convidado.getAceitouConvite());

		if (Boolean.FALSE.equals(convidado.getAceitouConvite())) {
			System.out.println("🔒 Convidado recusou o convite. Acesso negado.");
			return ResponseEntity.status(403).build();
		}

		System.out.println("✅ Acesso permitido.");
		return ResponseEntity.ok(convidado);
	}

	@GetMapping("/evento/{eventoId}")
	public ResponseEntity<List<Convidado>> listarConvidadosPorEvento(@PathVariable Long eventoId) {
		List<Convidado> convidados = convidadoService.listarPorEventoId(eventoId);
		return ResponseEntity.ok(convidados);
	}

}
