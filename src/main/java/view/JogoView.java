package view;

import controller.JogoController;
import model.Estado;
import model.Jogo;
import model.Peca;
import observer.ObservadorMensagem;
import observer.Observer;

import javax.swing.*;
import java.awt.*;

public class JogoView extends JFrame implements Observer, ObservadorMensagem {
    private Jogo jogo;
    private JogoController controller;
    private JButton[][] botoes;

    public JogoView(Jogo jogo, JogoController controller) {
        this.jogo = jogo;
        this.controller = controller;
        this.jogo.adicionarObserver(this);

        setTitle("Seega");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        JPanel painel = new JPanel(new GridLayout(5, 5));
        botoes = new JButton[5][5];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int linha = i;
                int coluna = j;

                botoes[i][j] = new JButton();
                botoes[i][j].setBackground(Color.LIGHT_GRAY);

                botoes[i][j].addActionListener(e -> {
                    boolean ok;
                    if (jogo.getEstado() == Estado.POSICIONANDO) {
                        ok = controller.colocarPeca(linha, coluna);
                    } else {
                        ok = controller.selecionarOuMover(linha, coluna);
                    }

                    if (!ok) {
                        JOptionPane.showMessageDialog(this, "Movimento inválido!");
                    }
                });


                painel.add(botoes[i][j]);
            }
        }

        add(painel);
        setVisible(true);
    }

    @Override
    public void atualizar() {
        Peca[][] tabuleiro = jogo.getTabuleiro();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tabuleiro[i][j] != null) {
                    int id = tabuleiro[i][j].getDono().getId();
                    botoes[i][j].setBackground(id == 1 ? Color.WHITE : Color.BLACK);
                } else {
                    botoes[i][j].setBackground(Color.LIGHT_GRAY);
                }
            }
        }

        if (jogo.getSelecionadaLinha() != -1) {
            botoes[jogo.getSelecionadaLinha()][jogo.getSelecionadaColuna()]
                    .setBackground(Color.YELLOW);
        }

        setTitle("Seega - Jogador " + jogo.getJogadorAtual().getId() +
                " | Estado: " + jogo.getEstado());
    }

    @Override
    public void exibirMensagem(String texto) {
        javax.swing.JOptionPane.showMessageDialog(null, texto);
    }
}
