import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "bootstrap/dist/css/bootstrap.min.css";
import $ from "jquery";  // Importando o jQuery
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css';
import 'bootstrap-datepicker';

window.$ = $;  // Tornando o jQuery global para o uso no projeto

const app = createApp(App);
app.use(router);
app.mount("#app");
