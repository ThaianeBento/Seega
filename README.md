# Regras do Jogo: Seega

## Componentes
- 1 tabuleiro com grade 5x5 (25 casas).
- 12 peças brancas para um jogador.
- 12 peças pretas para o outro jogador.

---

## Objetivo
Capturar todas as peças inimigas ou, caso o jogo chegue ao limite de 20 jogadas consecutivas sem capturas, vencer por possuir maior número de peças no tabuleiro.

---

## Fase 1 – Colocação das peças
1. Os jogadores decidem quem começa.
2. Alternadamente, cada jogador coloca duas peças no tabuleiro, em casas livres.
3. O quadrado central deve ficar vazio nesta fase.
4. Não há capturas durante a colocação.
5. Quando todas as peças forem posicionadas, começa a fase de movimento.
6. O segundo jogador será o primeiro a jogar na fase seguinte.

---

## Fase 2 – Movimento e captura
1. Os jogadores movem alternadamente uma de suas peças.
2. Uma peça pode ser movida apenas ortogonalmente (para cima, baixo, esquerda ou direita) e apenas para uma casa vazia adjacente.
3. Captura por custódia:
    - Uma peça adversária é capturada quando fica entre duas peças do jogador (na vertical ou horizontal).
    - Exemplo: X O X → o "O" é capturado.
4. Peças no quadrado central não podem ser capturadas.
5. Sempre que um jogador capturar pelo menos uma peça, ele tem direito a jogar novamente.
6. Se um jogador não puder mover nenhuma peça, ele passa a vez até que um movimento válido seja possível.

---

## Fim de jogo
- Um jogador vence se capturar todas as peças do adversário.
- Se durante 20 jogadas consecutivas** não houver capturas, o jogo termina imediatamente.
    - O vencedor será aquele com mais peças no tabuleiro no momento.  

---

# Configurações

## Tecnologias

- Java 22
- H2 
- JPA
- ***INTERFACE***

## Build
