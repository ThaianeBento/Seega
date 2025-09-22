package model;

import observer.Publicador;
import observer.Observer;


public class Jogo {
   
    private Estado estado;
    private Jogador jogador1;
    private Jogador jogador2;
    private Jogador jogadorAtual;
    private Peca[][] tabuleiro;
    private int pecasColocadas;
    private int pecasTurno;
    private int jogadasSemCaptura;
    private int selecionadaLinha = -1;
    private int selecionadaColuna = -1;
    
   
    private final Publicador publicador = new Publicador();

   
    
    public Jogo() {
        inicializar();
    }
    
    private void inicializar() {
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
    }

    
    
    public void adicionarObserver(Observer observer) {
        publicador.adicionarObserver(observer);
    }
    
    public void removerObserver(Observer observer) {
        publicador.removerObserver(observer);
    }
    
    public Publicador getPublicador() {
        return publicador;
    }

 
    
    public Estado getEstado() { return estado; }
    public Jogador getJogadorAtual() { return jogadorAtual; }
    public Jogador getJogador1() { return jogador1; }
    public Jogador getJogador2() { return jogador2; }
    public Peca[][] getTabuleiro() { return tabuleiro; }
    public int getPecasColocadas() { return pecasColocadas; }
    public int getPecasTurno() { return pecasTurno; }
    public int getJogadasSemCaptura() { return jogadasSemCaptura; }
    public int getSelecionadaLinha() { return selecionadaLinha; }
    public int getSelecionadaColuna() { return selecionadaColuna; }

   
    
    public Peca getPeca(int linha, int coluna) {
        if (!posicaoValida(linha, coluna)) {
            return null;
        }
        return tabuleiro[linha][coluna];
    }
    
    public void setPeca(int linha, int coluna, Peca peca) {
        if (posicaoValida(linha, coluna)) {
            tabuleiro[linha][coluna] = peca;
        }
    }
    
    public void removerPeca(int linha, int coluna) {
        if (posicaoValida(linha, coluna)) {
            tabuleiro[linha][coluna] = null;
        }
    }
    
    public boolean posicaoValida(int linha, int coluna) {
        return linha >= 0 && linha < 5 && coluna >= 0 && coluna < 5;
    }
    
    public boolean posicaoVazia(int linha, int coluna) {
        // CORREÇÃO: Posição vazia significa não ter PEÇA, independente se é casa central
        return posicaoValida(linha, coluna) && tabuleiro[linha][coluna] == null;
    }
    
    public boolean isCasaCentral(int linha, int coluna) {
        return linha == 2 && coluna == 2;
    }

   
    
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public void setJogadorAtual(Jogador jogador) {
        this.jogadorAtual = jogador;
    }
    
    public void setPecasColocadas(int pecasColocadas) {
        this.pecasColocadas = pecasColocadas;
    }
    
    public void setPecasTurno(int pecasTurno) {
        this.pecasTurno = pecasTurno;
    }
    
    public void setJogadasSemCaptura(int jogadasSemCaptura) {
        this.jogadasSemCaptura = jogadasSemCaptura;
    }
    
    public void setSelecionada(int linha, int coluna) {
        this.selecionadaLinha = linha;
        this.selecionadaColuna = coluna;
    }
    
    public void limparSelecao() {
        this.selecionadaLinha = -1;
        this.selecionadaColuna = -1;
    }

   
    
    public void incrementarPecasColocadas() {
        this.pecasColocadas++;
    }
    
    public void incrementarPecasTurno() {
        this.pecasTurno++;
    }
    
    public void incrementarJogadasSemCaptura() {
        this.jogadasSemCaptura++;
    }
    
    public void zerarJogadasSemCaptura() {
        this.jogadasSemCaptura = 0;
    }
    
    public void zerarPecasTurno() {
        this.pecasTurno = 0;
    }

   
    
    public Jogador getOutroJogador() {
        return (jogadorAtual == jogador1) ? jogador2 : jogador1;
    }
    
    public int contarPecasNoTabuleiro(Jogador jogador) {
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
    
    public boolean temPecaDoJogador(Jogador jogador, int linha, int coluna) {
        if (!posicaoValida(linha, coluna) || tabuleiro[linha][coluna] == null) {
            return false;
        }
        return tabuleiro[linha][coluna].getDono() == jogador;
    }
    
    public java.util.List<int[]> getPosicoesDasPecas(Jogador jogador) {
        java.util.List<int[]> posicoes = new java.util.ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (temPecaDoJogador(jogador, i, j)) {
                    posicoes.add(new int[]{i, j});
                }
            }
        }
        return posicoes;
    }

    
    
    public void reset() {
        inicializar();
    }
}