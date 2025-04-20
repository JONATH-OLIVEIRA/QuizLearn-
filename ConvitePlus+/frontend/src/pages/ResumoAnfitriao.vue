<template>
    <div class="container mt-5">
        <div class="mb-4 text-center">
            <h2>{{ evento.nomeEvento || 'Nome do Evento' }}</h2>
            <p class="text-muted">{{ evento.endereco || 'Endereço do Evento' }}</p>
            <p class="text-muted">
                <strong>Data e Hora: </strong>{{ evento.dataEvento || 'Data não informada' }} às {{ evento.horaEvento ||
                'Hora não informada' }}
            </p>

            <!-- Exibindo a foto do evento com as dimensões definidas -->
            <div v-if="evento.fotoUrl" class="mt-3">
                <img :src="evento.fotoUrl" alt="Foto do Evento" class="img-fluid"
                    style="max-width: 300px; height: auto;">
            </div>
            <div v-else class="mt-3">
                <p class="text-muted">Foto do evento não disponível.</p>
            </div>
        </div>

        <div class="row text-center mb-4">
            <div class="col-md-4 mb-3">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title text-success">✅ Confirmados</h5>
                        <p class="card-text fs-4">{{ confirmados.length }}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title text-warning">⏳ Aguardando</h5>
                        <p class="card-text fs-4">{{ aguardando.length }}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title text-danger">❌ Recusaram</h5>
                        <p class="card-text fs-4">{{ recusados.length }}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Atualizado: Lista de Presentes -->
        <div class="mt-5">
            <h4>🎁 Lista de Presentes</h4>
            <div v-if="presentes.length === 0" class="text-muted">Nenhum presente cadastrado.</div>
            <ul v-else class="list-group mt-3">
                <li v-for="presente in presentes" :key="presente.id"
                    class="list-group-item d-flex justify-content-between align-items-center">
                    <div>
                        <strong>{{ presente.nome }}</strong>

                    </div>
                </li>
            </ul>
        </div>

        <div class="mt-5">
            <h4>✅ Convidados Confirmados</h4>
            <div v-if="confirmados.length === 0" class="text-muted">Nenhum convidado confirmou presença ainda.</div>
            <ul v-else class="list-group mt-3">
                <li v-for="convidado in confirmados" :key="convidado.id" class="list-group-item">
                    {{ convidado.nome }}
                </li>
            </ul>
        </div>

        <div class="mt-5 text-center">
            <router-link to="/dashboard" class="btn btn-secondary">
                ← Voltar ao Dashboard
            </router-link>
        </div>
    </div>
</template>
<script>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useRoute } from 'vue-router';
import api from '@/services/api';
import eventBus from '@/utils/eventBus';

export default {
    setup() {
        const route = useRoute();
        const evento = ref({});
        const convidados = ref([]);
        const presentes = ref([]); // Lista de presentes (atualizada)

        const confirmados = ref([]);
        const aguardando = ref([]);
        const recusados = ref([]);

        const carregarResumo = async () => {
            const eventoId = route.query.eventoId || route.params.eventoId;

            if (!eventoId) {
                alert("ID do evento não fornecido.");
                return;
            }

            try {
                const res = await api.get(`/eventos/${eventoId}`);
                const dados = res.data;

                console.log("Dados recebidos do evento:", dados);

                // Formatar a data no formato dia/mês/ano
                const dataEventoFormatada = new Intl.DateTimeFormat('pt-BR').format(new Date(dados.dataEvento));

                // Formatar a hora para exibir apenas horas e minutos (HH:mm)
                const horaFormatada = new Intl.DateTimeFormat('pt-BR', {
                    hour: '2-digit',
                    minute: '2-digit',
                }).format(new Date(`1970-01-01T${dados.horaEvento}`));

                evento.value = {
                    id: dados.id,
                    nomeEvento: dados.nomeEvento,
                    endereco: dados.enderecoEvento,
                    dataEvento: dataEventoFormatada,
                    horaEvento: horaFormatada, // Substitui dados.horaEvento pela hora formatada
                    fotoUrl: dados.imagemUrl || ''
                };

                console.log("Foto URL do evento:", evento.value.fotoUrl);

                // Atualizar a lista de presentes
                presentes.value = [...dados.sugestoesPresentes]; // Atualizando estado reativo com lista de presentes

                console.log("Presentes carregados:", presentes.value);

                // Atualizar reatividade e categorias de convidados
                convidados.value = [...dados.convidados];
                confirmados.value = [...convidados.value.filter(c => c.aceitouConvite === true)];
                recusados.value = [...convidados.value.filter(c => c.aceitouConvite === false)];
                aguardando.value = [...convidados.value.filter(c => c.aceitouConvite == null || c.aceitouConvite === undefined)];

                console.log("Convidados categorizados:", {
                    confirmados: confirmados.value,
                    recusados: recusados.value,
                    aguardando: aguardando.value
                });
            } catch (error) {
                console.error("Erro ao carregar dados do evento:", error);
                alert("Erro ao carregar dados do evento.");
            }
        };


        onMounted(() => {
            carregarResumo();
            eventBus.on('respostaConvite', carregarResumo);
        });

        onBeforeUnmount(() => {
            eventBus.off('respostaConvite', carregarResumo);
        });

        return {
            evento,
            presentes,
            confirmados,
            aguardando,
            recusados
        };
    }
};
</script>
