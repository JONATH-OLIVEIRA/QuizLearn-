package com.conviteplus.conviteplus;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.conviteplus.conviteplus.dto.LoginAnfitriaoDTO;
import com.conviteplus.conviteplus.model.Anfitriao;

import org.junit.jupiter.api.Test; // Certifique-se de importar o JUnit 5
import static org.junit.jupiter.api.Assertions.assertTrue; // Importar a asserção

public class TestValidacaoLogin {
    
  
    @Test
    public void testSenhaBCrypt() {
        // Hash que temos no banco para a senha "010203"
        String hashNoBanco = "$2a$10$E7175zQAQ54wx1dW0vjjyu2omD9rRj9JS44gqvXPi3/LdTtqHmoti";

        // Senha digitada pelo usuário
        String senhaRecebida = "010203";

        // Configurar o validador de senhas BCrypt
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Validar a senha contra o hash
        boolean senhaValida = passwordEncoder.matches(senhaRecebida, hashNoBanco);

        // Exibir no console para debugging
        System.out.println("Senha válida? " + senhaValida);

        // Garantir que a senha é válida
        assertTrue(senhaValida, "A senha deveria ser válida, mas a validação falhou.");
    }
}




