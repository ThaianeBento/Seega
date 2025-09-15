package observer;

import java.util.ArrayList;
import java.util.List;

public class Publicador {
    private List<Observer> observers = new ArrayList<>();

    public void adicionarObserver(Observer o) { observers.add(o); }
    public void removerObserver(Observer o) { observers.remove(o); }
    public void notificar() {
        for (Observer o : observers) {
            o.atualizar();
        }
    }
}
