package lv.company.edup.resources.authentication;

import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.exceptions.InternalException;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Path("authentication")
public class AuthenticationResource {

    @Context UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Path("login")
    public Response loginUser(@Context HttpServletRequest request,
                              @Context HttpServletResponse response,
                              @FormParam("j_username") String userName,
                              @FormParam("j_password") String password) {
        try {
            request.login(userName, password);
            setRedirect(request, response);
            return Response.ok().build();
        } catch (ServletException e) {
            String message = e.getMessage();
            if (StringUtils.containsIgnoreCase(message, "login failed")) {
                throw new BadRequestException("Login failed");
            } else {
                throw new InternalException(e.getMessage(), e);
            }
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Path("logout")
    public Response logout(@Context HttpServletRequest request,
                           @Context HttpServletResponse response) throws ServletException, IOException {
        request.logout();
        URI requestUri = uriInfo.getRequestUri();
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme()).append("://").append(requestUri.getAuthority()).append("/edup");
        response.sendRedirect(builder.toString());
        return Response.ok().build();
    }


    private void setRedirect(HttpServletRequest request, HttpServletResponse response) {
        try {
            URI requestUri = uriInfo.getRequestUri();
            StringBuilder builder = new StringBuilder();
            builder.append(request.getScheme()).append("://").append(requestUri.getAuthority()).append("/edup/index.html");
            response.sendRedirect(builder.toString());
        } catch (IOException e) {
            throw new InternalException(e.getMessage(), e);
        }
    }
}
