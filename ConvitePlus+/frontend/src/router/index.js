import { createRouter, createWebHistory } from 'vue-router';
import Home from "../pages/Home.vue";
import Login from "../pages/Login.vue";
import Dashboard from "../pages/Dashboard.vue";
import CriarEvento from "../pages/CriarEvento.vue";
import CadastroPresentes from "../pages/CadastroPresentes.vue"; // Pode remover se não for mais usar
import CadastroAnfitriao from "../pages/CadastroAnfitriao.vue";
import Convite from "../pages/Convite.vue";
import Resumo from "../pages/Resumo.vue"; // Pode manter se ainda for usada em outro lugar
import EditarEvento from "../pages/EditarEvento.vue";
import ResumoConvidado from "../pages/ResumoConvidado.vue";
import LoginConvidado from "../pages/LoginConvidado.vue";
import ResumoAnfitriao from '@/pages/ResumoAnfitriao.vue';
import AnfitriaoDados from "../pages/AnfitriaoDados.vue"; // Nova rota adicionada

const routes = [
  { path: "/", component: Home },
  { path: "/login", component: Login },
  { path: "/dashboard", component: Dashboard },
  { path: "/evento/novo", component: CriarEvento, props: route => ({ anfitriaoId: route.query.anfitriaoId }) },
  { path: "/evento/presentes", component: CadastroPresentes }, // ❓ Pode remover se não for mais usar
  { path: "/cadastro-anfitriao", component: CadastroAnfitriao },
  { path: "/convites/:codigoConvite", component: Convite },
  { path: "/evento/resumo", component: Resumo },
  { path: "/evento/resumo-anfitriao", component: ResumoAnfitriao },
  { path: "/evento/editar/:eventoId", component: EditarEvento, props: true },
  { path: "/convidado/resumo", component: ResumoConvidado },
  { path: "/login-convidado", component: LoginConvidado },
  { path: "/anfitrioes/dados-pessoais", component: AnfitriaoDados, props: route => ({ anfitriaoId: route.query.anfitriaoId }) }, // Rota para dados pessoais

  // 🆕 Página de teste de imagem com Cloudinary
  { path: "/imagem-teste", component: () => import("../pages/ImagemTeste.vue") },
  { path: "/teste-upload", component: () => import("../pages/UploadImagem.vue") },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
