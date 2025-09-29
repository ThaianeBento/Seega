package model;

public class Jogo {
    // ======== Atributos ========
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

    // ======== Construtor ========
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

    // ======== Getters ========
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

    // ======== Setters ========
    public void setEstado(Estado estado) { this.estado = estado; }
    public void setJogadorAtual(Jogador jogador) { this.jogadorAtual = jogador; }
    public void setPecasColocadas(int pecasColocadas) { this.pecasColocadas = pecasColocadas; }
    public void setPecasTurno(int pecasTurno) { this.pecasTurno = pecasTurno; }
    public void setJogadasSemCaptura(int jogadasSemCaptura) { this.jogadasSemCaptura = jogadasSemCaptura; }
    
    public void setSelecionada(int linha, int coluna) {
        this.selecionadaLinha = linha;
        this.selecionadaColuna = coluna;
    }
    
    public void limparSelecao() {
        this.selecionadaLinha = -1;
        this.selecionadaColuna = -1;
    }

    // ======== Operações básicas do tabuleiro ========
    public Peca getPeca(int linha, int coluna) {
        if (!posicaoValida(linha, coluna)) return null;
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
    
    private boolean posicaoValida(int linha, int coluna) {
        return linha >= 0 && linha < 5 && coluna >= 0 && coluna < 5;
    }

    // ======== Reset ========
    public void reset() {
        inicializar();
    }
}