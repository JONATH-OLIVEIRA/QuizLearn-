package com.conviteplus.conviteplus.dto;

import com.conviteplus.conviteplus.model.Convidado;

public class ConvidadoDTO {

	private Long id; // Identificação única do convidado
	private String nome; // Nome do convidado
	private String telefone; // Telefone do convidado para acesso
	private Boolean aceitouConvite; // Indica se o convidado aceitou o convite (true, false ou null)
	private Long eventoId; // ID do evento associado ao convidado

	// Construtor padrão
	public ConvidadoDTO() {
	}

	// Construtor que aceita um objeto do tipo Convidado
	public ConvidadoDTO(Convidado convidado) {
		this.id = convidado.getId();
		this.nome = convidado.getNome();
		this.telefone = convidado.getTelefone();
		this.aceitouConvite = convidado.getAceitouConvite();
		this.eventoId = convidado.getEvento().getId();
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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Boolean getAceitouConvite() {
		return aceitouConvite;
	}

	public void setAceitouConvite(Boolean aceitouConvite) {
		this.aceitouConvite = aceitouConvite;
	}

	public Long getEventoId() {
		return eventoId;
	}

	public void setEventoId(Long eventoId) {
		this.eventoId = eventoId;
	}
}
