/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds.route;

/**
 *
 * @author Dropsu
 */
public class Place {
    
    public String place_name;
    public int index_number_in_route;
    public String route_id;
    
    Place (String place_name, int index_number_in_route,String route_id)
    {
        this.place_name = place_name;
        this.index_number_in_route = index_number_in_route;
        this.route_id = route_id;
    }
}
