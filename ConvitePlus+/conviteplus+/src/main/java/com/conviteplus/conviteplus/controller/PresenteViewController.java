package com.conviteplus.conviteplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PresenteViewController {

    @GetMapping("/presentes")
    public String mostrarPaginaPresentes(@RequestParam Long eventoId, Model model) {
        model.addAttribute("eventoId", eventoId);
        return "presentes";  // Retorna a página HTML "presentes.html"
    }
}
