<template>
  <div class="container mt-5">
    <h1 class="mb-4 text-primary">Editar Evento</h1>

    <div v-if="evento">
      <div class="mb-3">
        <label class="form-label fw-bold">ID do Evento:</label>
        <p class="form-control-plaintext">{{ evento.id }}</p>
      </div>

      <div class="mb-3">
        <label class="form-label fw-bold">Nome do Evento:</label>
        <input v-model="evento.nomeEvento" class="form-control" />
      </div>

      <div class="mb-3">
        <label class="form-label fw-bold">Endereço:</label>
        <input v-model="evento.enderecoEvento" class="form-control" />
      </div>

      <div class="mb-3">
        <label class="form-label fw-bold">Quantidade de Convidados:</label>
        <input type="number" v-model="evento.quantConvidados" class="form-control" />
      </div>

      <div class="mb-3">
        <label class="form-label fw-bold">Data do Evento:</label>
        <input type="date" v-model="evento.dataEvento" class="form-control" />
      </div>

      <div class="mb-3">
        <label class="form-label fw-bold">Hora do Evento:</label>
        <input type="time" v-model="evento.horaEvento" class="form-control" />
      </div>

      <div class="mb-3">
        <label class="form-label fw-bold">Imagem do Evento:</label>
        <input type="file" @change="handleFileChange" class="form-control" />
        <div v-if="imagemPreview">
          <img :src="imagemPreview" alt="Imagem do evento" class="img-thumbnail mt-2" />
        </div>
        <div v-else-if="evento.imagemUrl">
          <img :src="evento.imagemUrl" alt="Imagem do evento" class="img-thumbnail mt-2" />
        </div>
      </div>

      <h2 class="mt-5 mb-3 text-secondary">Presentes</h2>
      <ul class="list-group mb-4">
        <li v-for="(presente, index) in presentes" :key="index" class="list-group-item d-flex justify-content-between align-items-center">
          <span>{{ presente.nome }}</span>
          <div>
            <button class="btn btn-sm btn-outline-secondary me-2" @click="editarPresente(presente)">Editar</button>
            <button class="btn btn-sm btn-outline-danger" @click="excluirPresente(presente.id)">Excluir</button>
          </div>
        </li>
      </ul>

      <div class="input-group mb-3">
        <input v-model="novoPresenteNome" class="form-control" placeholder="Digite o nome do novo presente" />
        <button class="btn btn-success" @click="adicionarPresente">Adicionar Novo Presente</button>
      </div>

      <div class="mt-3">
        <button class="btn btn-primary" :disabled="enviando" @click="salvarEdicao">
          {{ enviando ? 'Salvando...' : 'Salvar Alterações' }}
        </button>
        <button class="btn btn-secondary ms-2" @click="voltarParaDashboard">Cancelar</button>
      </div>
    </div>

    <div v-else>
      <p>Carregando evento...</p>
    </div>

    <div v-if="sucessoMensagem" class="alert alert-success text-center mt-4">
      {{ sucessoMensagem }}
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { AuthService } from "@/services/authService";

