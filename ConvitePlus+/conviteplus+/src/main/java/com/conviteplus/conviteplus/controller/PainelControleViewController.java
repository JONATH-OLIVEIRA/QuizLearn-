package com.conviteplus.conviteplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PainelControleViewController {

    @GetMapping("/painelControle")
    public String carregarPainelControle() {
        return "painelControle"; // Carrega PainelControle.html da pasta "src/main/resources/templates/"
    }
}