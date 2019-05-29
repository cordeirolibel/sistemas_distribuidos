package transfer;

import java.io.Serializable;

public class Notificacao  implements Serializable {
    String tipo;
    Oferta oferta;
    Horarios horarios;
    int id_cli;
    Interesse interesse;

    public Notificacao(Interesse interesse_){
        interesse = new Interesse(interesse_);
        tipo = "mot";
    }
    public Notificacao(Oferta oferta_, Interesse interesse_){
        interesse = new Interesse(interesse_);
        oferta = new Oferta(oferta_);
        tipo = "cli";
    }
    public Notificacao(Oferta oferta_, int id_cli_,Horarios horarios_){
        oferta = new Oferta(oferta_);
        id_cli = id_cli_;
        horarios = horarios_;
        tipo = "cli_ser";
    }

}
