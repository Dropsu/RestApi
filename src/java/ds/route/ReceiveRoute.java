/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds.route;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
/**
 *
 * @author Dropsu
 */
@Path ("/receive_route")
public class ReceiveRoute {
   
    @POST
    @Produces("text/plain")
    @Consumes(MediaType.APPLICATION_JSON)
    public String getRoute (String data)
    {
    return data;
    }
}
