package model;

import observer.Publicador;

import java.util.List;

public class Jogo extends Publicador {
    private Estado estado;
    private Jogador jogador1;
    private Jogador jogador2;
    private Jogador jogadorAtual;
    private Peca[][] tabuleiro;
    private int pecasColocadas;
    private int pecasTurno;
    private int jogadasSemCaptura = 0;

    private int selecionadaLinha = -1;
    private int selecionadaColuna = -1;

    public Jogo() {
        this.estado = Estado.POSICIONANDO;
        this.tabuleiro = new Peca[5][5];
        this.jogador1 = new Jogador(1);
        this.jogador2 = new Jogador(2);
        this.jogadorAtual = jogador1;
        this.pecasColocadas = 0;
        this.pecasTurno = 0;
    }

    public Estado getEstado() { return estado; }
    public Jogador getJogadorAtual() { return jogadorAtual; }
    public Peca[][] getTabuleiro() { return tabuleiro; }

    public void mudarTurno() {
        jogadorAtual = (jogadorAtual == jogador1) ? jogador2 : jogador1;

        if (estado == Estado.JOGANDO) {
            if (!temMovimentoPossivel(jogadorAtual)) {
                Jogador outro = (jogadorAtual == jogador1) ? jogador2 : jogador1;

                if (!temMovimentoPossivel(outro)) {
                    fimDeJogo("Nenhum jogador tem movimentos possíveis. Encerrando jogo.");
                    return;
                }

                notificarMensagem("Jogador " + jogadorAtual.getId() + " não tem movimentos possíveis. Passa a vez!");
                jogadorAtual = outro;

            }
        }

        notificar(); // avisa a view que mudou
    }

    public boolean colocarPeca(int linha, int coluna) {
        if (estado != Estado.POSICIONANDO) return false;

        if (linha == 2 && coluna == 2) return false; //CENTRAL
        if (tabuleiro[linha][coluna] != null) return false;

        Peca peca = new Peca(jogadorAtual);
        tabuleiro[linha][coluna] = peca;
        jogadorAtual.getPecas().add(peca);
        pecasColocadas++;
        pecasTurno++;

        if (pecasColocadas == 24) {
            estado = Estado.JOGANDO;
            jogadorAtual = jogador2; // segundo jogador começa na fase de movimento
            pecasTurno = 0;

            if (!temMovimentoPossivel(jogadorAtual)) {
                notificarMensagem("Jogador " + jogadorAtual.getId() + " não tem movimentos possíveis. Passa a vez!");
                jogadorAtual = jogador1;

                if (!temMovimentoPossivel(jogadorAtual)) {
                    fimDeJogo("Nenhum jogador tem movimentos possíveis. Encerrando jogo.");
                }
            }

            notificar();
            return true;
        }

        if (pecasTurno == 2) {
            pecasTurno = 0;
            mudarTurno();
        }

        notificar();
        return true;
    }

    public boolean selecionarOuMover(int linha, int coluna) {
        if (estado != Estado.JOGANDO) return false;

        if (selecionadaLinha == -1 && tabuleiro[linha][coluna] != null) {
            if (tabuleiro[linha][coluna].getDono() == jogadorAtual) {
                selecionadaLinha = linha;
                selecionadaColuna = coluna;
                notificar();
                return true;
            }
            return false;
        }

        if (selecionadaLinha != -1) {
            boolean ok = moverPeca(linha, coluna);
            selecionadaLinha = -1;
            selecionadaColuna = -1;
            notificar();
            return ok;
        }

        return false;
    }

    private boolean moverPeca(int novaLinha, int novaColuna) {
        if (tabuleiro[novaLinha][novaColuna] != null) return false;

        if (Math.abs(novaLinha - selecionadaLinha) + Math.abs(novaColuna - selecionadaColuna) != 1) {
            return false;
        }

        Peca peca = tabuleiro[selecionadaLinha][selecionadaColuna];
        tabuleiro[selecionadaLinha][selecionadaColuna] = null;
        tabuleiro[novaLinha][novaColuna] = peca;

        boolean houveCaptura = capturar(novaLinha, novaColuna, peca.getDono());

        if (houveCaptura) {
            jogadasSemCaptura = 0;
        } else {
            jogadasSemCaptura++;
            mudarTurno();
        }

        verificarFimDeJogo();
        return true;
    }

