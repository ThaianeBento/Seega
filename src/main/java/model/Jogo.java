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

        if (pecasTurno == 2) {
            pecasTurno = 0;
            mudarTurno();
        }

        if (pecasColocadas == 24) {
            estado = Estado.JOGANDO;
            jogadorAtual = jogador2;
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

        if (!houveCaptura) {
            mudarTurno();
        }

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
                        tabuleiro[li][co] = null;
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
}
