package view;

import controller.JogoController;
import model.Estado;
import model.Jogo;
import model.Jogador;
import observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class JogoView extends JFrame implements Observer {
    private final JogoController controller;
    private final Jogo jogo;
    
    private CasaTabuleiro[][] casas;
    private JLabel lblStatus;
    private JLabel lblTurno;
    private JPanel painelPecasJ1;
    private JPanel painelPecasJ2;
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
        setTitle("Seega - Jogo de Estrategia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 900);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(45, 52, 54));
    }

    private void criarComponentes() {
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblStatus.setOpaque(true);
        lblStatus.setBackground(new Color(99, 110, 114));
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(178, 190, 195), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        lblTurno = new JLabel("", SwingConstants.CENTER);
        lblTurno.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTurno.setOpaque(true);
        lblTurno.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(178, 190, 195), 2),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        painelPecasJ1 = criarPainelContador("Jogador 1", Color.WHITE, Color.BLACK);
        painelPecasJ2 = criarPainelContador("Jogador 2", new Color(45, 52, 54), Color.WHITE);

        lblRodadas = new JLabel("", SwingConstants.CENTER);
        lblRodadas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblRodadas.setForeground(Color.WHITE);

        casas = new CasaTabuleiro[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                casas[i][j] = new CasaTabuleiro(i, j, controller.isCasaCentral(i, j));
            }
        }
    }

    private JPanel criarPainelContador(String titulo, Color corFundo, Color corTexto) {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(corFundo);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(178, 190, 195), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(corTexto);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblContador = new JLabel("0");
        lblContador.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblContador.setForeground(corTexto);
        lblContador.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblContador.setName("contador");
        
        painel.add(lblTitulo);
        painel.add(Box.createVerticalStrut(5));
        painel.add(lblContador);
        
        return painel;
    }

    private void configurarLayout() {

        JPanel painelSuperior = new JPanel(new GridLayout(2, 1, 10, 10));
        painelSuperior.setBackground(new Color(45, 52, 54));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        painelSuperior.add(lblStatus);
        painelSuperior.add(lblTurno);
        add(painelSuperior, BorderLayout.NORTH);

        JPanel containerTabuleiro = new JPanel(new BorderLayout());
        containerTabuleiro.setBackground(new Color(45, 52, 54));
        containerTabuleiro.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JPanel painelTabuleiro = new JPanel(new GridLayout(5, 5, 8, 8));
        painelTabuleiro.setBackground(new Color(116, 125, 140));
        painelTabuleiro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(178, 190, 195), 3),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                painelTabuleiro.add(casas[i][j]);
            }
        }
        
        containerTabuleiro.add(painelTabuleiro, BorderLayout.CENTER);
        add(containerTabuleiro, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new GridLayout(1, 3, 15, 10));
        painelInferior.setBackground(new Color(45, 52, 54));
        painelInferior.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        painelInferior.add(painelPecasJ1);
        painelInferior.add(painelPecasJ2);
        
        JPanel painelRodadas = new JPanel();
        painelRodadas.setBackground(new Color(99, 110, 114));
        painelRodadas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(178, 190, 195), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        painelRodadas.add(lblRodadas);
        painelInferior.add(painelRodadas);
        
        add(painelInferior, BorderLayout.SOUTH);

        criarMenu();
    }

    private void criarMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(99, 110, 114));
        
        JMenu menu = new JMenu("Opcoes");
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JMenuItem regrasItem = new JMenuItem("Regras do Jogo");
        regrasItem.addActionListener(e -> mostrarRegras());

        JMenuItem reiniciarItem = new JMenuItem("Reiniciar Jogo");
        reiniciarItem.addActionListener(e -> {
            int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja reiniciar o jogo?", "Confirmar", JOptionPane.YES_NO_OPTION);
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

                casas[i][j].addActionListener(e -> {
                    if (controller.getEstado() == Estado.POSICIONANDO) {
                        controller.colocarPeca(linha, coluna);
                    } else if (controller.getEstado() == Estado.JOGANDO) {
                        controller.selecionarOuMover(linha, coluna);
                    }
                });
            }
        }
    }


    @Override
    public void onPecaColocada(int linha, int coluna, Jogador jogador) {
        casas[linha][coluna].animarColocacao();
        atualizarCasa(linha, coluna);
        atualizarEstatisticas();
    }

    @Override
    public void onPecaMovida(int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino, Jogador jogador) {
        casas[linhaOrigem][colunaOrigem].limparAnimacao();
        casas[linhaDestino][colunaDestino].animarMovimento();
        atualizarCasa(linhaOrigem, colunaOrigem);
        atualizarCasa(linhaDestino, colunaDestino);
        atualizarEstatisticas();
    }

    @Override
    public void onPecaCapturada(int linha, int coluna, Jogador jogadorCapturado, Jogador jogadorCapturador) {
        casas[linha][coluna].animarCaptura();
        
        Timer timer = new Timer(600, e -> {
            atualizarCasa(linha, coluna);
            atualizarEstatisticas();
        });
        timer.setRepeats(false);
        timer.start();
        
        JOptionPane.showMessageDialog(this,
            "Jogador " + jogadorCapturador.getId() + " capturou uma peça!",
            "Captura!",
            JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onPecaSelecionada(int linha, int coluna, Jogador jogador) {
        atualizarTabuleiroCompleto();
        casas[linha][coluna].setSelecionada(true);
    }

    @Override
    public void onSelecaoRemovida() {
        atualizarTabuleiroCompleto();
    }

    @Override
    public void onTurnoMudou(Jogador novoJogador) {
        lblTurno.setText(obterTextoTurno());
        lblTurno.setBackground(obterCorTurno());
        lblTurno.setForeground(obterCorTextoTurno());
    }

    @Override
    public void onEstadoMudou(Estado novoEstado, Estado estadoAnterior) {
        lblStatus.setText(obterTextoStatus());
        
        if (novoEstado == Estado.JOGANDO) {
            casas[2][2].setEnabled(true);
            
            JOptionPane.showMessageDialog(this,
                "Posicionamento concluído!",
                "Nova Fase!",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void onJogoTerminado(Jogador vencedor) {
        lblStatus.setText(obterTextoStatus());
        
        String mensagem;
        String titulo;
        
        if (vencedor == null) {
            mensagem = "Empate! O jogo terminou empatado.";
            titulo = "Empate";
        } else {
            mensagem = "Jogador " + vencedor.getId() + " venceu!\n\nParabéns!";
            titulo = "Fim de Jogo";
        }
        
        int opcao = JOptionPane.showConfirmDialog(this,
            mensagem + "\n\nDeseja jogar novamente?",
            titulo,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
        
        if (opcao == JOptionPane.YES_OPTION) {
            controller.reiniciarJogo();
        }
    }

    @Override
    public void onMovimentoInvalido(String motivo) {
        lblStatus.setText( motivo);
        lblStatus.setBackground(new Color(231, 76, 60));
        
        Timer timer = new Timer(2500, e -> {
            lblStatus.setText(obterTextoStatus());
            lblStatus.setBackground(new Color(99, 110, 114));
        });
        timer.setRepeats(false);
        timer.start();
    }

    // ======== Métodos de Atualização ========

    private void atualizarInterfaceCompleta() {
        atualizarTabuleiroCompleto();
        atualizarEstatisticas();
        
        lblStatus.setText(obterTextoStatus());
        lblTurno.setText(obterTextoTurno());
        lblTurno.setBackground(obterCorTurno());
        lblTurno.setForeground(obterCorTextoTurno());
    }

    private void atualizarTabuleiroCompleto() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                atualizarCasa(i, j);
            }
        }
    }

    private void atualizarCasa(int linha, int coluna) {
        casas[linha][coluna].setPeca(controller.getPeca(linha, coluna));
        casas[linha][coluna].setSelecionada(estaSelecionada(linha, coluna));
    }

    private void atualizarEstatisticas() {
        JLabel contadorJ1 = (JLabel) painelPecasJ1.getComponent(2);
        contadorJ1.setText(String.valueOf(controller.contarPecasNoTabuleiro(controller.getJogador1())));
        
        JLabel contadorJ2 = (JLabel) painelPecasJ2.getComponent(2);
        contadorJ2.setText(String.valueOf(controller.contarPecasNoTabuleiro(controller.getJogador2())));
        
        lblRodadas.setText("Rodadas sem captura: " + controller.getJogadasSemCaptura() + "/20");
    }

    // ======== Metodos de Apresentacao (View Logic) ========

    private String obterTextoStatus() {
        switch (controller.getEstado()) {
            case POSICIONANDO:
                return "Fase: Posicionamento";
            case JOGANDO:
                return "Fase: Jogo - Mova suas peças";
            case FIM:
                return "Jogo Terminado";
            default:
                return "Estado desconhecido";
        }
    }

    private String obterTextoTurno() {
        return "Turno: Jogador " + controller.getJogadorAtual().getId();
    }

    private boolean estaSelecionada(int linha, int coluna) {
        return linha == controller.getSelecionadaLinha() && coluna == controller.getSelecionadaColuna();
    }

    private Color obterCorTurno() {
        return controller.getJogadorAtual().getId() == 1 ? Color.WHITE : Color.BLACK;
    }

    private Color obterCorTextoTurno() {
        return controller.getJogadorAtual().getId() == 1 ? Color.BLACK : Color.WHITE;
    }

    private void mostrarRegras() {
        String regras = """
            SEEGA - REGRAS DO JOGO
            
            OBJETIVO:
            Capture todas as pecas do adversario ou bloqueie seus movimentos.
            
            FASE 1 - POSICIONAMENTO:
            • Cada jogador coloca 12 pecas alternadamente
            • Nao pode colocar na casa central (meio do tabuleiro)
            • Coloque 2 pecas por turno
            
            FASE 2 - MOVIMENTACAO:
            • Mova suas pecas uma casa por vez (horizontal/vertical)
            • Capture pecas inimigas atraves do sanduiche
            • Se capturar, jogue novamente!
            
            CAPTURA:
            • Coloque suas pecas em ambos os lados de uma peca inimiga
            • A peca no meio e capturada e removida
            
            VITORIA:
            • Capture todas as pecas adversarias
            • Bloqueie todos os movimentos do oponente
            • Apos 20 rodadas sem captura, vence quem tiver mais pecas
            
            DICA: Planeje suas jogadas e use a casa central!
            """;

        JTextArea textArea = new JTextArea(regras);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBackground(new Color(240, 242, 245));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Como Jogar", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void atualizar() {
        atualizarInterfaceCompleta();
    }

    // ======== Classe Interna: Casa do Tabuleiro ========
    
    private class CasaTabuleiro extends JButton {
        private model.Peca peca;
        private boolean selecionada;
        private final boolean casaCentral;
        private Color corAnimacao;

        public CasaTabuleiro(int linha, int coluna, boolean casaCentral) {
            this.casaCentral = casaCentral;
            this.selecionada = false;
            
            setPreferredSize(new Dimension(90, 90));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public void setPeca(model.Peca peca) {
            this.peca = peca;
            repaint();
        }

        public void setSelecionada(boolean selecionada) {
            this.selecionada = selecionada;
            repaint();
        }

        public void animarColocacao() {
            corAnimacao = new Color(46, 213, 115);
            Timer timer = new Timer(400, e -> limparAnimacao());
            timer.setRepeats(false);
            timer.start();
        }

        public void animarMovimento() {
            corAnimacao = new Color(52, 172, 224);
            Timer timer = new Timer(500, e -> limparAnimacao());
            timer.setRepeats(false);
            timer.start();
        }

        public void animarCaptura() {
            corAnimacao = new Color(231, 76, 60);
            Timer timer = new Timer(600, e -> limparAnimacao());
            timer.setRepeats(false);
            timer.start();
        }

        public void limparAnimacao() {
            corAnimacao = null;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Fundo da casa
            Color corFundo = casaCentral ? new Color(149, 165, 166) : new Color(189, 195, 199);
            g2.setColor(corFundo);
            g2.fillRoundRect(0, 0, width, height, 15, 15);

            // Marca da casa central
            if (casaCentral && peca == null) {
                g2.setColor(new Color(127, 140, 141));
                g2.setStroke(new BasicStroke(2));
                int margin = 15;
                g2.drawLine(margin, margin, width - margin, height - margin);
                g2.drawLine(width - margin, margin, margin, height - margin);
            }

            // Desenhar peça (círculo)
            if (peca != null) {
                int diameter = Math.min(width, height) - 20;
                int x = (width - diameter) / 2;
                int y = (height - diameter) / 2;

              

                // Cor da peça
                Color corPeca = peca.getDono().getId() == 1 ? Color.WHITE : new Color(45, 52, 54);
                
               

                g2.setColor(corPeca);
                g2.fillOval(x, y, diameter, diameter);

                // Borda da peça
                g2.setColor(peca.getDono().getId() == 1 ? new Color(200, 200, 200) : new Color(30, 30, 30));
                g2.setStroke(new BasicStroke(3));
                g2.drawOval(x, y, diameter, diameter);

                
            }

            // Highlight de seleção
            if (selecionada) {
                g2.setColor(new Color(241, 196, 15));
                g2.setStroke(new BasicStroke(4));
                g2.drawRoundRect(2, 2, width - 4, height - 4, 15, 15);
            }

            g2.dispose();
        }
    }
}