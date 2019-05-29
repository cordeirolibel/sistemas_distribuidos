package transfer;


import java.io.Serializable;
import java.util.Date;

public class Interesse  implements Serializable {
    String veiculo;
    String itinerario;
    float preco;
    int n_passageiros;
    int id;
    int dia;
    int mes;
    int hora;

    public Interesse(){

    }
    public Interesse(Interesse i){
        veiculo = i.veiculo;
        itinerario = i.itinerario;
        preco = i.preco;
        n_passageiros = i.n_passageiros;
        id = i.id;
        dia = i.dia;
        mes = i.mes;
        hora = i.hora;
    }
    public void print(){
        System.out.printf("Veiculo     %s\n",veiculo);
        System.out.printf("Itinerario  %s\n",itinerario);
        System.out.printf("Preco       %.2f\n",preco);
        System.out.printf("Passageiros %d\n",n_passageiros);
        System.out.printf("Dia         %d\n",dia);
        System.out.printf("Mes         %d\n",mes);
        System.out.printf("Hora        %d\n",hora);
    }
}