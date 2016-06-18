
package ds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ds.route.*;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * DbCommunication class is responsible for all operations on database. It responds to request from {@link ApiCommunication}.
 */

public class DbCommunication {
    
    /**
     * Holds the status of the database connection or eventual errors.
     */
    public static String connectionStatus;
    
    /**
     * Establishes connection with database and returns it for other methods.
     * @return {Connection} variable holding connection
     */
    public static Connection establishConnection()
    {     
        Connection con = null;
        try {
            Class.forName ("oracle.jdbc.OracleDriver"); //laduje sterownik do OracleDatabase
        } catch (ClassNotFoundException e) {}
        try {
            con = DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:XE", "dropsu", "damiuq55" );
        } catch (SQLException err) {
              connectionStatus = err.getMessage();
            }
        return con;
    }
    
    /**
     * Adds Route to database. Uses SQL statements.
     * Places Route object to ROUTES table, and its places array to PLACES table.
     * For database organization see {@link Place}
     * Method called by {@link ApiCommunication#receiveRoute(javax.json.JsonObject) receiveRoute}
     * @param routeToAdd
     * @return
     */
    public static String addRouteToDb (Route routeToAdd)
    {
        Connection con = DbCommunication.establishConnection();
         try {
             Statement stmt = con.createStatement( ); // tworzy obekt "zdanie" przywiazany do wskazanego polaczenia

             String SQL = "INSERT INTO Routes VALUES ("
                     + "'"+routeToAdd.route_id +"',"
                     + "'"+routeToAdd.city_name +"',"
                     + routeToAdd.route_length_km+","
                     + routeToAdd.estimated_walk_time_in_mins+","
                     + routeToAdd.number_of_places
                     + ")"; 

             stmt.executeQuery( SQL ); 

             for(int i=0;i<routeToAdd.number_of_places;i++)
             {
                SQL = "INSERT INTO Places VALUES ("
                     + "'"+routeToAdd.places[i].place_name+"',"
                        + routeToAdd.places[i].index_number_in_route+","
                     + "'"+routeToAdd.places[i].route_id+"'"
                        + ")"; 
                stmt.executeQuery( SQL );
             }

             con.close();

            }catch (SQLException err) {return err.getMessage();}

        return "OK";
    }
    
    /**
     * Returns requested by {@link Route#route_id} Route from database.
     * Method is called by {@link ApiCommunication#SearchRouteById(java.lang.String) SearchRouteById}
     * @param routeId
     * @return {Route} Route
     */
    public static Route getRouteFromDb (String routeId)
    {
        Connection con = DbCommunication.establishConnection();
          
        try {
            Statement stmt = con.createStatement( ); 
            String SQL = "SELECT * FROM Routes WHERE Route_id=" + "'" + routeId + "'";
            ResultSet rsRoute = stmt.executeQuery( SQL ); 
            rsRoute.next();
    
    
            String route_id = rsRoute.getString("ROUTE_ID");
            String city_name = rsRoute.getString("CITY_NAME");
            String route_length_km = rsRoute.getString("ROUTE_LENGTH_KM");
            int estimated_walk_time_in_mins = rsRoute.getInt("ESTIMATED_WALK_TIME_IN_MINS");
            int number_of_places = rsRoute.getInt("NUMBER_OF_PLACES");
                
            //Przygotowywanie tablicy miejsc
       
            SQL = "SELECT * FROM Places WHERE Route_id="  + "'" + routeId + "'";
            ResultSet rsPlace = stmt.executeQuery( SQL );   
            rsPlace.next();

            Place miejsca [] = new Place [number_of_places];
               
            for (int i=0; i<number_of_places;i++) {
                Place nowe = new Place(rsPlace.getString("PLACE_NAME"),rsPlace.getInt("INDEX_NUMBER_IN_ROUTE"),rsPlace.getString("ROUTE_ID"));
                miejsca[i]=nowe;
                rsPlace.next();
            }
        
            Route generatedRoute = new Route(route_id,city_name,
                route_length_km,estimated_walk_time_in_mins,
                number_of_places,miejsca);
        
            con.close();
            return generatedRoute;                           
        } catch (SQLException err){return null;}   
           
    }
    
    /**
     * Returns routes array assosiated to given {@link Route#city_name} from database.
     * Method called by {@link ApiCommunication#SearchRouteByCityName(java.lang.String) SearchRouteByCityName}
     * @param CityName
     * @return
     */
    public static Route [] getRouteFromDbByCityName (String CityName)
    {
        // Funkcja jest bardzo długa, ale nie udało mi się jej rozbitć na użyteczne, funkcjonalnie odosobnione funkcje, ani użyć
          //getRouteFromDatabase z powodu sposobu przekazywania połączenia z bazą danych przez zmienną con. 
        Connection con = DbCommunication.establishConnection();
          
        try {
            // zliczanie ilosci tras
            Statement stmt = con.createStatement( ); 
            String SQL = "SELECT COUNT (*) FROM Routes WHERE City_name=" + "'" + CityName + "'";      
            ResultSet rsCount = stmt.executeQuery( SQL );
            rsCount.next();
            int rozmiar = rsCount.getInt("COUNT(*)");
            //pobiernie tras
            SQL = "SELECT * FROM Routes WHERE City_name=" + "'" + CityName + "'";
            ResultSet rsRoute = stmt.executeQuery( SQL );
               
        
            Route [] wektorTras = new Route [rozmiar];

            //wymagane z powodow implementacyjnych deklaracje
            String route_id = null;
            String city_name = null;
            String route_length_km = null;
            int estimated_walk_time_in_mins = 0;
            int number_of_places = 0;
            Place miejsca [] = null;

            //wektory zapewniajace komunikacje miedzy petlami obslugi tras i miejsc
            int [] numberOfPlacesInRoutes = new int [rozmiar]; 
            String [] routesIds = new String [rozmiar];
       
        
        
            // wpisywanie kolejnych tras do wektora tras (wszystkie pola uzupelniane oprocz pola "places")
            for (int j =0;j<rozmiar;j++) {
                rsRoute.next();

                route_id = rsRoute.getString("ROUTE_ID");
                city_name = rsRoute.getString("CITY_NAME");
                route_length_km = rsRoute.getString("ROUTE_LENGTH_KM");
                estimated_walk_time_in_mins = rsRoute.getInt("ESTIMATED_WALK_TIME_IN_MINS");
                number_of_places = rsRoute.getInt("NUMBER_OF_PLACES");

                numberOfPlacesInRoutes[j]=number_of_places;
                routesIds[j]=route_id;

                Route generatedRoute = new Route(route_id,city_name,
                    route_length_km,estimated_walk_time_in_mins,
                    number_of_places,miejsca);
                wektorTras[j] = generatedRoute;
            }
        
            //Uzupelniane dla kazdej trasy z wektora pola "places"
            for(int j = 0;j<rozmiar;j++) {
                SQL = "SELECT * FROM Places WHERE Route_id="  + "'" + routesIds[j] + "'";
                ResultSet rsPlace = stmt.executeQuery( SQL );   
                rsPlace.next();

                miejsca = new Place [numberOfPlacesInRoutes[j]];

                for (int i=0; i<numberOfPlacesInRoutes[j];i++) {
                    Place nowe = new Place(rsPlace.getString("PLACE_NAME"),rsPlace.getInt("INDEX_NUMBER_IN_ROUTE"),rsPlace.getString("ROUTE_ID"));
                    miejsca[i]=nowe;
                    rsPlace.next();
            }
            wektorTras[j].places = miejsca;
            }
            
            con.close();
            return wektorTras;                              
        }catch (SQLException err){return null;}  
    }
}