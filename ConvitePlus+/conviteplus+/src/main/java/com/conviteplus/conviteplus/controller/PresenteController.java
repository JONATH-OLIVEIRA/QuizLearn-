package com.conviteplus.conviteplus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.conviteplus.conviteplus.dto.PresenteDTO;
import com.conviteplus.conviteplus.model.Evento;
import com.conviteplus.conviteplus.model.Presente;
import com.conviteplus.conviteplus.service.EventoService;
import com.conviteplus.conviteplus.service.PresenteService;

@RestController
public class PresenteController {

    private final PresenteService presenteService;
    private final EventoService eventoService;

    @Autowired
    public PresenteController(PresenteService presenteService, EventoService eventoService) {
        this.presenteService = presenteService;
        this.eventoService = eventoService;
    }

    @PostMapping("/api/presentes")
    public ResponseEntity<Presente> criarPresente(@RequestBody PresenteDTO presenteRequest) {
        try {
            // Buscar o evento pelo ID (modelo original)
            Evento evento = eventoService.buscarEventoOriginalPorId(presenteRequest.getEventoId());
            if (evento == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Evento não encontrado
            }

            // Criar o presente e associar ao evento
            Presente presente = new Presente(presenteRequest.getNome(), evento);

            // Salvar o presente
            Presente novoPresente = presenteService.salvar(presente);

            return new ResponseEntity<>(novoPresente, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Retorna erro genérico no caso de falha
        }
    }

    
    @GetMapping("/api/eventos/{eventoId}/presentes")
    public ResponseEntity<List<Presente>> listarPresentes(@PathVariable Long eventoId) {
        try {
            // Buscar o evento pelo ID (modelo original)
            Evento evento = eventoService.buscarEventoOriginalPorId(eventoId);
            if (evento == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Evento não encontrado
            }

            // Obter os presentes associados ao evento
            List<Presente> presentes = evento.getSugestoesPresentes();

            return ResponseEntity.ok(presentes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna erro 500 em caso de falha
        }
    }

    
    @DeleteMapping("/api/presentes/{presenteId}")
    public ResponseEntity<Void> excluirPresente(@PathVariable Long presenteId) {
        Presente presente = presenteService.buscarPorId(presenteId);
        if (presente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        presenteService.excluir(presenteId); // Deletar o presente
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    @PutMapping("/api/presentes/{presenteId}")
    public ResponseEntity<Presente> editarPresente(@PathVariable Long presenteId, @RequestBody PresenteDTO presenteDTO) {
        Presente presente = presenteService.buscarPorId(presenteId);
        if (presente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Atualiza o nome do presente
        presente.setNome(presenteDTO.getNome());
        Presente presenteAtualizado = presenteService.salvar(presente);
        
        return ResponseEntity.ok(presenteAtualizado);
    }
}
