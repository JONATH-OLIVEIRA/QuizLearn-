package com.conviteplus.conviteplus.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.conviteplus.conviteplus.model.Evento;
import com.conviteplus.conviteplus.model.Presente;

public class EventoDTO {
    private Long id;                          // ID do evento
    private String nomeEvento;                // Nome do evento
    private String enderecoEvento;            // Endereço do evento
    private int quantConvidados;              // Quantidade de convidados
    private Long anfitriaoId;                 // ID do anfitrião associado
    private List<Long> presentesIds;          // IDs dos presentes
    private LocalDate dataEvento;             // Data do evento
    private LocalTime horaEvento;             // Hora do evento
    private String imagemUrl;                 // URL da imagem do evento
    private List<PresenteDTO> sugestoesPresentes; // Detalhes dos presentes
    private List<ConvidadoDTO> convidados;    // Lista completa de convidados

    // Construtor padrão
    public EventoDTO() {}

    // Construtor que aceita um objeto Evento (completo)
    public EventoDTO(Evento evento) {
        this.id = evento.getId();
        this.nomeEvento = evento.getNomeEvento();
        this.enderecoEvento = evento.getEnderecoEvento();
        this.quantConvidados = evento.getQuantConvidados();
        this.anfitriaoId = evento.getAnfitriao().getId();
        this.presentesIds = evento.getSugestoesPresentes().stream()
                .map(Presente::getId) // Presume que cada Presente tem um ID
                .toList();
        this.dataEvento = evento.getDataEvento();
        this.horaEvento = evento.getHoraEvento();
        this.imagemUrl = evento.getImagemUrl();
        this.sugestoesPresentes = evento.getSugestoesPresentes().stream()
                .map(PresenteDTO::new) // Presume que PresenteDTO aceita Presente
                .collect(Collectors.toList());
        this.convidados = evento.getConvidados().stream()
                .map(ConvidadoDTO::new) // Presume que ConvidadoDTO aceita Convidado
                .collect(Collectors.toList());
    }

    // Construtor manual (para casos específicos)
    public EventoDTO(String nomeEvento, String enderecoEvento, int quantConvidados, Long anfitriaoId,
                     List<Long> presentesIds, LocalDate dataEvento, LocalTime horaEvento,
                     String imagemUrl, List<PresenteDTO> sugestoesPresentes, List<ConvidadoDTO> convidados) {
        this.nomeEvento = nomeEvento;
        this.enderecoEvento = enderecoEvento;
        this.quantConvidados = quantConvidados;
        this.anfitriaoId = anfitriaoId;
        this.presentesIds = presentesIds;
        this.dataEvento = dataEvento;
        this.horaEvento = horaEvento;
        this.imagemUrl = imagemUrl;
        this.sugestoesPresentes = sugestoesPresentes;
        this.convidados = convidados;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<ConvidadoDTO> getConvidados() {
        return convidados;
    }

    public void setConvidados(List<ConvidadoDTO> convidados) {
        this.convidados = convidados;
    }
}
