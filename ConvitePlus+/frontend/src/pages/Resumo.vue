<template>
  <div class="container mt-5">
    <div class="card shadow rounded p-4">
      <h2 class="mb-4 text-center">🎉 Resumo do Evento</h2>

      <div v-if="evento" class="mb-4">
        <p><strong>Nome do Evento:</strong> {{ evento.nome }}</p>
        <p><strong>Endereço:</strong> {{ evento.endereco }}</p>

        <h5 class="mt-3">🎁 Presentes:</h5>
        <ul class="list-group">
          <li class="list-group-item" v-for="(presente, index) in evento.presentes" :key="index">
            {{ presente.nome }}
          </li>
        </ul>
      </div>

      <div v-if="convidado" class="mt-4">
        <h5 class="fw-bold">Olá, {{ convidado.nome }}!</h5>

        <div v-if="convidado.aceitouConvite === null || convidado.aceitouConvite === undefined" class="mt-3">
          <p class="mb-2">Você irá ao evento?</p>
          <button class="btn btn-success me-2" @click="responderConvite(true)">
            ✅ Vou ao evento
          </button>
          <button class="btn btn-danger" @click="responderConvite(false)">
            ❌ Não poderei ir
          </button>
        </div>

        <div v-else-if="convidado.aceitouConvite === true" class="alert alert-success mt-3">
          🎉 Presença confirmada! Nos vemos lá!
        </div>

        <div v-else class="alert alert-danger mt-3">
          ❌ Você recusou o convite. Acesso encerrado.
        </div>
      </div>

      <div v-if="mensagem" class="alert alert-info mt-4">
        {{ mensagem }}
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
    const eventoId = this.$route.query.eventoId;
    const telefone = this.$route.query.telefone;

    try {
      const response = await axios.get(
        `http://localhost:8080/convidados/acesso?telefone=${telefone}&eventoId=${eventoId}`
      );
      this.convidado = response.data;

      const eventoResponse = await axios.get(`http://localhost:8080/api/eventos/${eventoId}`);
      this.evento = eventoResponse.data;
    } catch (error) {
      if (error.response) {
        if (error.response.status === 403) {
          this.mensagem = '❌ Você recusou o convite. Acesso encerrado.';
        } else if (error.response.status === 404) {
          this.mensagem = 'Convidado não encontrado ou link inválido.';
        } else {
          this.mensagem = 'Erro ao verificar o convite.';
        }
      } else {
        this.mensagem = 'Erro de conexão com o servidor.';
      }
    }
  },
  methods: {
    async responderConvite(resposta) {
      try {
        await axios.put(
          `http://localhost:8080/api/convidados/${this.convidado.id}/confirmacao`,
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
  },
};
</script>

<style scoped>
.container {
  max-width: 600px;
}
</style>
