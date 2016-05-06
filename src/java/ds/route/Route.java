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
    
    public String route_id;
    public String city_name;
    public String route_length_km;
    public int estimated_walk_time_in_mins;
    public int number_of_places;
    public Place miejsca [];
    
    
    public Route (String route_id,
    String city_name,
    String route_length_km,
    int estimated_walk_time_in_mins,
    int number_of_places,
    JsonArray jsonMiejsca)
    {
    this.route_id = route_id;
    this.city_name = city_name;
    this.route_length_km=route_length_km;
    this.estimated_walk_time_in_mins = estimated_walk_time_in_mins;
    this.number_of_places = number_of_places;
    miejsca = new Place [number_of_places];
    
    for (int i=0; i<jsonMiejsca.size();i++)
    {
        Place nowe = new Place(jsonMiejsca.getString(i),i,this.route_id);
        this.miejsca[0] = nowe;
    }
    }
    
}
