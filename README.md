# Seega

Implementação em Java do Seega, jogo de tabuleiro estratégico. O projeto reproduz o tabuleiro 5x5, as fases de posicionamento e movimentação/captura e oferece uma interface gráfica em Swing.

## Visão Geral
- Duas fases distintas: colocação de 24 peças (12 por jogador) seguida da fase de movimento com capturas por custódia.
- Tabuleiro 5x5 representado por uma matriz simples, com a casa central bloqueada na primeira fase.
- Interface Swing com feedback visual para colocação, movimento, capturas e mudanças de turno.
- Arquitetura orientada a objetos empregando os padrões MVC e Observer para manter responsabilidades isoladas e facilitar extensões.

## Regras do Jogo
### Fase 1 – Posicionamento
- Os jogadores alternam turnos posicionando duas peças por rodada em casas vazias.
- A casa central (2,2) permanece bloqueada durante todo o posicionamento.
- Não ocorrem capturas nesta fase; após a 24ª peça o jogo transita automaticamente para a fase de movimentação e o segundo jogador inicia.

### Fase 2 – Movimentação e Capturas
- Cada turno começa com a seleção de uma peça do jogador ativo, seguida pelo movimento ortogonal para uma casa adjacente vazia.
- Captura por sanduíche: sempre que uma peça adversária fica entre duas peças do jogador (vertical ou horizontalmente), ela é removida do tabuleiro.
- Após capturar pelo menos uma peça o jogador mantém o turno; caso contrário o controle é passado ao adversário e o contador de jogadas sem captura é incrementado.

### Condições de Vitória
- Um jogador vence ao eliminar todas as peças adversárias.
- Vitória por tempo: ao atingir 20 turnos consecutivos sem capturas, vence quem mantém mais peças no tabuleiro; empate se a quantidade for igual.

## Arquitetura e Padrões
### MVC
- **Model**: classes de domínio `Jogo`, `Jogador`, `Peca` e `Estado` encapsulam estado do tabuleiro, jogadores e fluxo da partida.
- **Controller**: centraliza regras, validações e transições de fase/turno. 
- **View**: interface Swing responsável pela renderização do tabuleiro, animações e mensagens aos usuários.

### Observer com Publicador
- O controller publica eventos através de um `Publicador`, evitando que a lógica dependa diretamente da view.
- `Publicador` mantém a lista de observers e disponibiliza métodos específicos.
- `JogoView` implementa `Observer` e reage aos callbacks para sincronizar a UI (desenho das peças, labels, animações, diálogos).
- Extensibilidade: novos observers (por exemplo, um `HubObserver` para integrar placares ou logs em um hub de jogos) podem ser registrados sem alterar controller ou view, seguindo os princípios Open/Closed e Inversão de Dependência.

## Fluxos Principais
- **Posicionamento**: `JogoController.colocarPeca` valida coordenadas, instancia a peça, atualiza contadores e dispara `notificarPecaColocada`. Ao atingir 24 peças chama `notificarEstadoMudou` para iniciar a fase de movimento.
- **Movimentação**: `JogoController.selecionarOuMover` controla seleção e deslocamento da peça; `verificarEExecutarCapturas` detecta o padrão sanduíche nas quatro direções e gera eventos de captura.
- **Gerenciamento de Turno**: o controller decide quando manter turno (após captura) ou alternar jogadores, notifica via `notificarTurnoMudou` e acompanha o contador de rodadas sem captura.

## Executando o Projeto
1. Certifique-se de ter o JDK 22 instalado.
2. Compile o projeto: `mvn clean package`
3. Execute o jogo: `java -cp target/Seega-1.0-SNAPSHOT.jar app.Main`

## Estrutura de Pastas
- `src/main/java/app` – ponto de entrada (`Main`).
- `src/main/java/controller` – controllers e regras de jogo.
- `src/main/java/model` – entidades de domínio e estado do jogo.
- `src/main/java/observer` – infraestrutura do padrão Observer (`Observer`, `Publicador`).
- `src/main/java/view` – interface Swing.



