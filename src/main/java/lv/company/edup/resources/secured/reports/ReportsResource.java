package lv.company.edup.resources.secured.reports;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@ApplicationScoped
@Path("private/reports")
public class ReportsResource {

    @Inject ReportsFacade facade;

    @GET
    @Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getReport() {
        return facade.streamReport();
    }
}
