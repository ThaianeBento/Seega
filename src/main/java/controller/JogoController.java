package controller;

import model.Jogo;

public class JogoController {
    private Jogo jogo;

    public JogoController(Jogo jogo) {
        this.jogo = jogo;
    }

    public boolean colocarPeca(int linha, int coluna) {
        return jogo.colocarPeca(linha, coluna);
    }

    public boolean selecionarOuMover(int linha, int coluna) {
        return jogo.selecionarOuMover(linha, coluna);
    }

    public Jogo getJogo() { return jogo; }
}
