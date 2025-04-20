<template>
  <div class="container mt-5">
    <div class="card shadow rounded p-4">
      <h2 class="mb-4 text-center">🎉 Resumo do Evento</h2>

      <!-- Mensagem de erro ou status -->
      <div v-if="mensagem" class="alert alert-info">
        {{ mensagem }}
      </div>

      <!-- Evento -->
      <div v-if="evento" class="mb-4 text-center">
        <img v-if="evento.imagemUrl" :src="evento.imagemUrl" alt="Imagem do Evento" class="rounded shadow-sm mb-4"
          style="width: 300px; height: 300px; object-fit: cover;" />
        <p><strong>Nome do Evento:</strong> {{ evento.nome }}</p>
        <p><strong>Endereço:</strong> {{ evento.endereco }}</p>
        <p><strong>Data:</strong> {{ evento.data }}</p>
        <p><strong>Hora:</strong> {{ evento.hora }}</p>

        <h5 class="mt-4">🎁 Presentes:</h5>
        <ul class="list-group text-start">
          <li class="list-group-item" v-for="(presente, index) in evento.presentes" :key="index">
            {{ presente.nome }}
          </li>
        </ul>
      </div>

      <!-- Convidado -->
      <div v-if="convidado" class="mt-4">
        <h5 class="fw-bold text-center">Olá, {{ convidado.nome }}!</h5>

        <!-- Botões de resposta só aparecem se aceitouConvite for null ou undefined -->
        <div v-if="convidado.aceitouConvite == null" class="mt-3 text-center">
          <p class="mb-2">Você irá ao evento?</p>
          <button class="btn btn-success me-2" @click="responderConvite(true)">
            ✅ Vou ao evento
          </button>
          <button class="btn btn-danger" @click="responderConvite(false)">
            ❌ Não poderei ir
          </button>
        </div>

        <!-- Mensagem para quem confirmou presença -->
        <div v-else-if="convidado.aceitouConvite" class="alert alert-success text-center mt-3">
          🎉 Presença confirmada! Nos vemos lá!
        </div>

        <!-- Mensagem para quem recusou o convite -->
        <div v-else class="alert alert-danger text-center mt-3">
          ❌ Você recusou o convite. Acesso encerrado.
        </div>
      </div>

      <!-- Botão Sair -->
      <div class="mt-4 text-center">
        <button class="btn btn-secondary" @click="redirecionarParaHome">
          ⬅️ Sair
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ResumoConvidado',
  data() {
    return {
      evento: null,
      convidado: null,
      mensagem: '',
    };
  },
  async mounted() {
    // Recuperando os dados do localStorage
    const eventoId = localStorage.getItem('eventoId');
    const telefone = localStorage.getItem('telefone');

    // Verificando se os dados necessários estão no localStorage
    if (!eventoId || !telefone) {
      this.mensagem = '🔒 Dados de acesso inválidos ou expirados. Por favor, faça o login novamente.';
      return;
    }

    try {
      // Busca o convidado
      const response = await axios.get(
        `http://localhost:8080/convidados/acesso?telefone=${telefone}&eventoId=${eventoId}`
      );
      this.convidado = response.data;
    } catch (error) {
      if (error.response && error.response.status === 403) {
        this.mensagem = '❌ Você recusou o convite. Acesso encerrado.';
      } else {
        this.mensagem = 'Convidado não encontrado ou link inválido.';
      }
      return;
    }

    try {
      // Busca o evento
      const eventoResponse = await axios.get(`http://localhost:8080/api/eventos/${eventoId}`);
      // Formatar a data no formato dia/mês/ano
      const dataFormatada = new Intl.DateTimeFormat('pt-BR').format(new Date(eventoResponse.data.dataEvento));
      // Formatar a hora para exibir apenas horas e minutos (HH:mm)
      const horaFormatada = new Intl.DateTimeFormat('pt-BR', {
        hour: '2-digit',
        minute: '2-digit',
      }).format(new Date(`1970-01-01T${eventoResponse.data.horaEvento}`));

      this.evento = {
        nome: eventoResponse.data.nomeEvento,
        endereco: eventoResponse.data.enderecoEvento,
        presentes: eventoResponse.data.sugestoesPresentes,
        imagemUrl: eventoResponse.data.imagemUrl, // Adiciona o campo imagemUrl
        data: dataFormatada, // Usa a data formatada
        hora: horaFormatada, // Adiciona o campo hora
      };
    } catch (error) {
      this.mensagem = 'Erro ao carregar os dados do evento.';
    }
  },
  methods: {
    async responderConvite(resposta) {
      try {
        await axios.put(
          `http://localhost:8080/convidados/${this.convidado.id}/confirmacao`,
          { resposta }
        );
        this.convidado.aceitouConvite = resposta;

        if (resposta) {
          this.mensagem = 'Obrigado por confirmar sua presença!';
        } else {
          this.mensagem = 'Sentiremos sua falta. Redirecionando...';
          setTimeout(() => {
            this.$router.push('/');
          }, 3000);
        }
      } catch (error) {
        this.mensagem = 'Erro ao registrar resposta.';
      }
    },
    redirecionarParaHome() {
      this.$router.push('/');
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 600px;
}

.img-fluid {
  width: 300px;
  height: 300px;
  object-fit: cover;
  margin: 0 auto;
}
</style>
