package com.conviteplus.conviteplus.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "eventos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Evento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nomeEvento;
	private String enderecoEvento;
	private int quantConvidados;
	private String codigoConvite;
	
	@Column(name = "imagem_url", length = 10244) // Define o tamanho máximo para 1024 caracteres
	private String imagemUrl;

	@JsonFormat(pattern = "yyyy-MM-dd") // Alinha com o padrão ISO
	@Column(name = "data_evento")
	private LocalDate dataEvento;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime horaEvento;

	@ManyToOne
	@JoinColumn(name = "anfitriao_id", nullable = false)
	@JsonIgnoreProperties("eventos")
	private Anfitriao anfitriao;

	@OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties("evento")
	private List<Presente> sugestoesPresentes = new ArrayList<>();

	@OneToMany(mappedBy = "evento")
	@JsonManagedReference
	private List<Convidado> convidados = new ArrayList<>();

	@Version
	private Long version;

	public Evento() {
	}

	

	public Evento(Long id, String nomeEvento, String enderecoEvento, int quantConvidados, String codigoConvite,
			String imagemUrl, LocalDate dataEvento, LocalTime horaEvento, Anfitriao anfitriao,
			List<Presente> sugestoesPresentes, List<Convidado> convidados, Long version) {
		super();
		this.id = id;
		this.nomeEvento = nomeEvento;
		this.enderecoEvento = enderecoEvento;
		this.quantConvidados = quantConvidados;
		this.codigoConvite = codigoConvite;
		this.imagemUrl = imagemUrl;
		this.dataEvento = dataEvento;
		this.horaEvento = horaEvento;
		this.anfitriao = anfitriao;
		this.sugestoesPresentes = sugestoesPresentes;
		this.convidados = convidados;
		this.version = version;
	}



	public List<Convidado> getConvidadosAceitos() {
	    return (convidados != null) ?
	        convidados.stream()
	            .filter(c -> Boolean.TRUE.equals(c.getAceitouConvite()))
	            .toList()
	        : List.of();
	}

	// Getters e Setters
	
	public List<Convidado> getConvidados() {
	    return convidados;
	}

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

	public Anfitriao getAnfitriao() {
		return anfitriao;
	}

	public void setAnfitriao(Anfitriao anfitriao) {
		this.anfitriao = anfitriao;
	}

	public List<Presente> getSugestoesPresentes() {
		return sugestoesPresentes;
	}

	public void setSugestoesPresentes(List<Presente> sugestoesPresentes) {
		this.sugestoesPresentes = sugestoesPresentes;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getCodigoConvite() {
		return codigoConvite;
	}

	public void setCodigoConvite(String codigoConvite) {
		this.codigoConvite = codigoConvite;
	}

	public String getImagemUrl() {
		return imagemUrl;
	}

	public void setImagemUrl(String imagemUrl) {
		this.imagemUrl = imagemUrl;
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

	@Override
	public String toString() {
		DateTimeFormatter dataFormatada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter horaFormatada = DateTimeFormatter.ofPattern("HH:mm");

		String data = (dataEvento != null) ? dataEvento.format(dataFormatada) : "sem data";
		String hora = (horaEvento != null) ? horaEvento.format(horaFormatada) : "sem hora";

		return "Evento [id=" + id +
			", nomeEvento=" + nomeEvento +
			", enderecoEvento=" + enderecoEvento +
			", dataEvento=" + data +
			", horaEvento=" + hora +
			", imagemUrl=" + imagemUrl +
			", quantConvidados=" + quantConvidados +
			", codigoConvite=" + codigoConvite +
			", anfitriao=" + (anfitriao != null ? anfitriao.getId() : null) +
			"]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		return Objects.equals(id, other.id);
	}
}
