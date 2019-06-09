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

    /**
     * Creates a new instance of Cotacao
     */
    public Cotacao() {
    }

    /**
     * Retrieves representation of an instance of servidor.Cotacao
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                +"<text>"
                +"<para>hello world2</para>"
                +"</text>";
    }

    /**
     * PUT method for updating or creating an instance of Cotacao
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
