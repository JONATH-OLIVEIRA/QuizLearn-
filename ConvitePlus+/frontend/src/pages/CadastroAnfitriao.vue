<template>
  <div class="container mt-5 d-flex justify-content-center align-items-center" style="min-height: 100vh;">
    <div class="card shadow-lg p-4" style="width: 450px;">
      <h1 class="text-center text-primary mb-4">Cadastrar Anfitrião</h1>

      <form @submit.prevent="criarAnfitriao">
        <div class="mb-3">
          <label for="nome" class="form-label fw-bold">Nome:</label>
          <input 
            type="text" 
            id="nome" 
            v-model="anfitriao.nome" 
            class="form-control" 
            placeholder="Digite seu nome completo" 
            required 
          />
        </div>

        <div class="mb-3">
          <label for="email" class="form-label fw-bold">E-mail:</label>
          <input 
            type="email" 
            id="email" 
            v-model="anfitriao.email" 
            class="form-control" 
            placeholder="Digite seu e-mail válido" 
            required 
          />
        </div>

        <div class="mb-3">
          <label for="senha" class="form-label fw-bold">Senha:</label>
          <input 
            type="password" 
            id="senha" 
            v-model="anfitriao.senha" 
            class="form-control" 
            placeholder="Crie uma senha segura" 
            required 
          />
        </div>

        <button type="submit" class="btn btn-primary btn-lg w-100 fw-bold mt-3">
          Cadastrar
        </button>
      </form>

      <div v-if="mensagemSucesso" class="alert alert-success mt-4 text-center">
        <i class="bi bi-check-circle"></i> {{ mensagemSucesso }}
      </div>

      <div class="text-center mt-3">
        <p class="text-muted small">Já tem uma conta? <router-link to="/login" class="text-primary fw-bold">Faça login</router-link></p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { AuthService } from "@/services/authService"; 

export default {
  data() {
    return {
      anfitriao: {
        nome: "",
        email: "",
        senha: ""
      },
      mensagemSucesso: ""
    };
  },
  methods: {
    async criarAnfitriao() {
      try {
        const response = await axios.post("http://localhost:8080/api/anfitrioes", this.anfitriao);

        const anfitriaoCriado = response.data;
        AuthService.saveUser({
          anfitriao: {
            id: anfitriaoCriado.id,
            nome: anfitriaoCriado.nome,
            email: anfitriaoCriado.email
          },
          eventos: []
        });

        this.mensagemSucesso = "Anfitrião cadastrado com sucesso!";
        setTimeout(() => {
          this.$router.push(`/evento/novo?anfitriaoId=${anfitriaoCriado.id}`);
        }, 2000);
      } catch (error) {
        console.error("Erro ao cadastrar anfitrião:", error);
      }
    }
  }
};
</script>

<style scoped>
.card {
  border-radius: 15px;
}
.btn-primary {
  background-color: #007bff;
  border-color: #007bff;
}
.btn-primary:hover {
  background-color: #0056b3;
  border-color: #004080;
}
</style>
