package controller;

import model.*;
import observer.Publicador;
import java.util.List;


public class JogoController {
    private final Jogo jogo;
    private final Publicador publicador;

    public JogoController(Jogo jogo) {
        this.jogo = jogo;
        this.publicador = jogo.getPublicador();
    }

    

    public boolean colocarPeca(int linha, int coluna) {
        
        if (!validarFasePositionamento()) return false;
        if (!validarPosicaoParaColocar(linha, coluna)) return false;

       
        executarColocacaoPeca(linha, coluna);
        
        
        verificarMudancaFase();
        verificarMudancaTurno();
        
        return true;
    }

    public boolean selecionarOuMover(int linha, int coluna) {
        if (!validarFaseJogo()) return false;

        
        if (jogo.getSelecionadaLinha() == -1) {
            return tentarSelecionarPeca(linha, coluna);
        } 
        
        else {
            return tentarMoverPeca(linha, coluna);
        }
    }

    public void reiniciarJogo() {
        jogo.reset();
       
        publicador.notificar();
    }

    public Jogo getJogo() {
        return jogo;
    }

   

    private boolean validarFasePositionamento() {
        if (jogo.getEstado() != Estado.POSICIONANDO) {
            publicador.notificarMovimentoInvalido("Não é a fase de posicionamento");
            return false;
        }
        return true;
    }

    private boolean validarFaseJogo() {
        if (jogo.getEstado() != Estado.JOGANDO) {
            publicador.notificarMovimentoInvalido("Não é a fase de jogo");
            return false;
        }
        return true;
    }

    private boolean validarPosicaoParaColocar(int linha, int coluna) {
        if (!jogo.posicaoValida(linha, coluna)) {
            publicador.notificarMovimentoInvalido("Posição fora do tabuleiro");
            return false;
        }

        if (jogo.isCasaCentral(linha, coluna)) {
            publicador.notificarMovimentoInvalido("Não é possível colocar peça na casa central");
            return false;
        }

        if (!jogo.posicaoVazia(linha, coluna)) {
            publicador.notificarMovimentoInvalido("Casa já está ocupada");
            return false;
        }

        return true;
    }

    private boolean validarSelecaoPeca(int linha, int coluna) {
        if (!jogo.posicaoValida(linha, coluna)) {
            publicador.notificarMovimentoInvalido("Posição fora do tabuleiro");
            return false;
        }

        if (jogo.posicaoVazia(linha, coluna)) {
            publicador.notificarMovimentoInvalido("Não há peça nesta posição");
            return false;
        }

        if (!jogo.temPecaDoJogador(jogo.getJogadorAtual(), linha, coluna)) {
            publicador.notificarMovimentoInvalido("Esta peça não é sua");
            return false;
        }

        return true;
    }

    private boolean validarMovimento(int linhaDestino, int colunaDestino) {
        if (!jogo.posicaoValida(linhaDestino, colunaDestino)) {
            publicador.notificarMovimentoInvalido("Posição de destino fora do tabuleiro");
            return false;
        }

       
        if (jogo.getPeca(linhaDestino, colunaDestino) != null) {
            publicador.notificarMovimentoInvalido("Casa de destino já está ocupada");
            return false;
        }

        
        int linhaOrigem = jogo.getSelecionadaLinha();
        int colunaOrigem = jogo.getSelecionadaColuna();
        int distancia = Math.abs(linhaDestino - linhaOrigem) + Math.abs(colunaDestino - colunaOrigem);

        if (distancia != 1) {
            publicador.notificarMovimentoInvalido("Movimento deve ser para casa adjacente");
            return false;
        }

        return true;
    }

    

    private void executarColocacaoPeca(int linha, int coluna) {
       
        Peca novaPeca = new Peca(jogo.getJogadorAtual());
        jogo.setPeca(linha, coluna, novaPeca);
        
       
        jogo.getJogadorAtual().getPecas().add(novaPeca);
        
      
        jogo.incrementarPecasColocadas();
        jogo.incrementarPecasTurno();

      
        publicador.notificarPecaColocada(linha, coluna, jogo.getJogadorAtual());
    }

