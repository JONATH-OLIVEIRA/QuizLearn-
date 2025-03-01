document.addEventListener('DOMContentLoaded', () => {
    const nameInput = document.getElementById('name');
    const startButton = document.getElementById('startBtn');
    let userName = "";

    // Função para embaralhar um array
    function shuffleArray(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];  // Troca os elementos
        }
    }

    // Verifica se estamos na página inicial
    if (startButton) {
        startButton.addEventListener('click', function() {
            userName = nameInput.value; // Armazenar o nome do usuário
            if (userName) {
                localStorage.setItem('userName', userName); // Salva o nome no localStorage
                window.location.href = 'quiz.html'; // Redireciona para a página do quiz
            } else {
                alert('Por favor, insira seu nome');
            }
        });
    }

    // Verifica se estamos na página do quiz
    if (document.getElementById('question') !== null && document.getElementById('options') !== null) {
        // Recupera o nome do usuário do localStorage
        userName = localStorage.getItem('userName');

        // Carregar as questões a partir de um arquivo JSON
        fetch('assets/questions.json')
            .then(response => response.json())
            .then(data => {
                const questions = data.questions;

                // Embaralhar as perguntas
                shuffleArray(questions);

                // Para cada pergunta, manter o índice da resposta correta antes de embaralhar as opções
                questions.forEach((question) => {
                    const correctAnswer = question.correct;
                    shuffleArray(question.options); // Embaralha as opções
                    question.correct = question.options.indexOf(correctAnswer); // Atualiza o índice correto com base no novo array embaralhado
                });

                let currentQuestion = 0;
                let selectedAnswers = [];

                // Função para renderizar a pergunta e opções
                function renderQuestion() {
                    const questionElement = document.getElementById('question');
                    const optionsElement = document.getElementById('options');
                    const current = questions[currentQuestion];

                    questionElement.innerHTML = current.question;
                    optionsElement.innerHTML = '';
                    current.options.forEach((option, index) => {
                        optionsElement.innerHTML += `
                            <div>
                                <input type="radio" name="answer" id="option${index}" value="${index}" ${selectedAnswers[currentQuestion] === index ? 'checked' : ''}>
                                <label for="option${index}">${option}</label>
                            </div>
                        `;
                    });

                    // Ajustar botões de navegação
                    const nextButton = document.getElementById('nextBtn');
                    const prevButton = document.getElementById('prevBtn');
                    const finishButton = document.getElementById('finishBtn');

                    if (currentQuestion === questions.length - 1) {
                        nextButton.style.display = 'none';  // Esconder o botão "Próxima"
                        finishButton.style.display = 'inline';  // Mostrar o botão "Finalizar Quiz"
                    } else {
                        nextButton.style.display = 'inline';  // Mostrar o botão "Próxima"
                        finishButton.style.display = 'none';  // Esconder o botão "Finalizar Quiz"
                    }

                    // Sempre mostrar o botão "Voltar"
                    prevButton.style.display = 'inline';
                }

                // Função de navegação (próxima/pergunta anterior)
                function navigate(direction) {
                    if (direction === 1 && currentQuestion < questions.length - 1) {
                        currentQuestion++;
                    } else if (direction === -1 && currentQuestion > 0) {
                        currentQuestion--;
                    }
                    renderQuestion();
                }

                // Função para finalizar o quiz
                function finishQuiz() {
                    let score = 0;
                    let result = "";

                    // Loop através de todas as perguntas
                    questions.forEach((question, index) => {
                        // Obter a resposta selecionada da pergunta atual
                        const selectedAnswer = selectedAnswers[index];

                        if (selectedAnswer !== undefined) {
                            if (selectedAnswer === question.correct) {
                                score++;
                            } else {
                                // Exibir explicação para a resposta errada
                                result += `<p>Questão ${index + 1}: Errada! - Resposta correta: ${question.options[question.correct]} - ${question.explanation}</p>`;
                            }
                        } else {
                            // Caso a pergunta não tenha sido respondida, considerar como errada
                            result += `<p>Questão ${index + 1}: Não respondida! - Resposta correta: ${question.options[question.correct]} - ${question.explanation}</p>`;
                        }
                    });

                    // Exibir o nome, a pontuação e as explicações das respostas erradas
                    let finalResult = `
                        <h3>${userName}, sua pontuação foi: ${score} de ${questions.length}</h3>
                        ${result}
                        <button id="restartBtn">Voltar para a página inicial</button>
                    `;

                    document.body.innerHTML = finalResult;

                    // Adicionar evento para o botão "Voltar para a página inicial"
                    document.getElementById('restartBtn').addEventListener('click', function() {
                        localStorage.removeItem('userName');  // Limpa o nome do usuário do localStorage
                        window.location.href = 'index.html';  // Redireciona para a página inicial
                    });
                }

                // Adicionar evento para salvar a resposta selecionada
                document.getElementById('options').addEventListener('change', function(event) {
                    if (event.target.name === 'answer') {
                        selectedAnswers[currentQuestion] = parseInt(event.target.value);
                    }
                });

                renderQuestion();
                document.getElementById('nextBtn').addEventListener('click', () => navigate(1));
                document.getElementById('prevBtn').addEventListener('click', () => navigate(-1));
                document.getElementById('finishBtn').addEventListener('click', finishQuiz);
            })
            .catch(error => {
                console.error('Erro ao carregar o arquivo JSON:', error);
            });
    }
});
