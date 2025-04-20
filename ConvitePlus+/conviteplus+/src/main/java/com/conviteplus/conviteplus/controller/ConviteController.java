package com.conviteplus.conviteplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConviteController {

    @GetMapping("/convite/{codigoConvite}")
    public String exibirPaginaConvite() {
    	
    	  System.out.println("Entrei aqui");
        // Retorna a página de convite (HTML) que está na pasta 'static/convite'
        return "convite/index"; // Aqui, "convite/index" é o caminho relativo para o arquivo HTML
       
    }
  
}