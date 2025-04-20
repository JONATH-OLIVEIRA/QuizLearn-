<template>
    <div>
      <h1>Gerenciar Presentes</h1>
      <ul>
        <li v-for="presente in presentes" :key="presente.id">
          <span>{{ presente.nome }}</span>
          <button @click="editarPresente(presente.id)">Editar</button>
          <button @click="excluirPresente(presente.id)">Excluir</button>
        </li>
      </ul>
      <button @click="adicionarPresente">Adicionar Novo Presente</button>
    </div>
  </template>
  
  <script>
  import api from "@/services/api"; // Certifique-se de ter configurado o axios
  
  export default {
    props: ["eventoId"], // Recebe o eventoId da rota
    data() {
      return {
        presentes: [], // Armazena os presentes do evento
      };
    },
    methods: {
      async carregarPresentes() {
        try {
          const response = await api.get(`/eventos/${this.eventoId}/presentes`);
          this.presentes = response.data; // Carrega os presentes do evento
        } catch (error) {
          console.error("Erro ao carregar presentes:", error);
        }
      },
  
      editarPresente(presenteId) {
        // Lógica para editar presente
        this.$router.push(`/editarPresente/${presenteId}`); // Redireciona para a página de edição
      },
  
      async excluirPresente(presenteId) {
        try {
          await api.delete(`/presentes/${presenteId}`); // Faz a exclusão do presente
          this.presentes = this.presentes.filter(p => p.id !== presenteId); // Remove o presente da lista local
        } catch (error) {
          console.error("Erro ao excluir presente:", error);
        }
      },
  
      adicionarPresente() {
        this.$router.push(`/adicionarPresente/${this.eventoId}`); // Navega para a página de adicionar presente
      }
    },
    created() {
      this.carregarPresentes();
    }
  };
  </script>
  
  <style scoped>
  button {
    margin-top: 10px;
    cursor: pointer;
  }
  </style>
  