<template>
    <div class="container mt-5">
        <div class="card shadow rounded p-4">
            <h2 class="text-center mb-4">👤 Dados do Anfitrião</h2>

            <!-- Mensagem de sucesso ou erro -->
            <div v-if="mensagem" class="alert" :class="mensagem.tipo === 'sucesso' ? 'alert-success' : 'alert-danger'">
                {{ mensagem.texto }}
            </div>

            <!-- Formulário para atualizar os dados -->
            <form @submit.prevent="salvarDados">
                <div class="mb-3">
                    <label for="nome" class="form-label fw-bold">Nome:</label>
                    <input type="text" id="nome" v-model="nome" class="form-control" required />
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label fw-bold">Email:</label>
                    <input type="email" id="email" v-model="email" class="form-control" required />
                </div>

                <div class="text-center mt-4">
                    <button type="submit" class="btn btn-primary me-2">Salvar Alterações</button>
                    <button type="button" class="btn btn-secondary" @click="voltarParaDashboard">Voltar</button>
                </div>
            </form>

            <hr class="my-4" />

            <!-- Formulário para atualizar a senha -->
            <h3 class="text-center mb-4">🔒 Alteração de Senha</h3>
            <form @submit.prevent="atualizarSenha">
                <div class="mb-3">
                    <label for="senhaAtual" class="form-label fw-bold">Senha Atual:</label>
                    <input type="password" id="senhaAtual" v-model="senhaAtual" class="form-control" required />
                </div>

                <div class="mb-3">
                    <label for="novaSenha" class="form-label fw-bold">Nova Senha:</label>
                    <input type="password" id="novaSenha" v-model="novaSenha" class="form-control" required />
                </div>

                <div class="text-center mt-4">
                    <button type="submit" class="btn btn-warning">Alterar Senha</button>
                </div>
            </form>

            <hr class="my-4" />

            <!-- Botão para excluir a conta -->
            <div class="text-center mt-4">
                <button type="button" class="btn btn-danger" @click="excluirConta">
                    Excluir Conta
                </button>
            </div>
        </div>
    </div>
</template>
<script>
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import api from "@/services/api";
import { AuthService } from "@/services/authService";

export default {
    setup() {
        const router = useRouter();
        const route = useRoute();

        const anfitriaoId = ref(null);
        const nome = ref("");
        const email = ref("");
        const senhaAtual = ref("");
        const novaSenha = ref("");
        const mensagem = ref(null);

        const carregarDados = async () => {
            try {
                const user = AuthService.getUser();
                anfitriaoId.value = route.query.anfitriaoId || user?.anfitriao?.id;

                if (!anfitriaoId.value) {
                    mensagem.value = { tipo: "erro", texto: "Erro: Anfitrião não encontrado." };
                    return;
                }

                const response = await api.get(`/anfitrioes/${anfitriaoId.value}`);
                nome.value = response.data.nome;
                email.value = response.data.email;
            } catch (error) {
                mensagem.value = { tipo: "erro", texto: "Erro ao carregar os dados do anfitrião." };
                console.error(error);
            }
        };

        const salvarDados = async () => {
            if (!anfitriaoId.value) {
                mensagem.value = { tipo: "erro", texto: "Erro: ID do anfitrião ausente." };
                return;
            }

            try {
                const response = await api.put(`/anfitrioes/${anfitriaoId.value}`, { nome: nome.value, email: email.value });
                mensagem.value = { tipo: "sucesso", texto: "Dados atualizados com sucesso!" };
                AuthService.saveUser({ ...AuthService.getUser(), anfitriao: response.data });
            } catch (error) {
                mensagem.value = { tipo: "erro", texto: "Erro ao atualizar os dados do anfitrião." };
                console.error(error);
            }
        };

        const atualizarSenha = async () => {
            if (!anfitriaoId.value) {
                mensagem.value = { tipo: "erro", texto: "Erro: ID do anfitrião ausente." };
                return;
            }

            try {
                await api.put(`/anfitrioes/${anfitriaoId.value}/senha`, { senhaAtual: senhaAtual.value, novaSenha: novaSenha.value });
                mensagem.value = { tipo: "sucesso", texto: "Senha atualizada com sucesso!" };
                senhaAtual.value = "";
                novaSenha.value = "";
            } catch (error) {
                mensagem.value = { tipo: "erro", texto: "Erro ao atualizar a senha." };
                console.error(error);
            }
        };

        const excluirConta = async () => {
            if (!anfitriaoId.value) {
                mensagem.value = { tipo: "erro", texto: "Erro: ID do anfitrião ausente." };
                return;
            }

            try {
                const response = await api.delete(`/anfitrioes/${anfitriaoId.value}`);
                mensagem.value = { tipo: "sucesso", texto: response.data.message || "Conta excluída com sucesso!" };

                // Limpar todos os dados da sessão e do localStorage
                localStorage.clear(); // Remove todos os dados armazenados
                sessionStorage.clear(); // Limpa qualquer dado de sessão

                // Redirecionar para a página principal
                setTimeout(() => {
                    window.location.href = "http://localhost:5173/";
                }, 2000); // Aguarda 2 segundos para exibir a mensagem antes do redirecionamento
            } catch (error) {
                mensagem.value = { tipo: "erro", texto: "Erro ao excluir a conta." };
                console.error(error);
            }
        };



        const voltarParaDashboard = () => {
            router.push("/dashboard");
        };

        onMounted(() => {
            carregarDados();
        });

        return {
            nome,
            email,
            senhaAtual,
            novaSenha,
            mensagem,
            salvarDados,
            atualizarSenha,
            excluirConta,
            voltarParaDashboard,
        };
    },
};
</script>
<style scoped>
.container {
    max-width: 600px;
}
</style>
