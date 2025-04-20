import api from "./api";

export const AuthService = {
  async login(email, senha) {
    try {
      const response = await api.post("/anfitrioes/login", { email, senha });

      console.log("Resposta da API:", response.data);

      // Valida a estrutura da resposta e acessa os dados do anfitrião
      const anfitriao = response.data.anfitriao;
      if (!anfitriao || !anfitriao.id) {
        throw new Error("Dados do anfitrião não encontrados.");
      }

      // Valida os eventos do anfitrião
      const eventos = response.data.eventos || []; // Fallback para lista vazia
      if (eventos.length === 0) {
        console.warn("Nenhum evento encontrado para este usuário.");
      }

      // Constrói o objeto user com os dados do anfitrião e eventos
      const user = {
        anfitriao: {
          id: anfitriao.id,
          nome: anfitriao.nome,
          email: anfitriao.email,
        },
        eventos: eventos,
      };

      this.saveUser(user); // Salva o usuário no localStorage

      return user; // Retorna o objeto user para uso no frontend
    } catch (error) {
      console.error("Erro ao fazer login:", error);

      // Diferencia erro de conexão e erro de backend
      if (!error.response) {
        throw new Error("Não foi possível conectar ao servidor.");
      }

      // Retorna o erro com mensagem detalhada do backend
      const message = error.response.data?.message || "Erro ao fazer login.";
      throw new Error(message);
    }


  },

  saveUser(user) {
    // Salva os dados do usuário no localStorage
    localStorage.setItem("user", JSON.stringify(user));
  },

  getUser() {
    // Recupera os dados do usuário do localStorage
    const user = localStorage.getItem("user");
    return user ? JSON.parse(user) : null; // Retorna null caso não exista
  },

  isAuthenticated() {
    // Verifica se o usuário está autenticado com base nos dados do localStorage
    const user = this.getUser();
    return !!(user && user.anfitriao && user.anfitriao.id); // Confirma se existem dados do anfitrião
  },

  logout() {
    // Remove os dados do usuário do localStorage
    localStorage.removeItem("user");
  },
};
