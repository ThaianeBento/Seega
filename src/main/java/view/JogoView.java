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
    private JLabel lblRodadas;
    private JLabel lblPecasJog1;
    private JLabel lblPecasJog2;


    public JogoView(Jogo jogo, JogoController controller) {
        this.jogo = jogo;
        this.controller = controller;
        this.jogo.adicionarObserver(this);

        setTitle("Seega");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        // ===== MENU SUPERIOR =====
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opções");

        JMenuItem regrasItem = new JMenuItem("Regras");
        regrasItem.addActionListener(e -> mostrarRegras());

        JMenuItem reiniciarItem = new JMenuItem("Reiniciar Jogo");
        reiniciarItem.addActionListener(e -> jogo.reset());

        menu.add(regrasItem);
        menu.add(reiniciarItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

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

        // ===== STATUS INFERIOR =====
        JPanel statusPainel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        lblRodadas = new JLabel("Rodadas sem captura: 0");
        lblPecasJog1 = new JLabel("Peças Jogador 1: 0");
        lblPecasJog2 = new JLabel("Peças Jogador 2: 0");

        statusPainel.add(lblRodadas);
        statusPainel.add(lblPecasJog1);
        statusPainel.add(lblPecasJog2);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(painel, BorderLayout.CENTER);
        mainPanel.add(statusPainel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

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


        lblRodadas.setText("Rodadas sem captura: " + jogo.getRodadasSemCaptura());
        lblPecasJog1.setText("Peças Jogador 1: " + contarPecas(jogo.getJogador1()));
        lblPecasJog2.setText("Peças Jogador 2: " + contarPecas(jogo.getJogador2()));

        setTitle("Seega - Jogador " + jogo.getJogadorAtual().getId() +
                " | Estado: " + jogo.getEstado());
    }

    @Override
    public void exibirMensagem(String texto) {
        javax.swing.JOptionPane.showMessageDialog(null, texto);
    }

    private int contarPecas(model.Jogador jogador) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (jogo.getTabuleiro()[i][j] != null &&
                        jogo.getTabuleiro()[i][j].getDono() == jogador) {
                    count++;
                }
            }
        }
        return count;
    }

    private void mostrarRegras() {
        String regras = """
        REGRAS DO SEEGA:
        - Fase 1: Jogadores colocam 2 peças por turno (exceto no centro).
        - Fase 2: Jogadores movem peças adjacentes.
        - Captura ocorre quando peça inimiga fica entre duas peças suas.
        - Jogo termina quando um jogador perde todas as peças
          ou após muitas jogadas sem captura.
        """;
        JOptionPane.showMessageDialog(this, regras, "Regras", JOptionPane.INFORMATION_MESSAGE);
    }

}
