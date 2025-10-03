package observer;

import model.Estado;
import model.Jogador;

public interface Observer {
	  
    void onPecaColocada(int linha, int coluna, Jogador jogador);
    void onPecaMovida(int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino, Jogador jogador);
    void onPecaCapturada(int linha, int coluna, Jogador jogadorCapturado, Jogador jogadorCapturador);
    void onPecaSelecionada(int linha, int coluna, Jogador jogador);
    void onSelecaoRemovida();
    void onTurnoMudou(Jogador novoJogador);
    void onEstadoMudou(Estado novoEstado, Estado estadoAnterior);
    void onJogoTerminado(Jogador vencedor);
    void onMovimentoInvalido(String motivo);
    default void atualizar() { }
}
