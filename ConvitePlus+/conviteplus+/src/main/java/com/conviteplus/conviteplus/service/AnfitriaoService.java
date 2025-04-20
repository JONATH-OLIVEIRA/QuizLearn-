package com.conviteplus.conviteplus.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.conviteplus.conviteplus.dto.AnfitriaoComEventosDTO;
import com.conviteplus.conviteplus.dto.AtualizarAnfitriaoDTO;
import com.conviteplus.conviteplus.dto.AtualizarSenhaDTO;
import com.conviteplus.conviteplus.model.Anfitriao;
import com.conviteplus.conviteplus.model.Evento;
import com.conviteplus.conviteplus.repository.AnfitriaoRepository;

import jakarta.transaction.Transactional;

@Service
public class AnfitriaoService {
    private final AnfitriaoRepository anfitriaoRepository;
    private final EventoService eventoService;
    private final PresenteService presenteService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AnfitriaoService(AnfitriaoRepository anfitriaoRepository, EventoService eventoService, 
                            PresenteService presenteService, PasswordEncoder passwordEncoder) {
        this.anfitriaoRepository = anfitriaoRepository;
        this.eventoService = eventoService;
        this.presenteService = presenteService;
        this.passwordEncoder = passwordEncoder;
    }

    // Salvar um novo anfitrião
    @Transactional
    public Anfitriao salvar(Anfitriao anfitriao) {
        anfitriao.setSenha(passwordEncoder.encode(anfitriao.getSenha())); // Criptografar a senha
        return anfitriaoRepository.save(anfitriao);
    }

    // Listar todos os anfitriões
    public List<Anfitriao> listarTodos() {
        return anfitriaoRepository.findAll();
    }

    // Buscar anfitrião por ID
    public Anfitriao buscarPorId(Long id) {
        return anfitriaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anfitrião não encontrado com o ID: " + id));
    }

    // Buscar anfitrião por email
    public Anfitriao buscarPorEmail(String email) {
        return anfitriaoRepository.findByEmail(email);
    }

    // Login: verificar email e senha
    public Anfitriao login(String email, String senha) {
        Anfitriao anfitriao = anfitriaoRepository.findByEmail(email);
        if (anfitriao != null && passwordEncoder.matches(senha, anfitriao.getSenha())) {
            return anfitriao;
        }
        return null; // Login falhou
    }

    // Listar anfitriões com eventos e presentes
    public List<AnfitriaoComEventosDTO> listarAnfitrioesComEventosEPresentes() {
        return anfitriaoRepository.findAll().stream()
                .map(anfitriao -> {
                    AnfitriaoComEventosDTO dto = new AnfitriaoComEventosDTO();
                    dto.setAnfitriaoId(anfitriao.getId());
                    dto.setNome(anfitriao.getNome());
                    dto.setEventos(eventoService.listarPorAnfitriao(anfitriao.getId()).stream()
                            .map(evento -> {
                                AnfitriaoComEventosDTO.EventoDTO eventoDTO = new AnfitriaoComEventosDTO.EventoDTO();
                                eventoDTO.setEventoId(evento.getId());
                                eventoDTO.setNomeEvento(evento.getNomeEvento());
                                eventoDTO.setEnderecoEvento(evento.getEnderecoEvento());
                                eventoDTO.setQuantConvidados(evento.getQuantConvidados());
                                eventoDTO.setPresentes(presenteService.buscarPresentesPorEvento(evento.getId()).stream()
                                        .map(presente -> {
                                            AnfitriaoComEventosDTO.PresenteDTO presenteDTO = new AnfitriaoComEventosDTO.PresenteDTO();
                                            presenteDTO.setPresenteId(presente.getId());
                                            presenteDTO.setNome(presente.getNome());
                                            return presenteDTO;
                                        }).collect(Collectors.toList()));
                                return eventoDTO;
                            }).collect(Collectors.toList()));
                    return dto;
                }).collect(Collectors.toList());
    }

    // Obter evento por ID
    public Evento getEventoPorId(Long anfitriaoId, Long eventoId) {
        Anfitriao anfitriao = anfitriaoRepository.findById(anfitriaoId).orElse(null);
        if (anfitriao != null) {
            return anfitriao.getEventos().stream()
                    .filter(evento -> evento.getId().equals(eventoId))
                    .findFirst().orElse(null);
        }
        return null;
    }
    
    public Anfitriao atualizarAnfitriao(Long id, AtualizarAnfitriaoDTO dto) {
        // Localizar o anfitrião no banco de dados
        Anfitriao anfitriao = anfitriaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Anfitrião não encontrado"));

        // Atualizar os dados do anfitrião
        anfitriao.setNome(dto.getNome());
        anfitriao.setEmail(dto.getEmail());

        // Salvar as alterações
        return anfitriaoRepository.save(anfitriao);
    }
    
    public void atualizarSenha(Long id, AtualizarSenhaDTO dto) {
        // Localizar o anfitrião no banco de dados
        Anfitriao anfitriao = anfitriaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Anfitrião não encontrado"));

        // Verificar a senha atual usando PasswordEncoder
        if (!passwordEncoder.matches(dto.getSenhaAtual(), anfitriao.getSenha())) {
            throw new RuntimeException("Senha atual inválida");
        }

        // Criptografar a nova senha antes de salvar
        anfitriao.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        anfitriaoRepository.save(anfitriao);
    }
    
    public boolean deletar(Long id) {
        if (anfitriaoRepository.existsById(id)) {
            anfitriaoRepository.deleteById(id); // Deleta o anfitrião do banco
            return true; // Retorna true se a exclusão for bem-sucedida
        }
        return false; // Retorna false se o anfitrião não for encontrado
    }
}

