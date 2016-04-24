/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds.db.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import ds.db.DBconnection;

/**
 *
 * @author Dropsu
 */
@Path("/dbase/import_test")
public class DbImportTest {
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public static String test ()
    {
        String imie;
        Connection con = DBconnection.pol();
        
        try {
         Statement stmt = con.createStatement( ); // tworzy obekt "zdanie" przywiazany do wskazanego polaczenia
                String SQL = "SELECT * FROM Championy"; // zmienna SQL przechowuje SQL...
                ResultSet rs = stmt.executeQuery( SQL ); //zmienna rs przechowuje dane pobrane z bazy, ta linijka pobiera je
                
                rs.next(); //rs posiada wskaznik wskazujacy na poczatku 1 pozycje przed pierwszym pobranym rekordem
                imie = rs.getString("imie");
        }catch (SQLException err)
        {
            return err.getMessage();
        }
        return imie;
    }
}
