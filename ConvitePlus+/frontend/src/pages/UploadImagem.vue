<script setup>
import { ref } from 'vue';

const imagemSelecionada = ref(null);
const imagemUrl = ref('');
const enviando = ref(false);

const uploadImagem = async () => {
  if (!imagemSelecionada.value) return;

  enviando.value = true;

  const formData = new FormData();
  formData.append('file', imagemSelecionada.value);
  formData.append('upload_preset', 'evento_upload'); // seu preset
  formData.append('folder', 'eventos'); // opcional

  try {
    const resposta = await fetch('https://api.cloudinary.com/v1_1/dthwnzrla/image/upload', {
      method: 'POST',
      body: formData
    });

    const data = await resposta.json();
    imagemUrl.value = data.secure_url;
  } catch (erro) {
    console.error('Erro ao enviar imagem:', erro);
  } finally {
    enviando.value = false;
  }
};

const handleFileChange = (e) => {
  imagemSelecionada.value = e.target.files[0];
};
</script>

<template>
  <div class="container mt-5">
    <div class="card shadow rounded-4 p-4">
      <h4 class="mb-3">Upload de Imagem do Evento</h4>

      <input class="form-control mb-3" type="file" accept="image/*" @change="handleFileChange" />

      <button class="btn btn-primary" :disabled="!imagemSelecionada || enviando" @click="uploadImagem">
        {{ enviando ? 'Enviando...' : 'Enviar Imagem' }}
      </button>

      <div v-if="imagemUrl" class="mt-4">
        <h5>Imagem Enviada:</h5>
        <img :src="imagemUrl" alt="Imagem enviada" class="img-fluid rounded" style="max-width: 400px;" />
      </div>
    </div>
  </div>
</template>

