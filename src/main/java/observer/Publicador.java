package observer;

import model.Estado;
import model.Jogador;
import java.util.ArrayList;
import java.util.List;


public class Publicador {
    
    private final List<Observer> observers = new ArrayList<>();
    
    
    
    public void adicionarObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void removerObserver(Observer observer) {
        observers.remove(observer);
    }
    
    
    
    public void notificarPecaColocada(int linha, int coluna, Jogador jogador) {
        for (Observer observer : observers) {
            try {
                observer.onPecaColocada(linha, coluna, jogador);
            } catch (Exception e) {
                System.err.println("Erro ao notificar peça colocada: " + e.getMessage());
            }
        }
    }
    
    public void notificarPecaMovida(int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino, Jogador jogador) {
        for (Observer observer : observers) {
            try {
                observer.onPecaMovida(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, jogador);
            } catch (Exception e) {
                System.err.println("Erro ao notificar peça movida: " + e.getMessage());
            }
        }
    }
    
    public void notificarPecaCapturada(int linha, int coluna, Jogador jogadorCapturado, Jogador jogadorCapturador) {
        for (Observer observer : observers) {
            try {
                observer.onPecaCapturada(linha, coluna, jogadorCapturado, jogadorCapturador);
            } catch (Exception e) {
                System.err.println("Erro ao notificar peça capturada: " + e.getMessage());
            }
        }
    }
    
    public void notificarPecaSelecionada(int linha, int coluna, Jogador jogador) {
        for (Observer observer : observers) {
            try {
                observer.onPecaSelecionada(linha, coluna, jogador);
            } catch (Exception e) {
                System.err.println("Erro ao notificar peça selecionada: " + e.getMessage());
            }
        }
    }
    
    public void notificarSelecaoRemovida() {
        for (Observer observer : observers) {
            try {
                observer.onSelecaoRemovida();
            } catch (Exception e) {
                System.err.println("Erro ao notificar seleção removida: " + e.getMessage());
            }
        }
    }
    
    
    
    public void notificarTurnoMudou(Jogador novoJogador) {
        for (Observer observer : observers) {
            try {
                observer.onTurnoMudou(novoJogador);
            } catch (Exception e) {
                System.err.println("Erro ao notificar mudança de turno: " + e.getMessage());
            }
        }
    }
    
    public void notificarEstadoMudou(Estado novoEstado, Estado estadoAnterior) {
        for (Observer observer : observers) {
            try {
                observer.onEstadoMudou(novoEstado, estadoAnterior);
            } catch (Exception e) {
                System.err.println("Erro ao notificar mudança de estado: " + e.getMessage());
            }
        }
    }
    
    
    
    public void notificarJogoTerminado(Jogador vencedor) {
        for (Observer observer : observers) {
            try {
                observer.onJogoTerminado(vencedor);
            } catch (Exception e) {
                System.err.println("Erro ao notificar fim de jogo: " + e.getMessage());
            }
        }
    }
    
    public void notificarMovimentoInvalido(String motivo) {
        for (Observer observer : observers) {
            try {
                observer.onMovimentoInvalido(motivo);
            } catch (Exception e) {
                System.err.println("Erro ao notificar movimento inválido: " + e.getMessage());
            }
        }
    }
    
    
    
  
    public void notificar() {
        for (Observer observer : observers) {
            try {
                observer.atualizar();
            } catch (Exception e) {
                System.err.println("Erro ao notificar (método genérico): " + e.getMessage());
            }
        }
    }
}