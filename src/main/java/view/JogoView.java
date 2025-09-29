package view;

import controller.JogoController;
import model.Estado;
import model.Jogo;
import model.Jogador;
import observer.Observer;

import javax.swing.*;
import java.awt.*;

public class JogoView extends JFrame implements Observer {
    private final JogoController controller;
    private final Jogo jogo;
    
    private JButton[][] botoes;
    private JLabel lblStatus;
    private JLabel lblTurno;
    private JLabel lblPecasJ1;
    private JLabel lblPecasJ2;
    private JLabel lblRodadas;

    public JogoView(Jogo jogo, JogoController controller) {
        this.jogo = jogo;
        this.controller = controller;
        
        this.controller.adicionarObserver(this);
        
        inicializarInterface();
    }

    private void inicializarInterface() {
        configurarJanela();
        criarComponentes();
        configurarLayout();
        configurarEventos();
        atualizarInterfaceCompleta();
        
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Seega - MVC com Observer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void criarComponentes() {
        // Labels de status
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 16));
        lblStatus.setOpaque(true);
        lblStatus.setBackground(new Color(240, 248, 255));

        lblTurno = new JLabel("", SwingConstants.CENTER);
        lblTurno.setFont(new Font("Arial", Font.BOLD, 14));
        lblTurno.setOpaque(true);
        lblTurno.setBackground(Color.WHITE);
        lblTurno.setBorder(BorderFactory.createEtchedBorder());

        lblPecasJ1 = new JLabel("", SwingConstants.CENTER);
        lblPecasJ1.setFont(new Font("Arial", Font.BOLD, 12));
        lblPecasJ1.setOpaque(true);
        lblPecasJ1.setBackground(Color.WHITE);
        lblPecasJ1.setBorder(BorderFactory.createEtchedBorder());

        lblPecasJ2 = new JLabel("", SwingConstants.CENTER);
        lblPecasJ2.setFont(new Font("Arial", Font.BOLD, 12));
        lblPecasJ2.setOpaque(true);
        lblPecasJ2.setBackground(Color.BLACK);
        lblPecasJ2.setForeground(Color.WHITE);
        lblPecasJ2.setBorder(BorderFactory.createEtchedBorder());

        lblRodadas = new JLabel("", SwingConstants.CENTER);
        lblRodadas.setFont(new Font("Arial", Font.PLAIN, 12));

