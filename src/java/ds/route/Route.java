package ds.route;
import javax.json.*;

/**
 * Describes Route. Has {@link Place} array as one of its fields.
 * @author Dropsu
 */

public class Route {
    
    /**
     * Hold Route's name given by user.
     */
    public String route_id;

    /**
     *City name corresponding to GoogleMapsApi "places".
     */
    public String city_name;

    /**
     *Route length in km.
     */
    public String route_length_km;

    /**
     *Estimated walk time in minutes. Received via http ( my Turrest site uses formula: (distance*0.015) minutes +(miejsca.length*10) minutes)
     */
    public int estimated_walk_time_in_mins;

    /**
     * Number of places in the route {@link Route#places} array
     */
    public int number_of_places;

    /**
     * Array holding places {@link Place} in Route. 
     */
    public Place[] places;
    
    /**
     *
     * @param route_id
     * @param city_name
     * @param route_length_km
     * @param estimated_walk_time_in_mins
     * @param number_of_places
     * @param jsonMiejsca
     */
    public Route (String route_id,
    String city_name,
    String route_length_km,
    int estimated_walk_time_in_mins,
    int number_of_places,
    JsonArray jsonMiejsca)
    {
    this.route_id = route_id;
    this.city_name = city_name;
    this.route_length_km= route_length_km;
    this.estimated_walk_time_in_mins = estimated_walk_time_in_mins;
    this.number_of_places = number_of_places;
    
    places = new Place [number_of_places];
    
    for (int i=0; i<jsonMiejsca.size();i++)
    {
        Place nowe = new Place(jsonMiejsca.getString(i),i,this.route_id);
        this.places[i] = nowe;
    }
    }
    
    /**
     *
     * @param route_id
     * @param city_name
     * @param route_length_km
     * @param estimated_walk_time_in_mins
     * @param number_of_places
     * @param miejsca
     */
    public Route (String route_id,
    String city_name,
    String route_length_km,
    int estimated_walk_time_in_mins,
    int number_of_places,
    Place miejsca [])
    {
    this.route_id = route_id;
    this.city_name = city_name;
    this.route_length_km= route_length_km;
    this.estimated_walk_time_in_mins = estimated_walk_time_in_mins;
    this.number_of_places = number_of_places;
    this.places = miejsca;
    }
    
}
