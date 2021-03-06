package lv.company.edup.resources.exposed.ping;

import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.ping.PingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class PingFacade extends ApplicationFacade {

    @Inject private PingService service;

    public Response buildDto() {
        return ok(service.getDto());
    }

    public Response buildHtml() {
        return ok(service.getHtml());
    }
}
