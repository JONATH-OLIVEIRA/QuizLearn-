package com.conviteplus.conviteplus.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.conviteplus.conviteplus.model.Anfitriao;

public class AnfitriaoDTO {
    private Long id;
    private String nome;
    private String email;
    private List<EventoDTO> eventos; // Lista de EventoDTO

    public AnfitriaoDTO(Anfitriao anfitriao) {
        this.id = anfitriao.getId();
        this.nome = anfitriao.getNome();
        this.email = anfitriao.getEmail();

        // Mapeamento dos eventos do Anfitriao para EventoDTO
        this.eventos = anfitriao.getEventos().stream()
            .map(EventoDTO::new)
            .collect(Collectors.toList());
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<EventoDTO> getEventos() {
        return eventos;
    }

    public void setEventos(List<EventoDTO> eventos) {
        this.eventos = eventos;
    }
}
