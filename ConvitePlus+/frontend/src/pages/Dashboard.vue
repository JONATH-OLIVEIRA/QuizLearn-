<template>
  <div class="container mt-4">
    <header class="d-flex justify-content-between align-items-center bg-dark text-white p-3 rounded">
      <h1 class="h3">Painel de Controle</h1>
      <button @click="logout" class="btn btn-outline-light">Sair</button>
    </header>

    <section class="mt-4">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <!-- Exibe "Seus Eventos" com o Nome do Anfitrião -->
        <h2 class="h5">
          Seus Eventos<span v-if="nomeAnfitriao"> - {{ nomeAnfitriao }}</span>
        </h2>
        <div>
          <button @click="criarNovoEvento" class="btn btn-primary me-2">Criar Novo Evento</button>
          <button @click="dadosPessoais" class="btn btn-info">Dados Pessoais</button>
        </div>
      </div>

      <!-- Lista de Eventos -->
      <ul class="list-group">
        <li v-for="evento in eventos" :key="evento.id" class="list-group-item">
          <strong>{{ evento.nomeEvento }}</strong>
          <div class="mt-2">
            <button @click="gerarLinkConvite(evento.codigoConvite)" class="btn btn-secondary btn-sm me-2">
              Gerar Link de Convite
            </button>
            <button @click="editarEvento(evento.id)" class="btn btn-warning btn-sm me-2">
              Editar
            </button>
            <button @click="verResumo(evento.id)" class="btn btn-info btn-sm me-2">
              Resumo do Evento
            </button>
            <button @click="deletarEvento(evento.id)" class="btn btn-danger btn-sm">
              Excluir
            </button>
          </div>
        </li>
      </ul>

      <p v-if="eventos.length === 0" class="mt-3">Nenhum evento cadastrado.</p>
    </section>
  </div>
</template>

<script>
import { ref, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import api from '@/services/api';
import { AuthService } from '@/services/authService';

export default {
  setup() {
    const router = useRouter();
    const route = useRoute();

    const eventos = ref([]);
    const user = ref(AuthService.getUser());
    const anfitriaoId = ref(user.value?.anfitriao?.id || null);
    const nomeAnfitriao = ref(user.value?.anfitriao?.nome || ""); // Nome do anfitrião

    const carregarEventos = async () => {
      if (!anfitriaoId.value) {
        console.warn("Anfitrião não definido, não é possível carregar eventos.");
        return;
      }

      try {
        const response = await api.get(`/anfitrioes/${anfitriaoId.value}/eventos`);
        eventos.value = response.data;
        AuthService.saveUser({ ...user.value, eventos: response.data });
      } catch (error) {
        console.error("Erro ao buscar eventos:", error);
      }
    };

    const criarNovoEvento = () => {
      if (!anfitriaoId.value) {
        alert("Erro: anfitrião não definido.");
        return;
      }
      router.push(`/evento/novo?anfitriaoId=${anfitriaoId.value}`);
    };

    const editarEvento = (eventoId) => router.push(`/evento/editar/${eventoId}`);

    const verResumo = (eventoId) => router.push(`/evento/resumo-anfitriao?eventoId=${eventoId}`);

    const dadosPessoais = () => {
      if (!anfitriaoId.value) {
        alert("Erro: anfitrião não definido.");
        return;
      }
      router.push(`/anfitrioes/dados-pessoais?anfitriaoId=${anfitriaoId.value}`);
    };

    const deletarEvento = async (eventoId) => {
      if (confirm("Tem certeza que deseja excluir este evento?")) {
        try {
          await api.delete(`/eventos/${eventoId}`);
          eventos.value = eventos.value.filter(evento => evento.id !== eventoId);
          AuthService.saveUser({ ...user.value, eventos: eventos.value });
        } catch (error) {
          console.error("Erro ao excluir evento:", error);
        }
      }
    };

    const gerarLinkConvite = (codigoConvite) => {
      if (!codigoConvite) return alert("Código de convite não disponível.");
      const link = `${window.location.origin}/convites/${codigoConvite}`;
      navigator.clipboard.writeText(link)
        .then(() => alert("Link copiado: " + link))
        .catch((err) => console.error("Erro ao copiar link:", err));
    };

    const logout = () => {
      AuthService.logout();
      router.push("/");
    };

    onMounted(() => {
      user.value = AuthService.getUser();
      anfitriaoId.value = user.value?.anfitriao?.id;
      nomeAnfitriao.value = user.value?.anfitriao?.nome || ""; // Atualiza o nome ao montar

      if (anfitriaoId.value) {
        carregarEventos();
      } else {
        console.warn("Usuário não autenticado ou anfitrião ausente.");
      }
    });

    watch(
      () => route.fullPath,
      (newPath) => {
        if (newPath.includes('/dashboard')) {
          user.value = AuthService.getUser();
          anfitriaoId.value = user.value?.anfitriao?.id;
          nomeAnfitriao.value = user.value?.anfitriao?.nome || ""; // Atualiza o nome ao mudar a rota
          if (anfitriaoId.value) {
            carregarEventos();
          }
        }
      }
    );

    return {
      eventos,
      nomeAnfitriao,
      criarNovoEvento,
      editarEvento,
      verResumo,
      dadosPessoais,
      deletarEvento,
      gerarLinkConvite,
      logout,
    };
  }
};
</script>
