<!-- src/components/ImagemTeste.vue -->
<script setup>
import { Cloudinary } from '@cloudinary/url-gen'
import { AdvancedImage } from '@cloudinary/vue'
import { fill } from '@cloudinary/url-gen/actions/resize'

// Instância do Cloudinary com seu Cloud Name
const cld = new Cloudinary({
  cloud: {
    cloudName: 'dthwnzrla',
  },
})

// Substitua pelo public ID da sua imagem no Cloudinary
const myImg = cld.image('free-png_sdpewi')

// Redimensiona a imagem
myImg.resize(fill().width(300).height(300))
</script>

<template>
  <div class="container mt-5">
    <div class="card shadow rounded-4 p-4">
      <h2 class="text-center mb-4">Pré-visualização da Imagem do Evento</h2>

      <div class="d-flex justify-content-center">
        <AdvancedImage :cldImg="myImg" class="img-fluid rounded" />
      </div>

      <div class="text-center mt-3 text-muted">
        <small>Essa imagem foi carregada diretamente da sua conta Cloudinary.</small>
      </div>
    </div>
  </div>
</template>
