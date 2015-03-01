package lv.company.edup.resources.logout;

import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.MalformedURLException;
import java.net.URI;

@Path("logout")
@Stateless
public class LogoutResource {

    @Context UriInfo uriInfo;

    @POST
    public Response logout(@Context HttpServletRequest request,
                           @Context HttpServletResponse response) throws ServletException, MalformedURLException {
        request.logout();
        URI requestUri = uriInfo.getRequestUri();
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme()).append("://").append(requestUri.getAuthority()).append("/edup");
        return Response.seeOther(URI.create(builder.toString())).build();
    }

}
