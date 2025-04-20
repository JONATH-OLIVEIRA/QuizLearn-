<template>
  <div class="container mt-5 d-flex justify-content-center align-items-center" style="min-height: 100vh;">
    <div class="card shadow-lg p-4" style="width: 400px;">
      <h1 class="text-center text-primary mb-4">Acessar Conta</h1>

      <div class="mb-3">
        <label for="email" class="form-label fw-bold">E-mail:</label>
        <input v-model="email" id="email" type="email" class="form-control" placeholder="Digite seu e-mail" required />
      </div>

      <div class="mb-3">
        <label for="senha" class="form-label fw-bold">Senha:</label>
        <input v-model="senha" id="senha" type="password" class="form-control" placeholder="Digite sua senha" required />
      </div>

      <div class="text-center mt-4">
        <button class="btn btn-primary btn-lg w-100 fw-bold" @click="handleLogin">Entrar</button>
      </div>

      <div v-if="errorMessage" class="alert alert-danger mt-4 text-center">
        <i class="bi bi-exclamation-triangle"></i> {{ errorMessage }}
      </div>

      <div v-if="successMessage" class="alert alert-success mt-4 text-center">
        <i class="bi bi-check-circle"></i> {{ successMessage }}
      </div>

      <div class="text-center mt-4">
        <p class="text-muted small">
          Não tem uma conta? <router-link to="/cadastro-anfitriao" class="text-primary fw-bold">Cadastre-se</router-link>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { AuthService } from "@/services/authService";

export default {
  data() {
    return {
      email: "",
      senha: "",
      errorMessage: null,
      successMessage: null,
    };
  },
  methods: {
    async handleLogin() {
      this.errorMessage = null;
      this.successMessage = null;

      try {
        // Envia os dados de login para o AuthService
        const user = await AuthService.login(this.email, this.senha);

        console.log("Dados recebidos no login:", user);

        // Verificar se os eventos existem
        if (!user.eventos || user.eventos.length === 0) {
          this.errorMessage = "Nenhum evento encontrado para este usuário.";
          return;
        }

        // Exibir mensagem de sucesso
        this.successMessage = "Login realizado com sucesso!";
        setTimeout(() => {
          this.$router.push("/dashboard");
        }, 2000);
      } catch (error) {
        console.error("Erro ao fazer login:", error);
        this.errorMessage = error.message || "Erro ao fazer login.";
      }
    },
  },
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
