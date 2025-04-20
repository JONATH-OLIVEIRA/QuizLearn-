<script setup>
import { Cloudinary } from '@cloudinary/url-gen'
import { AdvancedImage } from "@cloudinary/vue"
import { fill } from "@cloudinary/url-gen/actions/resize"

// Usa seu Cloud Name
const cld = new Cloudinary({
  cloud: {
    cloudName: 'dthwnzrla',
  },
})

// Substitua pelo public ID da sua imagem após upload
const myImg = cld.image('free-png_sdpewi')
myImg.resize(fill().width(250).height(250))
</script>

<template>
  <div>
    <h3>Imagem do Evento</h3>
    <AdvancedImage :cldImg="myImg" />
  </div>
</template>