        // Botões do tabuleiro
        botoes = new JButton[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                botoes[i][j] = new JButton();
                botoes[i][j].setPreferredSize(new Dimension(80, 80));
                botoes[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                
                if (controller.isCasaCentral(i, j)) {
                    botoes[i][j].setBackground(new Color(180, 180, 180));
                } else {
                    botoes[i][j].setBackground(Color.LIGHT_GRAY);
                }
            }
        }
    }

    private void configurarLayout() {
        // Painel superior
        JPanel painelSuperior = new JPanel(new GridLayout(2, 1, 5, 5));
        painelSuperior.setBorder(BorderFactory.createTitledBorder("Status"));
        painelSuperior.add(lblStatus);
        painelSuperior.add(lblTurno);
        add(painelSuperior, BorderLayout.NORTH);

        // Tabuleiro
        JPanel painelTabuleiro = new JPanel(new GridLayout(5, 5, 2, 2));
        painelTabuleiro.setBorder(BorderFactory.createTitledBorder("Tabuleiro"));
        painelTabuleiro.setBackground(new Color(139, 69, 19));
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                painelTabuleiro.add(botoes[i][j]);
            }
        }
        add(painelTabuleiro, BorderLayout.CENTER);

        // Painel inferior
        JPanel painelInferior = new JPanel(new GridLayout(1, 3, 10, 5));
        painelInferior.setBorder(BorderFactory.createTitledBorder("Estatísticas"));
        painelInferior.add(lblPecasJ1);
        painelInferior.add(lblPecasJ2);
        painelInferior.add(lblRodadas);
        add(painelInferior, BorderLayout.SOUTH);

        criarMenu();
    }

    private void criarMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opções");

        JMenuItem regrasItem = new JMenuItem("Regras");
        regrasItem.addActionListener(e -> mostrarRegras());

        JMenuItem reiniciarItem = new JMenuItem("Reiniciar");
        reiniciarItem.addActionListener(e -> {
            int opcao = JOptionPane.showConfirmDialog(this,
                "Reiniciar jogo?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                controller.reiniciarJogo();
            }
        });

        menu.add(regrasItem);
        menu.add(reiniciarItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void configurarEventos() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                final int linha = i;
                final int coluna = j;

                botoes[i][j].addActionListener(e -> {
                    if (controller.getEstado() == Estado.POSICIONANDO) {
                        controller.colocarPeca(linha, coluna);
                    } else if (controller.getEstado() == Estado.JOGANDO) {
                        controller.selecionarOuMover(linha, coluna);
                    }
                });
            }
        }
    }

    // ======== Observer Methods ========

    @Override
    public void onPecaColocada(int linha, int coluna, Jogador jogador) {
        atualizarBotaoTabuleiro(linha, coluna);
        atualizarEstatisticas();
        
        // Animação de colocação
        botoes[linha][coluna].setBackground(Color.GREEN);
        Timer timer = new Timer(200, e -> atualizarBotaoTabuleiro(linha, coluna));
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void onPecaMovida(int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino, Jogador jogador) {
        atualizarBotaoTabuleiro(linhaOrigem, colunaOrigem);
        atualizarBotaoTabuleiro(linhaDestino, colunaDestino);
        atualizarEstatisticas();
        
        // Animação de movimento
        botoes[linhaDestino][colunaDestino].setBackground(Color.CYAN);
        Timer timer = new Timer(300, e -> atualizarBotaoTabuleiro(linhaDestino, colunaDestino));
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void onPecaCapturada(int linha, int coluna, Jogador jogadorCapturado, Jogador jogadorCapturador) {
        // Animação de captura
        botoes[linha][coluna].setBackground(Color.RED);
        Timer timer = new Timer(400, e -> {
            atualizarBotaoTabuleiro(linha, coluna);
            atualizarEstatisticas();
        });
        timer.setRepeats(false);
        timer.start();
        
        // Notificação
        JOptionPane.showMessageDialog(this,
            "Jogador " + jogadorCapturador.getId() + " capturou uma peça!",
            "Captura!",
            JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onPecaSelecionada(int linha, int coluna, Jogador jogador) {
        atualizarTabuleiroCompleto();
        botoes[linha][coluna].setBackground(controller.obterCorSelecionada());
    }

    @Override
    public void onSelecaoRemovida() {
        atualizarTabuleiroCompleto();
    }

    @Override
    public void onTurnoMudou(Jogador novoJogador) {
        lblTurno.setText(controller.obterTextoTurno());
        lblTurno.setBackground(controller.obterCorTurno());
        lblTurno.setForeground(controller.obterCorTextoTurno());
    }

    @Override
    public void onEstadoMudou(Estado novoEstado, Estado estadoAnterior) {
        lblStatus.setText(controller.obterTextoStatus());
        
        if (novoEstado == Estado.JOGANDO) {
            // Habilita casa central
            botoes[2][2].setEnabled(controller.deveHabilitarCasaCentral());
            
            JOptionPane.showMessageDialog(this,
                "Posicionamento concluído!\nAgora mova suas peças.",
                "Nova Fase!",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void onJogoTerminado(Jogador vencedor) {
        lblStatus.setText(controller.obterTextoStatus());
        
        if (vencedor == null) {
            JOptionPane.showMessageDialog(this,
                "Empate! Jogo terminou empatado.\n\nJogar novamente?",
                "Empate",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Jogador " + vencedor.getId() + " venceu!\n\nJogar novamente?",
                "Fim de Jogo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void onMovimentoInvalido(String motivo) {
        lblStatus.setText("✗ " + motivo);
        lblStatus.setForeground(Color.RED);
        
        Timer timer = new Timer(2500, e -> {
            lblStatus.setText(controller.obterTextoStatus());
            lblStatus.setForeground(Color.BLACK);
        });
        timer.setRepeats(false);
        timer.start();
    }

    // ======== Métodos de Atualização ========

    private void atualizarInterfaceCompleta() {
        atualizarTabuleiroCompleto();
        atualizarEstatisticas();
        
        lblStatus.setText(controller.obterTextoStatus());
        lblTurno.setText(controller.obterTextoTurno());
        lblTurno.setBackground(controller.obterCorTurno());
        lblTurno.setForeground(controller.obterCorTextoTurno());
    }

    private void atualizarTabuleiroCompleto() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                atualizarBotaoTabuleiro(i, j);
            }
        }
    }

    private void atualizarBotaoTabuleiro(int linha, int coluna) {
        botoes[linha][coluna].setText(controller.obterTextoBotao(linha, coluna));
        botoes[linha][coluna].setBackground(controller.obterCorBotao(linha, coluna));

        // Destaque para peça selecionada
        if (controller.estaSelecionada(linha, coluna)) {
            botoes[linha][coluna].setBackground(controller.obterCorSelecionada());
        }
    }

    private void atualizarEstatisticas() {
        lblPecasJ1.setText(controller.obterTextoPecasJ1());
        lblPecasJ2.setText(controller.obterTextoPecasJ2());
        lblRodadas.setText(controller.obterTextoRodadas());
    }

    private void mostrarRegras() {
        String regras = """
            SEEGA - REGRAS
            
            Aqui vai as regras kekekeke
            """;

        JTextArea textArea = new JTextArea(regras);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 350));

        JOptionPane.showMessageDialog(this, scrollPane, "Como Jogar", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void atualizar() {
        atualizarInterfaceCompleta();
    }
}