    private boolean tentarSelecionarPeca(int linha, int coluna) {
        if (!validarSelecaoPeca(linha, coluna)) {
            return false;
        }

       
        jogo.setSelecionada(linha, coluna);
        
       
        publicador.notificarPecaSelecionada(linha, coluna, jogo.getJogadorAtual());
        
        return true;
    }

    private boolean tentarMoverPeca(int linhaDestino, int colunaDestino) {
        if (!validarMovimento(linhaDestino, colunaDestino)) {
            return false;
        }

        int linhaOrigem = jogo.getSelecionadaLinha();
        int colunaOrigem = jogo.getSelecionadaColuna();
        
       
        executarMovimento(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino);
        
       
        boolean houveCaptura = verificarEExecutarCapturas(linhaDestino, colunaDestino);
        
       
        gerenciarTurnoAposMovimento(houveCaptura);
        
       
        jogo.limparSelecao();
        publicador.notificarSelecaoRemovida();
        
        
        verificarFimDeJogo();
        
        return true;
    }

    private void executarMovimento(int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino) {
        
        Peca peca = jogo.getPeca(linhaOrigem, colunaOrigem);
        jogo.removerPeca(linhaOrigem, colunaOrigem);
        jogo.setPeca(linhaDestino, colunaDestino, peca);

        
        publicador.notificarPecaMovida(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, jogo.getJogadorAtual());
    }

   

    private boolean verificarEExecutarCapturas(int linha, int coluna) {
        boolean capturou = false;
        Jogador jogadorAtual = jogo.getJogadorAtual();
        
        
        int[][] direcoes = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        for (int[] direcao : direcoes) {
            int linhaInimiga = linha + direcao[0];
            int colunaInimiga = coluna + direcao[1];
            int linhaPropria = linha + 2 * direcao[0];
            int colunaPropria = coluna + 2 * direcao[1];

            if (podeCapturar(linhaInimiga, colunaInimiga, linhaPropria, colunaPropria, jogadorAtual)) {
                executarCaptura(linhaInimiga, colunaInimiga, jogadorAtual);
                capturou = true;
            }
        }

        return capturou;
    }

    private boolean podeCapturar(int linhaInimiga, int colunaInimiga, int linhaPropria, int colunaPropria, Jogador jogadorAtual) {
        
        if (!jogo.posicaoValida(linhaInimiga, colunaInimiga) || !jogo.posicaoValida(linhaPropria, colunaPropria)) {
            return false;
        }

       
        Peca pecaInimiga = jogo.getPeca(linhaInimiga, colunaInimiga);
        Peca pecaPropria = jogo.getPeca(linhaPropria, colunaPropria);

        if (pecaInimiga == null || pecaPropria == null) {
            return false;
        }

       
        if (pecaInimiga.getDono() == jogadorAtual || pecaPropria.getDono() != jogadorAtual) {
            return false;
        }

       

        return true;
    }

    private void executarCaptura(int linha, int coluna, Jogador jogadorCapturador) {
        Peca pecaCapturada = jogo.getPeca(linha, coluna);
        Jogador jogadorCapturado = pecaCapturada.getDono();

        
        jogo.removerPeca(linha, coluna);

        
        jogadorCapturado.getPecas().remove(pecaCapturada);

       
        publicador.notificarPecaCapturada(linha, coluna, jogadorCapturado, jogadorCapturador);
    }

    

    private void verificarMudancaTurno() {
        if (jogo.getPecasTurno() >= 2) {
            jogo.zerarPecasTurno();
            mudarTurno();
        }
    }

    private void verificarMudancaFase() {
        if (jogo.getPecasColocadas() >= 24) {
            Estado estadoAnterior = jogo.getEstado();
            jogo.setEstado(Estado.JOGANDO);
            jogo.setJogadorAtual(jogo.getJogador2()); 
            jogo.zerarPecasTurno();

           
            publicador.notificarEstadoMudou(Estado.JOGANDO, estadoAnterior);

          
            if (!temMovimentosPossiveis(jogo.getJogadorAtual())) {
                verificarJogadorSemMovimentos();
            }
        }
    }

