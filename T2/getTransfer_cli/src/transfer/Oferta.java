package transfer;

import java.io.Serializable;
import java.util.Date;

public class Oferta  implements Serializable {
    //String itinerario;
    String veiculo;
    int passageiros;
    float preco;
    int id;

    public Oferta(){
    }

    public void print(){
        System.out.printf("Veiculo     %s\n",veiculo);
        System.out.printf("Preco       %.2f\n",preco);
        System.out.printf("Passageiros %d\n",passageiros);
    }

}
