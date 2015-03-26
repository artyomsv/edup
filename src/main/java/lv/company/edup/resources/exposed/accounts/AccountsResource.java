package lv.company.edup.resources.exposed.accounts;

import lv.company.edup.services.accounts.dto.AccountDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.Response;

@Path("accounts")
@ApplicationScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AccountsResource {

    @Inject private AccountsFacade facade;

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createAccount(AccountDto dto) {
        return facade.createAccount(dto);
    }
}
