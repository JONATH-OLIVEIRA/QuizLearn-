<template>
  <div class="home-container">
    <!-- Balões animados -->
    <div class="balao balao1">🎈</div>
    <div class="balao balao2">🎈</div>
    <div class="balao balao3">🎈</div>
    <div class="balao balao4">🎈</div>

    <!-- Gato correndo -->
    <div class="gato-correndo"></div>

    <!-- Conteúdo principal -->
    <div class="container text-white mt-5">
      <div class="row justify-content-center">
        <div class="col-md-8">
          <div class="card bg-dark bg-opacity-75 shadow-lg p-4 text-center rounded-4">
            <h1 class="mb-4 text-white glow">🎉 Bem-vindo ao <span class="text-primary">ConvitePlus+</span></h1>
            <p class="lead mb-4 text-white">Organize eventos e gerencie convidados com facilidade!</p>

            <div class="d-grid gap-3">
              <!-- Login de anfitrião -->
              <router-link to="/login" class="btn btn-outline-light btn-lg btn-icon">
                Sou Anfitrião
              </router-link>

              <!-- Cadastro de anfitrião -->
              <router-link to="/cadastro-anfitriao" class="btn btn-light btn-lg btn-icon">
                Quero me cadastrar
              </router-link>

              <!-- Login de convidado -->
              <router-link to="/login-convidado" class="btn btn-success btn-lg btn-icon">
                Sou Convidado
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <footer class="footer">
  <div class="container d-flex justify-content-center align-items-center py-3 flex-wrap">
    <p class="mx-3 mb-0">Desenvolvido por: <strong>JOS TECNOLOGIA & ANZHELIKA PURGINA</strong></p>
    <p class="mx-3 mb-0">Fale conosco: <a href="mailto:contato@jostecnologia.com" class="text-light">contato@jostecnologia.com</a></p>
    <p class="mx-3 mb-0">© 2025 Todos os direitos reservados</p>
  </div>
</footer>


</template>

<script>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { AuthService } from '@/services/authService';

export default {
  name: 'Home',
  setup() {
    const router = useRouter();

    onMounted(() => {
      const user = AuthService.getUser();
      if (user && user.anfitriao) {
        router.push("/dashboard");
      }
    });

    return {};
  }
};
</script>

<style scoped>
.home-container {
  height: 100vh;
  background-image: url('/free-png.webp');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  position: relative;
  padding: 2rem;
}

/* Balões animados 🎈 */
.balao {
  position: absolute;
  font-size: 2.5rem;
  opacity: 0.8;
  animation: subir 10s linear infinite;
  pointer-events: none;
}

.balao1 {
  left: 10%;
  animation-delay: 0s;
}

.balao2 {
  left: 30%;
  animation-delay: 2s;
}

.balao3 {
  left: 60%;
  animation-delay: 4s;
}

.balao4 {
  left: 80%;
  animation-delay: 6s;
}

@keyframes subir {
  0% {
    bottom: -60px;
    transform: translateX(0) rotate(0deg);
  }

  100% {
    bottom: 100%;
    transform: translateX(-30px) rotate(360deg);
  }
}

/* Gato correndo com sprite */
.gato-correndo {
  position: absolute;
  bottom: 20px;
  width: 682px;
  height: 682px;
  background-image: url('/gato.png');
  background-repeat: no-repeat;
  background-position: 0 0;
  background-size: 2046px 1364px;
  animation: correr 6s linear infinite, animar-gato 0.6s steps(3) infinite;
  z-index: 5;
  transform: scale(0.3);
  transform-origin: bottom left;
}

@keyframes correr {
  0% {
    left: -700px;
  }

  100% {
    left: 100vw;
  }
}

@keyframes animar-gato {
  0% {
    background-position-x: 0;
  }

  100% {
    background-position-x: -2046px;
  }
}

/* Glow effect for heading */
.glow {
  text-shadow: 0 0 5px rgba(255, 255, 255, 0.2), 0 0 10px rgba(0, 0, 255, 0.4);
}

/* Botões com profundidade */
.btn-icon {
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}

.btn-icon:hover {
  transform: scale(1.05); /* Amplia levemente o botão */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.4); /* Adiciona sombra */
}

.footer {
  position: absolute;
  bottom: 0;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.8);
  color: #fff;
}

.footer a {
  text-decoration: none;
  color: #00bcd4;
}

.footer a:hover {
  text-decoration: underline;
}

</style>
