/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds;



import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;
import ds.route.Route;
import javax.json.*;

/**
 *
 * @author Dropsu
 */
@Path("") // path do wejscia do klasy
public class ApiCommunication {
    
      @GET // kazdy path moze miec tylko po jednej motodzie GET, POST itd.
    @Produces(MediaType.TEXT_HTML)
    public String getDocumentation() {  //weryfikacja
        return "<h1>Api Documentation </h1>"
                + "<p>Avaliable methods:</p>"
                + "<h3>1) Search for route by its name</h3>"
                + ".../api/search_route/{routeId}<br>"
                + "Input: 'routeId' -> Name of route to search for<br>"
                + "Result: route as JSON ";
                
    }
    
    
    @Path ("/send_route")
    @POST
    @Produces("text/plain")
    @Consumes(MediaType.APPLICATION_JSON)
    public void receiveRoute (JsonObject data)
    {
        Route routeToAdd = jsonToJavaObj(data);
        DbCommunication.addRouteToDb(routeToAdd);
    }
    
    @Path("search_route_by_id/{routeId}")
    @GET
    @Produces("text/plain")
    public String SearchRouteById (@PathParam("routeId") String routeId)
    {
        Route routeFromDb = DbCommunication.getRouteFromDb(routeId);
        return javaObjToJson(routeFromDb).toString();
    }
    
    @Path("search_route_by_city/{cityName}")
    @GET
    @Produces("text/plain")
    public String SearchRouteByCityName (@PathParam("cityName") String cityName)
    {
        Route wektorTras [] = DbCommunication.getRouteFromDbByCityName(cityName);
        //return javaObjToJson(wektorTras).toString();routeId
        return "ok";
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
    
    static public JsonObject javaObjToJson (Route routeToConvert)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for(int i =0;i<routeToConvert.number_of_places;i++) {
        arrayBuilder.add(routeToConvert.miejsca[i].place_name);
        }
        
        JsonArray miejsca = arrayBuilder.build();
        
        
      JsonObject jsonToSend = Json.createObjectBuilder()
              .add("city_name", routeToConvert.city_name)
              .add("route_id",routeToConvert.route_id)
              .add("route_length_km",routeToConvert.route_length_km)
              .add("estimated_walk_time_in_mins",routeToConvert.estimated_walk_time_in_mins)
              .add("number_of_places", routeToConvert.number_of_places)
              .add("miejsca",miejsca)
              .build();
     return jsonToSend;
    } 
    
}
