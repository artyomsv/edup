package lv.company.edup.resources.secured.reports;

import lv.company.edup.resources.ApplicationFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@ApplicationScoped
public class ReportsFacade extends ApplicationFacade {

    private Logger logger = Logger.getLogger(ReportsFacade.class.getSimpleName());

    public Response streamReport() {
        return null;
    }

}
