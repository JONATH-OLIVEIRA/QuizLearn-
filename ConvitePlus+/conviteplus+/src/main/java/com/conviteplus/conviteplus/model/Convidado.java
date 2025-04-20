package com.conviteplus.conviteplus.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "convidados")
public class Convidado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String telefone;
    private Boolean aceitouConvite;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    @JsonBackReference // resolve recursão infinita
    private Evento evento;

    @Version
    private Long version; // Controle de versão para otimistic locking

    public Convidado() {
    }

    public Convidado(String nome, String telefone, Boolean aceitouConvite, Evento evento) {
        this.nome = nome;
        this.telefone = telefone;
        this.aceitouConvite = aceitouConvite;
        this.evento = evento;
    }

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

    public Boolean isAceitouConvite() {
        return aceitouConvite;
    }
    
    public Boolean getAceitouConvite() {
        return aceitouConvite;
    }

    public void setAceitouConvite(Boolean aceitouConvite) {
        this.aceitouConvite = aceitouConvite;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Convidado [id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", aceitouConvite=" + aceitouConvite + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Convidado other = (Convidado) obj;
        return Objects.equals(id, other.id);
    }
}
