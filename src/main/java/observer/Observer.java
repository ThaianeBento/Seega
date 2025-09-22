package observer;

import model.Estado;
import model.Jogador;

public interface Observer {
	  //evento das pecas
    void onPecaColocada(int linha, int coluna, Jogador jogador);
    void onPecaMovida(int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino, Jogador jogador);
    void onPecaCapturada(int linha, int coluna, Jogador jogadorCapturado, Jogador jogadorCapturador);
    void onPecaSelecionada(int linha, int coluna, Jogador jogador);
    void onSelecaoRemovida();
    
    // Eventos de estado
    void onTurnoMudou(Jogador novoJogador);
    void onEstadoMudou(Estado novoEstado, Estado estadoAnterior);
    
    // Eventos de jogo
    void onJogoTerminado(Jogador vencedor);
    void onMovimentoInvalido(String motivo);
    
    // Método genérico (mantém compatibilidade)
    default void atualizar() { }
}
