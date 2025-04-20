<template>
  <div class="container mt-5">
    <div class="card shadow p-4">
      <h2 class="text-center mb-4">🔑 Acesso do Convidado</h2>

      <div v-if="mensagem" class="alert alert-info text-center">{{ mensagem }}</div>

      <form @submit.prevent="acessarResumo">
        <div class="mb-3">
          <label for="telefone" class="form-label">Digite seu telefone:</label>
          <input
            type="text"
            id="telefone"
            v-model="telefone"
            class="form-control"
            placeholder="Ex: 85999999999"
            required
          />
        </div>

        <button type="submit" class="btn btn-primary w-100">Entrar</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'LoginConvidado',
  data() {
    return {
      telefone: '',
      mensagem: '',
      eventoId: null
    };
  },
  mounted() {
    // Recupera da URL ou do localStorage
    let eventoIdFromUrl = this.$route.query.eventoId;
    if (!eventoIdFromUrl || isNaN(Number(eventoIdFromUrl))) {
      eventoIdFromUrl = localStorage.getItem('eventoId');
    }

    if (eventoIdFromUrl && !isNaN(Number(eventoIdFromUrl))) {
      this.eventoId = Number(eventoIdFromUrl);
      localStorage.setItem('eventoId', this.eventoId); // Garante persistência
    } else {
      this.eventoId = null;
    }
  },
  methods: {
      async acessarResumo() {
          if (!this.eventoId) {
              this.mensagem = 'Evento não identificado. Acesse o link correto do convite.';
              return;
          }

          try {
              const response = await axios.get(`http://localhost:8080/convidados/acesso`, {
                  params: {
                      telefone: this.telefone,
                      eventoId: this.eventoId
                  }
              });

              const convidado = response.data;

              // Salva no localStorage para uso posterior
              localStorage.setItem('telefone', this.telefone);
              localStorage.setItem('eventoId', this.eventoId);

              this.mensagem = '✅ Acesso liberado. Redirecionando...';

              setTimeout(() => {
                  this.$router.push({
                      path: '/convidado/resumo',
                      query: {
                          eventoId: convidado.eventoId,
                          telefone: convidado.telefone
                      }
                  });
              }, 1000);
          } catch (error) {
              if (error.response && error.response.status === 403) {
                  this.mensagem = '❌ Você recusou o convite. Acesso negado.';
              } else {
                  this.mensagem = '❌ Convidado não encontrado. Verifique o telefone.';
              }
          }
      }
  }
};
</script>

<style scoped>
.container {
  max-width: 400px;
}
</style>
