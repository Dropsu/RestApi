/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds.route;

/**
 * Describes Place. For database organization purposes, has fields associating it to Route it is included in such as 
 * {@link Place#route_id} and {@link Place#index_number_in_route}.
 * @author Dropsu
 */
public class Place {
    
    /**
     * Holds place name corresponding to GoogleMapsApi "formatted_adress" for places.
     */
    public String place_name;

    /**
     * Hold's position in of place in route, necessery for database organization purposes.
     */
    public int index_number_in_route;

    /**
     ** Hold's Route's consisting given place name, necessery for database organization purposes.
     */
    public String route_id;
    
    /**
     *
     * @param place_name
     * @param index_number_in_route
     * @param route_id
     */
    public Place (String place_name, int index_number_in_route,String route_id)
    {
        this.place_name = place_name;
        this.index_number_in_route = index_number_in_route;
        this.route_id = route_id;
    }
}
