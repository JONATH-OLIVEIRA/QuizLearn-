package com.conviteplus.conviteplus.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventoRequest {

    private String nomeEvento;
    private String enderecoEvento;
    private int quantConvidados;
    private Long anfitriaoId;
    private List<Long> presentesIds;
    private LocalDate dataEvento;
    private LocalTime horaEvento;
    private String imagemUrl;
    private List<PresenteDTO> sugestoesPresentes; // Adicionado ao construtor

    // Construtor padrão
    public EventoRequest() {}

    // Construtor completo com sugestoesPresentes
    public EventoRequest(String nomeEvento, String enderecoEvento, int quantConvidados, Long anfitriaoId, 
                         List<Long> presentesIds, LocalDate dataEvento, LocalTime horaEvento, 
                         String imagemUrl, List<PresenteDTO> sugestoesPresentes) {
        this.nomeEvento = nomeEvento;
        this.enderecoEvento = enderecoEvento;
        this.quantConvidados = quantConvidados;
        this.anfitriaoId = anfitriaoId;
        this.presentesIds = presentesIds;
        this.dataEvento = dataEvento;
        this.horaEvento = horaEvento;
        this.imagemUrl = imagemUrl;
        this.sugestoesPresentes = sugestoesPresentes; // Corrigido
    }

    // Getters e Setters
    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getEnderecoEvento() {
        return enderecoEvento;
    }

    public void setEnderecoEvento(String enderecoEvento) {
        this.enderecoEvento = enderecoEvento;
    }

    public int getQuantConvidados() {
        return quantConvidados;
    }

    public void setQuantConvidados(int quantConvidados) {
        this.quantConvidados = quantConvidados;
    }

    public Long getAnfitriaoId() {
        return anfitriaoId;
    }

    public void setAnfitriaoId(Long anfitriaoId) {
        this.anfitriaoId = anfitriaoId;
    }

    public List<Long> getPresentesIds() {
        return presentesIds;
    }

    public void setPresentesIds(List<Long> presentesIds) {
        this.presentesIds = presentesIds;
    }

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDate dataEvento) {
        this.dataEvento = dataEvento;
    }

    public LocalTime getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(LocalTime horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public List<PresenteDTO> getSugestoesPresentes() {
        return sugestoesPresentes;
    }

    public void setSugestoesPresentes(List<PresenteDTO> sugestoesPresentes) {
        this.sugestoesPresentes = sugestoesPresentes;
    }
}
