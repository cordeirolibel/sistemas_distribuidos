/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author cordeiro
 */
@Path("cotacao")
public class Cotacao {

    @Context
    private UriInfo context;
    LinkedList<Oferta> lista_oferta;
    LinkedList<Horarios> lista_horarios_mot;
    
    /**
     * Creates a new instance of Cotacao
     */
    public Cotacao() {
        lista_oferta           = new LinkedList<Oferta>();
        lista_horarios_mot     = new LinkedList<Horarios>();
    }

    /**
     * Retrieves representation of an instance of servidor.Cotacao
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        Gson gson = new Gson();
        Oferta oferta_i;
        int size = lista_oferta.size();

        //lista de retorno de ofertas
        LinkedList<Oferta> lista_oferta_retorno = new LinkedList<Oferta>();

        //procura o oferta desse motorista
        for (int i=0;i<size;i++){
            oferta_i = lista_oferta.get(i);
            lista_oferta_retorno.add(oferta_i);
            
        }
        return gson.toJson(lista_oferta_retorno);  
    }

    /**
     * PUT method for updating or creating an instance of Cotacao
     * @param content representation for the resource
     */
    @GET
    @Path("{json}")
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
    
     // --------------------------------------------------------------
    // =====> Internas
    // --------------------------------------------------------------
    
    //retorna true se oferta atende Interesse, mas nao avalia o preco
    private boolean comparaInteresseComOfertaSemPreco(Interesse i,Oferta o,Horarios h){
        if ((o.veiculo.equals(i.veiculo)) &
            (o.passageiros>=i.n_passageiros) &
            (h.disponivel(i.dia,i.mes,i.hora))){//verifica no calendario
            return true;
        }
        return false;
    }
    
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
    

}
