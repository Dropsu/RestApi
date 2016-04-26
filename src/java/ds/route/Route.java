/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds.route;
import javax.json.*;

/**
 *
 * @author Dropsu
 */
public class Route {
    
    String route_id;
    String city_name;
    float route_length_km;
    int estimated_walk_time_in_mins;
    int number_of_places;
    Place miejsca [];
    
    Route (String route_id,
    String city_name,
    float route_length_km,
    int estimated_walk_time_in_mins,
    int number_of_places,
    Place miejsca [],
    JsonArray jsonMiejsca)
    {
    this.route_id = route_id;
    this.city_name = city_name;
    this.route_length_km=route_length_km;
    this.estimated_walk_time_in_mins = estimated_walk_time_in_mins;
    this.number_of_places = number_of_places;
    
    for (int i=0; i<jsonMiejsca.size();i++)
    {
        miejsca[i].place_name=jsonMiejsca.getString(i);
        miejsca[i].index_number_in_route=i;
        miejsca[i].route_id = this.route_id;
    }
    }
 
}
