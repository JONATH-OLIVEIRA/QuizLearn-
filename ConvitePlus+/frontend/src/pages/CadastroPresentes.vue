<template>
  <div class="container mt-5">
    <div class="card shadow-lg p-4">
      <h1 class="text-center text-primary mb-4">Cadastro de Sugestões de Presentes</h1>

      <!-- Formulário -->
      <form @submit.prevent="adicionarPresente" class="mb-4">
        <div class="input-group">
          <input 
            type="text" 
            id="nomePresente" 
            v-model="nomePresente" 
            class="form-control" 
            placeholder="Digite o nome do presente" 
            required
          >
          <button type="submit" class="btn btn-outline-primary">Adicionar</button>
        </div>
      </form>

      <!-- Lista de Presentes -->
      <h2 class="h5 text-primary mb-3">Lista de Presentes</h2>
      <ul class="list-group">
        <li 
          v-for="presente in presentes" 
          :key="presente.id" 
          class="list-group-item d-flex justify-content-between align-items-center"
        >
          <span>{{ presente.nome }}</span>
          <button 
            @click="removerPresente(presente.id)" 
            class="btn btn-outline-danger btn-sm"
          >
            Remover
          </button>
        </li>
      </ul>

      <!-- Botão de Concluir -->
      <div class="text-center mt-4">
        <button @click="concluirCadastro" class="btn btn-success btn-lg px-5">
          Concluir Cadastro
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import api from "@/services/api";

export default {
  setup() {
    const router = useRouter();
    const route = useRoute();
    const eventoId = ref(route.query.eventoId || "");
    const nomePresente = ref("");
    const presentes = ref([]);

    const carregarPresentes = async () => {
      try {
        const response = await api.get(`/presentes?eventoId=${eventoId.value}`);
        presentes.value = response.data;
      } catch (error) {
        console.error("Erro ao carregar presentes:", error);
      }
    };

    const adicionarPresente = async () => {
      if (!nomePresente.value.trim()) return;
      try {
        const response = await api.post("/presentes", {
          nome: nomePresente.value,
          eventoId: eventoId.value,
        });
        presentes.value.push(response.data);
        nomePresente.value = "";
      } catch (error) {
        console.error("Erro ao adicionar presente:", error);
      }
    };

    const removerPresente = async (id) => {
      try {
        await api.delete(`/presentes/${id}`);
        presentes.value = presentes.value.filter((presente) => presente.id !== id);
      } catch (error) {
        console.error("Erro ao remover presente:", error);
      }
    };

    const concluirCadastro = () => {
      // Redirecionar para o dashboard do anfitrião, substituindo a página de resumo
      router.push(`/dashboard`);
    };

    onMounted(carregarPresentes);

    return { eventoId, nomePresente, presentes, adicionarPresente, removerPresente, concluirCadastro };
  },
};
</script>

<style scoped>
.card {
  border-radius: 15px;
  background-color: #fff;
}

.btn-success:hover {
  background-color: #218838;
}

.btn-outline-danger:hover {
  background-color: #dc3545;
  color: #fff;
}

.input-group input {
  border-radius: 0.25rem;
}

.list-group-item {
  border: 1px solid #ddd;
  border-radius: 0.5rem;
}

.list-group-item:hover {
  background-color: #f8f9fa;
}
</style>
