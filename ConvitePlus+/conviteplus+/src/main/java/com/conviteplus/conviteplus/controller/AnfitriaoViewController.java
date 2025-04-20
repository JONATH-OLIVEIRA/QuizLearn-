package com.conviteplus.conviteplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/anfitriao")
public class AnfitriaoViewController {

    @GetMapping("/novo")
    public String mostrarCadastroAnfitriao() {
        return "anfitriao";  // Nome do arquivo HTML
    }
}