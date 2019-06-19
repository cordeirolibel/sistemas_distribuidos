/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;

/**
 *
 * @author cordeiro
 */
public class Oferta  implements Serializable {
    
    //String itinerario;
    String veiculo;
    int passageiros;
    float preco;
    int id;


    public Oferta(){
    }

    //cria uma copia
    public Oferta(Oferta o){
        veiculo = o.veiculo;
        passageiros = o.passageiros;
        preco = o.preco;
        id = o.id;
    }

    public void print(){
        System.out.printf("Veiculo     %s\n",veiculo);
        System.out.printf("Preco       %.2f\n",preco);
        System.out.printf("Passageiros %d\n",passageiros);
    }



}

