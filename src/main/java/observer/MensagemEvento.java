package observer;

public class MensagemEvento {
    private final String texto;

    public MensagemEvento(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}
