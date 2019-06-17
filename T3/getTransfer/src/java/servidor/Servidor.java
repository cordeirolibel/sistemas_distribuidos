/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import com.google.gson.Gson;
import java.util.LinkedList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author cordeiro
 */
@Path("app")
public class Servidor {

    @Context
    private UriInfo context;
    LinkedList<Interesse> lista_interesses_clie;
    LinkedList<Oferta> lista_oferta;
    LinkedList<Horarios> lista_horarios_mot;
    
    /**
     * Creates a new instance of Servidor
    */
    public Servidor() {
        System.out.println("ServImpl executado!");
        
        //cria todas as listas e filas
        lista_interesses_clie  = new LinkedList<Interesse>();
        lista_oferta           = new LinkedList<Oferta>();
        lista_horarios_mot     = new LinkedList<Horarios>();
        
        initOfertas();
    }


    /**
     * PUT method for updating or creating an instance of Cotacao
     * @param content representation for the resource
     */
    @GET
    @Path("cotacao/{json}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("json") String json) {
        System.out.printf("=======Cotacao========\n");
        Interesse interesse = new Interesse();
        Gson gson = new Gson();
        
        // converts string json to Interesse class object
        try {
            if (json != null) {
                interesse = gson.fromJson(json, Interesse.class);      
            }
        } catch (Exception ex) {
            System.out.println("invalid JSON!");
            return("invalid JSON!");
        }
        
        //printa interesse
        System.out.printf("==> Interesse de %d:\n",interesse.id);
        interesse.print();
        
        Oferta oferta_i;
        Horarios horarios;
        int size = lista_oferta.size();

        //lista de retorno de ofertas
        LinkedList<Oferta> lista_oferta_retorno = new LinkedList<Oferta>();

        //procura o oferta desse motorista
        for (int i=0;i<size;i++){
            oferta_i = lista_oferta.get(i);
            horarios = lista_horarios_mot.get(i);
            //se oferta atende o interesse
            if (comparaInteresseComOferta(interesse,oferta_i,horarios)){
              
                //adiciona na lista de retorno
                lista_oferta_retorno.add(oferta_i);
            }
        }
        return gson.toJson(lista_oferta_retorno);  

    }
    
    /**
     * PUT method for updating or creating an instance of Cotacao
     * @param content representation for the resource
     */
    @GET
    @Path("reserva/{oferta}/{interesse}")
    @Produces(MediaType.APPLICATION_JSON)
    public String reserva(@PathParam("oferta") String oferta_json,@PathParam("interesse") String interesse_json) {
        System.out.printf("=======Reserva========\n");
        
        Interesse interesse = new Interesse();
        Oferta oferta = new Oferta();
        
        Gson gson = new Gson();
        
        // converte string json para Interesse e oferta class object
        try {
            if (oferta_json != null) {
                oferta = gson.fromJson(oferta_json, Oferta.class);      
            }
            if (interesse_json != null) {
                interesse = gson.fromJson(interesse_json, Interesse.class);      
            }
        } catch (Exception ex) {
            System.out.println("invalid JSON!");
            return("invalid JSON!");
        }
        
        //print
        System.out.printf("==> Oferta de motorista %d e cliente %d\n",oferta.id,interesse.id);
        oferta.print();

        Oferta oferta_i;
        Horarios horarios;
        int size = lista_oferta.size();
        //procura o oferta desse motorista
        for (int i=0;i<size;i++){

            //pega informacoes do motorista
            oferta_i = lista_oferta.get(i);

            //quando encontrar a oferta
            if (oferta_i.id == oferta.id) {
                horarios = lista_horarios_mot.get(i);

                //controle de concorrencia, só um entra no bloco
                synchronized (this) {

                    //verifica se tem horario disponivel
                    if(horarios.disponivel(interesse.dia, interesse.mes, interesse.hora)) {
                        //marcar horario
                        horarios.set(interesse.dia, interesse.mes, interesse.hora);
                    }
                    else{
                        System.out.printf("\tMotorista ocupado\n");
                        return ("Motorista ocupado");
                    }
                }
                //notifica o motorista que a reserva foi feita
                System.out.printf("\tReserva efetuada\n");
                return ("Reserva efetuada");

            }
        }
        System.out.printf("\tMotorista nao encontrado\n");
        return ("Motorista nao encontrado");
    }
    
    // --------------------------------------------------------------
    // =====> Internas
    // --------------------------------------------------------------
   
    
    //retorna true se oferta atende Interesse
    private boolean comparaInteresseComOferta(Interesse i,Oferta o,Horarios h){
        
        if ((o.veiculo.equals(i.veiculo)) &
            (h.disponivel(i.dia,i.mes,i.hora)) & //verifica no calendario
            (o.passageiros>=i.n_passageiros) &
            (o.preco <= i.preco)){
            return true;
        }
        return false;
    }
    
    private void initOfertas(){
        Oferta oferta;
        
        // Oferta 1
        oferta = new Oferta();
        
        oferta.id = 0;
        oferta.passageiros = 2;
        oferta.preco = (float) 20.0;
        oferta.veiculo = "bmw";   
        
        lista_oferta.add(oferta);
        lista_horarios_mot.add(new Horarios());
                
        // Oferta 2
        oferta = new Oferta();
        
        oferta.id = 1;
        oferta.passageiros = 4;
        oferta.preco = (float) 15.0;
        oferta.veiculo = "duster";   
        
        lista_oferta.add(oferta);
        lista_horarios_mot.add(new Horarios());
        
        // Oferta 3
        oferta = new Oferta();
        
        oferta.id = 2;
        oferta.passageiros = 3;
        oferta.preco = (float) 10.0;
        oferta.veiculo = "fusca";  
        
        lista_oferta.add(oferta);
        lista_horarios_mot.add(new Horarios());
    }
    
}
