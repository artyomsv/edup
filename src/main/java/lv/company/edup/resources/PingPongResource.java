package lv.company.edup.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("ping")
public class PingPongResource {

    @GET
    @Path("pong")
    public String pong() {
        return "PONG!!!";
    }

}
