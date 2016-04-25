/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds.db;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
/**
 *
 * @author Dropsu
 */
@Path ("dbase/add_city_table")
public class DbAddCityTable {
    @GET
    @Produces("text/plain")
    public String addCity ()
    {
        Connection con = DbConnection.pol();
        try {
            String city = "Wroclaw";
        Statement stmt = con.createStatement();
            String SQL = "CREATE TABLE " + city +
                   "(id INTEGER not NULL, " + 
                   " PRIMARY KEY ( id ))";
        stmt.executeQuery(SQL);
            
        }
        catch (SQLException err) {
        return err.getMessage();
        }
        return "Dodano tablice miasta";
    }
}
