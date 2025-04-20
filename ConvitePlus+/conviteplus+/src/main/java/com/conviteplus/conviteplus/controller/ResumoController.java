package com.conviteplus.conviteplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.conviteplus.conviteplus.model.Evento;
import com.conviteplus.conviteplus.service.EventoService;
import com.conviteplus.conviteplus.service.PresenteService;

@Controller
public class ResumoController {

    private final EventoService eventoService;
    private final PresenteService presenteService;

    @Autowired
    public ResumoController(EventoService eventoService, PresenteService presenteService) {
        this.eventoService = eventoService;
        this.presenteService = presenteService;
    }

   
}