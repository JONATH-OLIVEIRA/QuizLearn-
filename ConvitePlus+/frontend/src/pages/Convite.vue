<template>
  <div class="container mt-5">
    <h1 class="text-primary mb-3">Convite para Evento</h1>
    <p class="mb-4">Preencha seu nome e telefone para continuar</p>

    <!-- Mensagem de sucesso -->
    <div
      v-if="mensagem"
      class="alert alert-success text-center animate__animated animate__fadeInDown"
      role="alert"
    >
      {{ mensagem }}
    </div>

    <form @submit.prevent="cadastrarConvidado" class="card p-4 shadow-sm">
      <div class="mb-3">
        <label for="nome" class="form-label fw-bold">Nome:</label>
        <input
          type="text"
          id="nome"
          v-model="nome"
          required
          class="form-control"
          placeholder="Digite seu nome"
        />
      </div>

      <div class="mb-3">
        <label for="telefone" class="form-label fw-bold">Telefone:</label>
        <input
          type="text"
          id="telefone"
          v-model="telefone"
          required
          class="form-control"
          placeholder="Digite seu telefone"
        />
      </div>

      <div class="text-end">
        <button
          type="submit"
          class="btn btn-primary"
          :disabled="carregando"
        >
          <span
            v-if="carregando"
            class="spinner-border spinner-border-sm me-2"
            role="status"
            aria-hidden="true"
          ></span>
          Continuar
        </button>
      </div>
    </form>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

export default {
  setup() {
    const nome = ref('');
    const telefone = ref('');
    const route = useRoute();
    const router = useRouter();
    const codigoConvite = ref('');
    const carregando = ref(false);
    const mensagem = ref('');

    onMounted(() => {
      codigoConvite.value = route.params.codigoConvite || '';
      console.log("Código do convite recebido:", codigoConvite.value);
    });

    const cadastrarConvidado = async () => {
      if (!codigoConvite.value) {
        alert('Código do convite inválido!');
        return;
      }

      if (!nome.value || !telefone.value) {
        alert('Por favor, preencha todos os campos!');
        return;
      }

      const data = {
        nome: nome.value,
        telefone: telefone.value
      };

      try {
        carregando.value = true;

        const response = await fetch(
          `http://localhost:8080/api/eventos/accept/${codigoConvite.value}`,
          {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data),
          }
        );

        if (!response.ok) {
          throw new Error('Erro ao cadastrar convidado');
        }

        const responseData = await response.json();

        // 🔐 Salvar telefone e eventoId no localStorage
        localStorage.setItem('telefone', telefone.value);
        localStorage.setItem('eventoId', responseData.eventoId);

        mensagem.value = 'Cadastro realizado com sucesso! Redirecionando...';

        setTimeout(() => {
          mensagem.value = '';
          // ✅ Redireciona sem query string
          router.push('/convidado/resumo');
        }, 2000);
      } catch (error) {
        console.error('Erro:', error);
        alert('Ocorreu um erro ao cadastrar. Tente novamente.');
      } finally {
        carregando.value = false;
      }
    };

    return {
      nome,
      telefone,
      cadastrarConvidado,
      carregando,
      mensagem,
    };
  },
};
</script>

<style scoped>
.container {
  max-width: 500px;
}
</style>
