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
import javax.ws.rs.PathParam;
import ds.route.Route;
import javax.json.JsonArray;

/**
 *
 * @author Dropsu
 */
@Path("") // path do wejscia do klasy
public class ApiCommunication {
    
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
        Route testowaTrasa = jsonToJavaObj(data);
        String polZBD = DbCommunication.addRouteToDb(testowaTrasa);
        return polZBD;
    }
    
    @Path("search_route/{routeId}")
    @GET
    @Produces("text/plain")
    public String searchForRoute (@PathParam("routeId") String routeId)
    {
        Route nowaTrasa = DbCommunication.getRouteFromDb(routeId);
        return nowaTrasa.city_name;
    }
    
    static public Route jsonToJavaObj (JsonObject data)
    {
        
    String route_id = data.getString("route_id");
    String city_name = data.getString("city_name");
    String route_length_km = data.getString("route_length_km");
    int estimated_walk_time_in_mins = data.getInt("estimated_walk_time_in_mins");
    int number_of_places = data.getInt("number_of_places");
    JsonArray jsonMiejsca = data.getJsonArray("places");
    
    Route trasa = new Route(route_id,city_name,route_length_km,estimated_walk_time_in_mins,number_of_places,jsonMiejsca);
    return trasa;
    }
}