export default {
  props: {
    eventoId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      evento: null,
      presentes: [],
      sucessoMensagem: null,
      novoPresenteNome: "",
      imagemPreview: null, // Pré-visualização da imagem no front-end
      imagemFile: null,    // Arquivo da imagem selecionada pelo usuário
      nomeArquivo: null,   // Nome do arquivo selecionado
      enviando: false,     // Indica processo de envio
    };
  },
  created() {
    const user = AuthService.getUser();
    if (!user || !user.anfitriao) {
      this.$router.push("/login");
    } else {
      this.buscarEvento(this.eventoId);
    }
  },
  methods: {
    buscarEvento(id) {
      axios
        .get(`http://localhost:8080/api/eventos/${id}`)
        .then((response) => {
          const eventoResponse = response.data;

          if (!eventoResponse || !eventoResponse.nomeEvento || !eventoResponse.dataEvento) {
            console.error("Erro: Evento retornado pela API não contém os campos esperados.");
            this.evento = null;
            return;
          }

          console.log("Evento carregado:", eventoResponse);

          // Mapear os dados do evento para o frontend
          this.evento = {
            id: id,
            nomeEvento: eventoResponse.nomeEvento,
            enderecoEvento: eventoResponse.enderecoEvento,
            quantConvidados: eventoResponse.quantConvidados,
            dataEvento: eventoResponse.dataEvento, // Já está no formato ISO
            horaEvento: eventoResponse.horaEvento.substring(0, 5), // Ajustar para HH:mm
            anfitriao: { id: eventoResponse.anfitriaoId }, // Adicionar o anfitrião como objeto
            imagemUrl: eventoResponse.imagemUrl,
          };

          // Atualizar a lista de presentes com nomes e IDs
          this.presentes = eventoResponse.sugestoesPresentes.map((presente) => ({
            id: presente.id,
            nome: presente.nome, // Usar o nome real do presente fornecido pelo backend
          }));

          // Atualizar pré-visualização da imagem
          this.imagemPreview = eventoResponse.imagemUrl || null;
        })
        .catch((error) => console.error("Erro ao buscar evento:", error));
    },

    async salvarEdicao() {
      if (!this.evento || !this.evento.id) {
        console.error("Erro: O evento não está definido ou não possui ID.");
        return;
      }

      this.enviando = true;

      let urlImagem = this.evento.imagemUrl; // Comece com a URL existente

      // Se uma nova imagem foi selecionada, faça o upload dela
      if (this.imagemFile) {
        try {
          // Upload da nova imagem para o Cloudinary
          const formData = new FormData();
          formData.append("file", this.imagemFile);
          formData.append("upload_preset", "evento_upload");
          formData.append("folder", "eventos");

          const response = await fetch("https://api.cloudinary.com/v1_1/dthwnzrla/image/upload", {
            method: "POST",
            body: formData,
          });

          const data = await response.json();
          urlImagem = data.secure_url;

          // Excluir imagem antiga no Cloudinary
          const publicIdAntigo = this.evento.imagemUrl.split('/').slice(-1)[0].split('.')[0]; // Extrai public_id
          await fetch(`https://api.cloudinary.com/v1_1/dthwnzrla/image/destroy`, {
            method: "POST",
            body: JSON.stringify({
              public_id: `eventos/${publicIdAntigo}`
            }),
            headers: {
              "Content-Type": "application/json"
            }
          });

        } catch (error) {
          console.error("Erro ao substituir a imagem:", error);
          return;
        }
      }

      const eventoData = {
        nomeEvento: this.evento.nomeEvento,
        enderecoEvento: this.evento.enderecoEvento,
        quantConvidados: this.evento.quantConvidados,
        dataEvento: this.evento.dataEvento, // Garantir formato ISO
        horaEvento: this.evento.horaEvento,
        anfitriaoId: this.evento.anfitriao.id, // ID do anfitrião
        presentesIds: this.presentes.map((presente) => presente.id), // IDs dos presentes
        imagemUrl: urlImagem, // Substituir ou manter URL da imagem
      };

      try {
        await axios.put(`http://localhost:8080/api/eventos/${this.evento.id}`, eventoData);
        this.sucessoMensagem = "Evento atualizado com sucesso!";
        setTimeout(() => this.$router.push("/dashboard"), 2000);
      } catch (error) {
        console.error("Erro ao salvar evento:", error);
      } finally {
        this.enviando = false;
      }
    },

    voltarParaDashboard() {
      this.$router.push("/dashboard");
    },

    adicionarPresente() {
      if (this.novoPresenteNome.trim() === "") {
        alert("Nome inválido!");
        return;
      }

      axios
        .post(`http://localhost:8080/api/presentes`, { nome: this.novoPresenteNome, eventoId: this.evento.id })
        .then((response) => {
          this.presentes.push(response.data);
          this.novoPresenteNome = "";
        })
        .catch((error) => console.error("Erro ao adicionar presente:", error));
    },

    excluirPresente(presenteId) {
      if (!confirm("Tem certeza?")) return;

      axios
        .delete(`http://localhost:8080/api/presentes/${presenteId}`)
        .then(() => {
          this.presentes = this.presentes.filter((p) => p.id !== presenteId);
        })
        .catch((error) => console.error("Erro ao excluir presente:", error));
    },

    editarPresente(presente) {
      const novoNome = prompt("Novo nome:", presente.nome);
      if (!novoNome) return;

      axios
        .put(`http://localhost:8080/api/presentes/${presente.id}`, { nome: novoNome })
        .then((response) => {
          const index = this.presentes.findIndex((p) => p.id === presente.id);
          if (index !== -1) this.presentes.splice(index, 1, response.data);
        })
        .catch((error) => console.error("Erro ao editar presente:", error));
    },

    handleFileChange(event) {
      const file = event.target.files[0];
      if (!file) return;

      this.imagemFile = file;
      this.nomeArquivo = file?.name || "";

      const reader = new FileReader();
      reader.onload = (e) => (this.imagemPreview = e.target.result); // Exibir pré-visualização da imagem
      reader.readAsDataURL(file);
    },
  },
};
</script>
