/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds;

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
 * @author Dropsu
 */
@Path("status/") // path do wejscia do klasy
public class GenericResource {

    private String jVersion = "2.1.2";

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    /**
     * Retrieves representation of an instance of ds.GenericResource
     * @return an instance of java.lang.String
     */
    
    @GET // kazdy path moze miec tylko po jednej motodzie GET, POST itd.
    @Produces(MediaType.TEXT_HTML)
    public String getVerification() {
        return "Wszystko jest fine";
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    @Path("/version")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getJerseyVersion() {
        return "<p>Jrsey Version </p>" + jVersion;
    }
    
    
    
}
