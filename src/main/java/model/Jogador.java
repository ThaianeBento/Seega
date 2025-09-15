package model;

import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private int id;
    private List<Peca> pecas;

    public Jogador (int id){
        this.id=id;
        this.pecas=new ArrayList<>();
    }

    public int getId() { return id;}
    public List<Peca> getPecas() { return pecas; }
}
