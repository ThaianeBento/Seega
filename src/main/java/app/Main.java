package app;

import controller.JogoController;
import model.Jogo;
import view.JogoView;

public class Main {
    public static void main(String[] args) {
            Jogo jogo = new Jogo();
            JogoController controller = new JogoController(jogo);
            new JogoView(jogo, controller);


    }
}