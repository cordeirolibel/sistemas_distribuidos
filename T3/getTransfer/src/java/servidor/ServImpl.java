/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author cordeiro
 */
public class ServImpl {
    LinkedList<Interesse> lista_interesses_clie;
    LinkedList<Oferta> lista_oferta;
    LinkedList<Horarios> lista_horarios_mot;
    
    public ServImpl() {
        System.out.println("ServImpl executado!");

        //cria todas as listas e filas
        lista_interesses_clie  = new LinkedList<Interesse>();
        lista_oferta           = new LinkedList<Oferta>();
        lista_horarios_mot     = new LinkedList<Horarios>();

    }
    
    
}
