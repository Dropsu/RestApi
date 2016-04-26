/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds.route;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.json.*;
/**
 *
 * @author Dropsu
 */
@Path ("/receive_route")
public class ReceiveRoute {
   
    JsonArray miejsca;
    @POST
    @Produces("text/plain")
    @Consumes(MediaType.APPLICATION_JSON)
    public String getRoute (JsonArray data)
    {
        miejsca = data;
        return miejsca.getString(1) + " *** POBIERANIE POSZLO OK *** ";
    }
}
