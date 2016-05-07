/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds;

import ds.route.Route;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import ds.route.Route;

/**
 *
 * @author Dropsu
 */
@Path("") // path do wejscia do klasy
public class ApiConnection {
    
      @GET // kazdy path moze miec tylko po jednej motodzie GET, POST itd.
    @Produces(MediaType.TEXT_HTML)
    public String getVerification() {  //weryfikacja
        return "Wszystko jest fine - REST";
    }
    @Path ("/send_route")
    @POST
    @Produces("text/plain")
    @Consumes(MediaType.APPLICATION_JSON)
    public String receiveRoute (JsonObject data)
    {
        Route testowaTrasa = RouteConverter.jsonToJavaObj(data);
        String polZBD = DbConnection.addRouteToDb(testowaTrasa);
        return polZBD;
    }
    
}
