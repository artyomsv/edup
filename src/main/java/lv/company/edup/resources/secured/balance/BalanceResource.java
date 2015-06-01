package lv.company.edup.resources.secured.balance;

import lv.company.edup.services.students.dto.StudentBalanceDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("private/balance")
@Produces(MediaType.APPLICATION_JSON)
public class BalanceResource {

    @Inject BalanceFacade facade;

    @GET
    public Response search() {
        return facade.search();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveBalance(StudentBalanceDto dto) {
        return facade.saveBalance(dto);
    }

    @DELETE
    @Path("{id}")
    public Response deleteBalance(@PathParam("id") Long id) {
        return facade.deactivateBalanceRecord(id);
    }

}