    private void gerenciarTurnoAposMovimento(boolean houveCaptura) {
        if (houveCaptura) {
            jogo.zerarJogadasSemCaptura();
            
        } else {
            jogo.incrementarJogadasSemCaptura();
            mudarTurno();
        }
    }

    private void mudarTurno() {
        Jogador jogadorAnterior = jogo.getJogadorAtual();
        Jogador novoJogador = jogo.getOutroJogador();
        
        jogo.setJogadorAtual(novoJogador);
        
        
        if (jogo.getEstado() == Estado.JOGANDO && !temMovimentosPossiveis(novoJogador)) {
            verificarJogadorSemMovimentos();
            return;
        }
        
     
        publicador.notificarTurnoMudou(novoJogador);
    }

    private void verificarJogadorSemMovimentos() {
        Jogador jogadorAtual = jogo.getJogadorAtual();
        Jogador outroJogador = jogo.getOutroJogador();

        
        publicador.notificarMovimentoInvalido("Jogador " + jogadorAtual.getId() + " não tem movimentos possíveis");

        if (!temMovimentosPossiveis(outroJogador)) {
            
            finalizarJogo(null); 
        } else {
            
            jogo.setJogadorAtual(outroJogador);
            publicador.notificarTurnoMudou(outroJogador);
        }
    }

    

    private void verificarFimDeJogo() {
        
        if (jogo.getJogador1().getPecas().isEmpty()) {
            finalizarJogo(jogo.getJogador2());
            return;
        }
        
        if (jogo.getJogador2().getPecas().isEmpty()) {
            finalizarJogo(jogo.getJogador1());
            return;
        }

        
        if (jogo.getJogadasSemCaptura() >= 20) {
            finalizarJogoPorTempo();
        }
    }

    private void finalizarJogoPorTempo() {
        int pecasJ1 = jogo.contarPecasNoTabuleiro(jogo.getJogador1());
        int pecasJ2 = jogo.contarPecasNoTabuleiro(jogo.getJogador2());

        jogo.setEstado(Estado.FIM);

        if (pecasJ1 > pecasJ2) {
            finalizarJogo(jogo.getJogador1());
        } else if (pecasJ2 > pecasJ1) {
            finalizarJogo(jogo.getJogador2());
        } else {
            finalizarJogo(null); 
        }
    }

    private void finalizarJogo(Jogador vencedor) {
        jogo.setEstado(Estado.FIM);
        
        
        publicador.notificarJogoTerminado(vencedor);
    }

    

    private boolean temMovimentosPossiveis(Jogador jogador) {
        List<int[]> posicoesPecas = jogo.getPosicoesDasPecas(jogador);
        
        for (int[] posicao : posicoesPecas) {
            int linha = posicao[0];
            int coluna = posicao[1];
            
            
            int[][] direcoes = {{1,0}, {-1,0}, {0,1}, {0,-1}};
            for (int[] dir : direcoes) {
                int novaLinha = linha + dir[0];
                int novaColuna = coluna + dir[1];
                
                if (jogo.posicaoVazia(novaLinha, novaColuna)) {
                    return true; 
                }
            }
        }
        
        return false; 
    }

    

    public void debugEstadoJogo() {
        System.out.println("=== ESTADO DO JOGO ===");
        System.out.println("Estado: " + jogo.getEstado());
        System.out.println("Jogador Atual: " + jogo.getJogadorAtual().getId());
        System.out.println("Peças Colocadas: " + jogo.getPecasColocadas() + "/24");
        System.out.println("Peças por Turno: " + jogo.getPecasTurno() + "/2");
        System.out.println("Jogadas sem Captura: " + jogo.getJogadasSemCaptura() + "/20");
        System.out.println("Peças J1: " + jogo.contarPecasNoTabuleiro(jogo.getJogador1()));
        System.out.println("Peças J2: " + jogo.contarPecasNoTabuleiro(jogo.getJogador2()));
    }

    public void debugTabuleiro() {
        System.out.println("=== TABULEIRO ===");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Peca peca = jogo.getPeca(i, j);
                if (peca == null) {
                    System.out.print("- ");
                } else {
                    System.out.print(peca.getDono().getId() + " ");
                }
            }
            System.out.println();
        }
        System.out.println("================");
    }
}