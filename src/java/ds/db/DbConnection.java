/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Dropsu
 */
@Path("/dbase")
public class DbConnection {
    
    public static String status_polaczenia;
    
    public static Connection pol()
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
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String test_polaczenia ()
    {
        pol();
        return status_polaczenia;
    }
    
}