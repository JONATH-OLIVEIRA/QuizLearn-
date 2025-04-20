package com.conviteplus.conviteplus.dto;

import java.util.List;

public class AnfitriaoComEventosDTO {
    
    private Long anfitriaoId;
    private String nome;
    private List<EventoDTO> eventos;

    // Getters and setters

    public static class EventoDTO {
        private Long eventoId;
        private String nomeEvento;
        private String enderecoEvento;
        private int quantConvidados;
        private List<PresenteDTO> presentes;

        public Long getEventoId() {
            return eventoId;
        }
        public void setEventoId(Long eventoId) {
            this.eventoId = eventoId;
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
        public List<PresenteDTO> getPresentes() {
            return presentes;
        }
        public void setPresentes(List<PresenteDTO> presentes) {
            this.presentes = presentes;
        }
    }

    public static class PresenteDTO {
        private Long presenteId;
        private String nome;

        public Long getPresenteId() {
            return presenteId;
        }
        public void setPresenteId(Long presenteId) {
            this.presenteId = presenteId;
        }
        public String getNome() {
            return nome;
        }
        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    public Long getAnfitriaoId() {
        return anfitriaoId;
    }

    public void setAnfitriaoId(Long anfitriaoId) {
        this.anfitriaoId = anfitriaoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<EventoDTO> getEventos() {
        return eventos;
    }

    public void setEventos(List<EventoDTO> eventos) {
        this.eventos = eventos;
    }
}
