/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds;

import ds.route.Route;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 *
 * @author Dropsu
 */
public class RouteConverter {
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