    private boolean capturar(int linha, int coluna, Jogador dono) {
        boolean capturou = false;
        List<int[]> direcoes = List.of(
                new int[]{1,0}, new int[]{-1,0},
                new int[]{0,1}, new int[]{0,-1}
        );

        for (int[] d : direcoes) {
            int li = linha + d[0];
            int co = coluna + d[1];
            int l2 = linha + 2*d[0];
            int c2 = coluna + 2*d[1];

            if (estaNoTabuleiro(l2, c2) &&
                    tabuleiro[li][co] != null &&
                    tabuleiro[l2][c2] != null) {

                if (tabuleiro[li][co].getDono() != dono &&
                        tabuleiro[l2][c2].getDono() == dono) {
                    if (!(li == 2 && co == 2)) {
                        Peca pecaCapturada = tabuleiro[li][co];

                        tabuleiro[li][co] = null;

                        if (pecaCapturada != null && pecaCapturada.getDono() != null) {
                            pecaCapturada.getDono().getPecas().remove(pecaCapturada);
                        }

                        capturou = true;
                    }
                }
            }
        }
        return capturou;
    }

    private boolean estaNoTabuleiro(int i, int j) {
        return i >= 0 && i < 5 && j >= 0 && j < 5;
    }

    public int getSelecionadaLinha() { return selecionadaLinha; }
    public int getSelecionadaColuna() { return selecionadaColuna; }

    private void verificarFimDeJogo() {
        if (jogador1.getPecas().isEmpty()) {
            fimDeJogo("Jogador 2 venceu! Todas as peças do Jogador 1 foram capturadas.");
        } else if (jogador2.getPecas().isEmpty()) {
            fimDeJogo("Jogador 1 venceu! Todas as peças do Jogador 2 foram capturadas.");
        }

        if (jogadasSemCaptura >= 20) {
            int p1 = contarPecas(jogador1);
            int p2 = contarPecas(jogador2);

            if (p1 > p2) {
                fimDeJogo("Jogador 1 venceu por maioria de peças!");
            } else if (p2 > p1) {
                fimDeJogo("Jogador 2 venceu por maioria de peças!");
            } else {
                fimDeJogo("Empate! Ambos os jogadores têm o mesmo número de peças.");
            }
        }
    }

    private void fimDeJogo(String mensagem) {
        estado = Estado.FIM; // jogo encerrado
        notificarMensagem(mensagem);
        notificar();
    }

    private int contarPecas(Jogador jogador) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tabuleiro[i][j] != null && tabuleiro[i][j].getDono() == jogador) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean temMovimentoPossivel(Jogador jogador) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tabuleiro[i][j] != null && tabuleiro[i][j].getDono() == jogador) {
                    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
                    for (int[] d : dirs) {
                        int ni = i + d[0];
                        int nj = j + d[1];
                        if (estaNoTabuleiro(ni,nj) && tabuleiro[ni][nj] == null) {
                            return true; // existe pelo menos um movimento
                        }
                    }
                }
            }
        }
        return false;
    }

    public void reset() {
        this.estado = Estado.POSICIONANDO;
        this.tabuleiro = new Peca[5][5];
        this.jogador1 = new Jogador(1);
        this.jogador2 = new Jogador(2);
        this.jogadorAtual = jogador1;
        this.pecasColocadas = 0;
        this.pecasTurno = 0;
        this.jogadasSemCaptura = 0;
        this.selecionadaLinha = -1;
        this.selecionadaColuna = -1;
        notificar();
    }

    public int getRodadasSemCaptura() {return this.jogadasSemCaptura;    }

    public Jogador getJogador1() { return this.jogador1;    }
    public Jogador getJogador2() { return this.jogador2;    }
}
