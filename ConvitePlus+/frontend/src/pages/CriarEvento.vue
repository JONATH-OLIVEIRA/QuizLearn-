<template>
  <div class="container mt-5">
    <div class="card shadow-lg p-4">
      <h1 class="text-center text-primary mb-4">Criar Evento</h1>
      
      <form @submit.prevent="criarEvento">
        <input type="hidden" v-model="anfitriaoId" />

        <div class="mb-3">
          <label for="nomeEvento" class="form-label fw-bold">Nome do Evento:</label>
          <input 
            type="text" 
            id="nomeEvento" 
            v-model="nomeEvento" 
            class="form-control" 
            placeholder="Digite o nome do evento" 
            required 
          />
        </div>

        <div class="mb-3">
          <label for="enderecoEvento" class="form-label fw-bold">Endereço:</label>
          <input 
            type="text" 
            id="enderecoEvento" 
            v-model="enderecoEvento" 
            class="form-control" 
            placeholder="Digite o endereço" 
            required 
          />
        </div>

        <div class="mb-3">
          <label for="quantConvidados" class="form-label fw-bold">Quantidade de Convidados:</label>
          <input 
            type="number" 
            id="quantConvidados" 
            v-model="quantConvidados" 
            class="form-control" 
            min="1" 
            placeholder="Número de convidados" 
            required 
          />
        </div>

        <div class="mb-3">
          <label for="dataEvento" class="form-label fw-bold">Data do Evento:</label>
          <input 
            type="date" 
            id="dataEvento" 
            v-model="dataEvento" 
            class="form-control" 
            required 
          />
        </div>

        <div class="mb-3">
          <label for="horaEvento" class="form-label fw-bold">Hora do Evento:</label>
          <input 
            type="time" 
            id="horaEvento" 
            v-model="horaEvento" 
            class="form-control" 
            required 
          />
        </div>

        <div class="mb-3">
          <label for="imagemEvento" class="form-label fw-bold">Imagem do Evento:</label>
          <input 
            type="file" 
            id="imagemEvento" 
            accept="image/*" 
            class="form-control" 
            @change="handleFileChange" 
          />
          <p v-if="nomeArquivo" class="text-muted mt-1">Arquivo selecionado: {{ nomeArquivo }}</p>
        </div>

        <button 
          type="submit" 
          class="btn btn-success w-100 fw-bold"
          :disabled="enviando"
        >
          {{ enviando ? 'Enviando...' : 'Criar Evento' }}
        </button>

        <p 
          class="mt-3 text-center" 
          :class="mensagem.includes('sucesso') ? 'text-success' : 'text-danger'"
        >
          {{ mensagem }}
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import api from "@/services/api";

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();

    const anfitriaoId = ref(route.query.anfitriaoId || "");
    const nomeEvento = ref("");
    const enderecoEvento = ref("");
    const quantConvidados = ref(1);
    const dataEvento = ref("");
    const horaEvento = ref("");
    const imagemSelecionada = ref(null);
    const nomeArquivo = ref("");
    const enviando = ref(false);
    const mensagem = ref("");

    const handleFileChange = (e) => {
      const file = e.target.files[0];
      imagemSelecionada.value = file;
      nomeArquivo.value = file?.name || "";
    };

    const uploadImagem = async () => {
      if (!imagemSelecionada.value) return "";

      const formData = new FormData();
      formData.append("file", imagemSelecionada.value);
      formData.append("upload_preset", "evento_upload");
      formData.append("folder", "eventos");

      const response = await fetch("https://api.cloudinary.com/v1_1/dthwnzrla/image/upload", {
        method: "POST",
        body: formData,
      });

      const data = await response.json();

      if (data.secure_url) {
        return data.secure_url;
      } else {
        console.error("Erro ao enviar imagem para Cloudinary:", data);
        throw new Error("Erro no upload da imagem.");
      }
    };

    const criarEvento = async () => {
      try {
        enviando.value = true;
        mensagem.value = "";

        // Primeiro faz o upload da imagem se existir
        let urlImagem = "";
        if (imagemSelecionada.value) {
          urlImagem = await uploadImagem();
        }

        // Agora cria o evento no backend
        const response = await api.post(`/eventos/${anfitriaoId.value}`, {
          nomeEvento: nomeEvento.value,
          enderecoEvento: enderecoEvento.value,
          quantConvidados: quantConvidados.value,
          imagemUrl: urlImagem, // Envia a URL da imagem para o backend
          dataEvento: dataEvento.value, // Envia a data do evento
          horaEvento: horaEvento.value, // Envia a hora do evento
        });

        const eventoId = response.data.id;
        mensagem.value = "Evento criado com sucesso!";

        // Redireciona para a página de presentes com o ID do evento
        router.push(`/evento/presentes?eventoId=${eventoId}`);
      } catch (error) {
        console.error("Erro ao criar evento:", error);
        mensagem.value = "Erro ao criar evento.";
      } finally {
        enviando.value = false;
      }
    };

    return {
      anfitriaoId,
      nomeEvento,
      enderecoEvento,
      quantConvidados,
      dataEvento,
      horaEvento,
      imagemSelecionada,
      nomeArquivo,
      enviando,
      mensagem,
      criarEvento,
      handleFileChange,
    };
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

.form-control:focus {
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.6);
}

.text-muted {
  font-size: 0.85rem;
}

</style>
