package com.conviteplus.conviteplus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conviteplus.conviteplus.model.Presente;
import com.conviteplus.conviteplus.repository.PresenteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PresenteService {

    private final PresenteRepository sugestaoPresenteRepository;

    @Autowired
    public PresenteService(PresenteRepository sugestaoPresenteRepository) {
        this.sugestaoPresenteRepository = sugestaoPresenteRepository;
    }

    public Presente salvar(Presente sugestaoPresente) {
        return sugestaoPresenteRepository.save(sugestaoPresente);
    }

     public List<Presente> buscarPresentesPorEvento(Long eventoId) {
        return sugestaoPresenteRepository.findByEventoId(eventoId); // Ajuste conforme o nome do método do repositório
    }

     public Presente buscarPorId(Long id) {
    	    return sugestaoPresenteRepository.findById(id)
    	        .orElseThrow(() -> new EntityNotFoundException("Presente com ID " + id + " não encontrado"));
    	}

    public void excluir(Long id) {
        sugestaoPresenteRepository.deleteById(id);
    }
}
