package ds;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

@Path("") // path do wejscia do klasy
public class GenericResource {


    public GenericResource() {
    }

  
    
    @GET // kazdy path moze miec tylko po jednej motodzie GET, POST itd.
    @Produces(MediaType.TEXT_HTML)
    public String getVerification() {  //weryfikacja
        return "Wszystko jest fine - REST";
    }

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
        
}
