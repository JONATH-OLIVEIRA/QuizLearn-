<template>
    <div
      class="gato-mascote"
      :style="{ top: `${pos.y}px`, left: `${pos.x}px`, backgroundPosition: spritePosition }"
    ></div>
  </template>
  
  <script>
  import { onMounted, onUnmounted, reactive, ref } from 'vue';
  
  export default {
    name: 'MascoteGato',
    setup() {
      const pos = reactive({ x: 200, y: 200 });
      const last = reactive({ x: 200, y: 200 });
      const spritePosition = ref('0px 0px');
  
      function getDirection(dx, dy) {
        const angle = Math.atan2(dy, dx) * (180 / Math.PI);
  
        if (angle >= -30 && angle < 30) return 'direita';
        if (angle >= 30 && angle < 75) return 'cima-direita';
        if (angle >= 75 && angle < 105) return 'cima';
        if (angle >= 105 && angle < 150) return 'cima-esquerda'; // usaremos esquerda
        if (angle >= 150 || angle < -150) return 'esquerda';
        if (angle >= -150 && angle < -105) return 'baixo-esquerda';
        if (angle >= -105 && angle < -75) return 'baixo';
        if (angle >= -75 && angle < -30) return 'baixo-direita'; // usaremos direita
        return 'baixo';
      }
  
      function updateSprite(dx, dy) {
        const dir = getDirection(dx, dy);
  
        switch (dir) {
          case 'cima':
            spritePosition.value = '0px 0px';
            break;
          case 'cima-direita':
            spritePosition.value = '-128px 0px';
            break;
          case 'direita':
          case 'baixo-direita':
            spritePosition.value = '-256px 0px';
            break;
          case 'baixo':
            spritePosition.value = '0px -128px';
            break;
          case 'baixo-esquerda':
            spritePosition.value = '-128px -128px';
            break;
          case 'esquerda':
          case 'cima-esquerda':
            spritePosition.value = '-256px -128px';
            break;
          default:
            spritePosition.value = '0px 0px';
        }
      }
  
      function handleMove(e) {
        const dx = e.clientX - last.x;
        const dy = e.clientY - last.y;
        updateSprite(dx, dy);
        last.x = e.clientX;
        last.y = e.clientY;
  
        pos.x = e.clientX;
        pos.y = e.clientY;
      }
  
      onMounted(() => {
        window.addEventListener('mousemove', handleMove);
      });
  
      onUnmounted(() => {
        window.removeEventListener('mousemove', handleMove);
      });
  
      return { pos, spritePosition };
    },
  };
  </script>
  
  <style scoped>
  .gato-mascote {
    position: absolute;
    width: 128px;
    height: 128px;
    background-image: url('/gato.png');
    background-size: 384px 256px; /* 3 colunas x 2 linhas */
    z-index: 20;
    pointer-events: none;
    transition: top 0.15s linear, left 0.15s linear;
  }
  </style>
  