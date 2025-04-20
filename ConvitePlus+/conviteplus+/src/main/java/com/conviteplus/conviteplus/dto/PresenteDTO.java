package com.conviteplus.conviteplus.dto;

import com.conviteplus.conviteplus.model.Presente;

public class PresenteDTO {
    private Long id;
    private String nome;
    private Long eventoId; // Adicione este campo

    // Construtor padrão
    public PresenteDTO() {}

    // Construtor que aceita um objeto Presente
    public PresenteDTO(Presente presente) {
        this.id = presente.getId();
        this.nome = presente.getNome();

        // Preenche o eventoId a partir do objeto Presente
        if (presente.getEvento() != null) {
            this.eventoId = presente.getEvento().getId(); // Obtém o ID do evento associado
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
}
