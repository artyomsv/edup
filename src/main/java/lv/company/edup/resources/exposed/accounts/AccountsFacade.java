package lv.company.edup.resources.exposed.accounts;

import lv.company.edup.resources.AbstractFacade;
import lv.company.edup.services.accounts.AccountsService;
import lv.company.edup.services.accounts.dto.AccountDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.ws.Response;

@ApplicationScoped
public class AccountsFacade extends AbstractFacade {

    @Inject private AccountsService accountsService;

    public Response createAccount(AccountDto dto) {
        return null;
    }
}
