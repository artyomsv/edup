package lv.company.edup.resources.secured.logout;

import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;

@Stateless
@Path("private/logout")
public class LogoutResource {

    @Context UriInfo uriInfo;

    @POST
    public Response logout(@Context HttpServletRequest request,
                           @Context HttpServletResponse response) throws ServletException, IOException {
        request.logout();
        URI requestUri = uriInfo.getRequestUri();
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme()).append("://").append(requestUri.getAuthority()).append("/edup");
        response.sendRedirect(builder.toString());
        return Response.ok().build();
    }

}
