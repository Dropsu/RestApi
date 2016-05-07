/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import ds.route.*;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Dropsu
 */

public class DbConnection {
    
    public static String status_polaczenia;
    
    public static Connection establishConnection()
    {     
        Connection con = null;
    try {
        Class.forName ("oracle.jdbc.OracleDriver"); //laduje sterownik do OracleDatabase
    } catch (ClassNotFoundException e) {
    }
        
          try {
              status_polaczenia = "Polaczenie z baza danych smiga i buczy";
               con = DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:XE", "dropsu", "damiuq55" );
          }
          catch(SQLException err) {
            status_polaczenia = err.getMessage();
          }
          return con;
    }
    
    public String test_polaczenia ()
    {
        establishConnection();
        return status_polaczenia;
    }
    
    
    public static String addRouteToDb (Route routeToAdd)
    {
    Connection con = DbConnection.establishConnection();
     try {
         Statement stmt = con.createStatement( ); // tworzy obekt "zdanie" przywiazany do wskazanego polaczenia
                
         String SQL = "INSERT INTO Routes VALUES ("
                 + "'"+routeToAdd.route_id +"',"
                 + "'"+routeToAdd.city_name +"',"
                 + routeToAdd.route_length_km+","
                 + routeToAdd.estimated_walk_time_in_mins+","
                 + routeToAdd.number_of_places
                 + ")"; // zmienna SQL przechowuje SQL...
                
         stmt.executeQuery( SQL ); 
         
         for(int i=0;i<routeToAdd.number_of_places;i++)
         {
            SQL = "INSERT INTO Places VALUES ("
                 + "'"+routeToAdd.miejsca[i].place_name+"',"
                    + routeToAdd.miejsca[i].index_number_in_route+","
                 + "'"+routeToAdd.miejsca[i].route_id+"'"
                    + ")"; 
            stmt.executeQuery( SQL );
         }
         
         
         con.close();
                
        
        }catch (SQLException err)
        {
            return err.getMessage();
        }
    return "OK";
    }
    
    
    
}