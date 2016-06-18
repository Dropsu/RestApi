package ds;



import ds.route.Place;
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
 * ApiCommunication class is responsible for responding to HTTP requests. It also translates Routes between JSON format and Java objects. JSON is used
 * for cummunication via http, while java objects are used within the application. 
 * @author Dropsu
 */
@Path("") // path do wejscia do klasy
public class ApiCommunication {
    
    @GET // kazdy path moze miec tylko po jednej motodzie GET, POST itd.
    /**
     * Returns shortened, suited to API user documentation.
     * @return {String} Documentation
     */
    @Produces(MediaType.TEXT_HTML)
    public String getDocumentation() { 
        return "<h1>Api Documentation </h1>"
                + "<p>Avaliable methods:</p>"
                + "<h3>1) Search for route by Route_name name</h3>"
                + ".../api/search_route_by_id/{routeId}<br>"
                + "Input: 'routeId' -> Name of route to search for<br>"
                + "Result: route as JSON"
                + "<h3>2) Search for routes by City_name</h3> "
                + ".../api/search_route_by_city/{city_name, country_name}<br>"
                + "Input: ex1: Rzeszow, Polska ex2: Warszawa, Polska ex3: Warszawa, Komorow, Polska ."
                + "Check google maps autocomplition for city_name if needed<br>"
                + "Result: Routes in given city as JSON";
              
                
    }
    
    /**
     * Receveives Route(JSON) via http and using {@link ApiCommunication#jsonToJavaObj jsonToJavaObj} and
     * {@link DbCommunication#addRouteToDb addRouteToDb} adds it to DataBase. 
     * @param data
     * @return {String} operationStatus
     */
    @Path ("/send_route")
    @POST
    @Produces("text/plain")
    @Consumes(MediaType.APPLICATION_JSON)
    public String receiveRoute (JsonObject data)
    {
        Route routeToAdd = jsonToJavaObj(data);
        DbCommunication.addRouteToDb(routeToAdd);
        return "Dodano Trase";
    }
    
    /**
     * Returns requested by route_id Route (JSON). Uses {@link ApiCommunication#javaObjToJson(ds.route.Route) javaObjToJson}
     * for conversion and {@link DbCommunication#getRouteFromDb(java.lang.String) getRouteFromDb for accesing database}.
     * This method can be accesed by making a HTTP request to: .../api/search_route_by_id/{{@link Route#route_id route_id}}
     * @param routeId
     * @return {JSON String} requestedRoute 
     */
    @Path("search_route_by_id/{routeId}")
    @GET
    @Produces("text/plain")
    public String SearchRouteById (@PathParam("routeId") String routeId)
    {
        Route routeFromDb = DbCommunication.getRouteFromDb(routeId);
        return javaObjToJson(routeFromDb).toString();
    }
    
    /**
     * Returns all Routes(JSON array) in requested City.
     * Uses {@link DbCommunication#getRouteFromDbByCityName(java.lang.String) getRouteFromDbByCityName}
     * then converts result with {@link ApiCommunication#routesArrayToJsonArray(ds.route.Route[]) routesArrayToJsonArray}
     * This method can be accesed by making a HTTP request in an ULR: .../api/search_route_by_city/{{@link Route#city_name city_name}}
     * @param cityName
     * @return {JSON Array String} routesArray
     */
    @Path("search_route_by_city/{cityName}")
    @GET
    @Produces("text/plain")
    public String SearchRouteByCityName (@PathParam("cityName") String cityName)
    {
        Route wektorTras [] = DbCommunication.getRouteFromDbByCityName(cityName);
        return routesArrayToJsonArray(wektorTras).toString();
    }
    
    /**
     * Converts Route from JSON format to java object. 
     * @param data
     * @return {Route} route
     */
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
    
    /**
     * Converts Route java object to JSON format. 
     * @param routeToConvert
     * @return {JSON String} routeAsJson
     */
    static public JsonObject javaObjToJson (Route routeToConvert)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for(int i =0;i<routeToConvert.number_of_places;i++) 
            arrayBuilder.add(routeToConvert.places[i].place_name);
        
        
        JsonArray miejsca = arrayBuilder.build();
        
        
        JsonObject createdJson = Json.createObjectBuilder()
              .add("city_name", routeToConvert.city_name)
              .add("route_id",routeToConvert.route_id)
              .add("route_length_km",routeToConvert.route_length_km)
              .add("estimated_walk_time_in_mins",routeToConvert.estimated_walk_time_in_mins)
              .add("number_of_places", routeToConvert.number_of_places)
              .add("miejsca",miejsca)
              .build();
     return createdJson;
    } 
   
    /**
     * Convert java array of Route objects to JSON array format. 
     * @param wekTras
     * @return {JSON Array} routesArray
     */
    static public JsonArray routesArrayToJsonArray (Route [] wekTras)
    {
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Route trasa : wekTras)
            arrayBuilder.add(javaObjToJson(trasa));
          
        JsonArray JArray = arrayBuilder.build();
        return JArray;
    }
    
}
