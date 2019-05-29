package transfer;

import java.io.Serializable;

public class Oferta  implements Serializable {
    //String itinerario;
    String veiculo;
    int passageiros;
    float preco;
    int id;

    public Oferta(Oferta o){
        veiculo = o.veiculo;
        passageiros = o.passageiros;
        preco = o.preco;
        id = o.id;
    }
    public Oferta(){
    }

    public void print(){
        System.out.printf("Veiculo     %s\n",veiculo);
        System.out.printf("Preco       %.2f\n",preco);
        System.out.printf("Passageiros %d\n",passageiros);
    }



}
