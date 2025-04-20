package com.conviteplus.conviteplus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conviteplus.conviteplus.model.Convidado;
import com.conviteplus.conviteplus.repository.ConvidadoRepository;

@Service
public class ConvidadoService {

	private final ConvidadoRepository convidadoRepository;

	@Autowired
	public ConvidadoService(ConvidadoRepository convidadoRepository) {
		this.convidadoRepository = convidadoRepository;
	}

	public Convidado salvar(Convidado convidado) {
		return convidadoRepository.save(convidado); // Salva o convidado no banco de dados
	}

	public Convidado buscarPorId(Long id) {
		return convidadoRepository.findById(id).orElse(null); // Retorna o convidado ou null se não encontrado
	}

	public boolean deletar(Long id) {
		if (convidadoRepository.existsById(id)) {
			convidadoRepository.deleteById(id); // Deleta o convidado
			return true;
		}
		return false; // Retorna falso se o convidado não for encontrado
	}

	public String atualizarConfirmacao(Long id, Boolean resposta) {
		Convidado convidado = convidadoRepository.findById(id).orElse(null);

		if (convidado == null) {
			return null;
		}

		convidado.setAceitouConvite(resposta);
		convidadoRepository.save(convidado);

		if (Boolean.FALSE.equals(resposta)) {
			// Aqui é onde cancelamos o "acesso leve"
			// (Poderíamos marcar como recusado ou desabilitar o acesso, se necessário)
			return "Convite recusado. Acesso leve cancelado.";
		}

		return "Resposta registrada com sucesso.";
	}

	public Convidado buscarPorTelefoneEEvento(String telefone, Long eventoId) {
		return convidadoRepository.findByTelefoneAndEventoId(telefone, eventoId);
	}

	public List<Convidado> listarPorEventoId(Long eventoId) {
		return convidadoRepository.findByEventoId(eventoId);
	}
	
	

}
