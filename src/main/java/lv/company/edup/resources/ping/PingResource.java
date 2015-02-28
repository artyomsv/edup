package lv.company.edup.resources.ping;

import lv.company.edup.common.configuration.Bundle;
import lv.company.edup.common.configuration.PropertiesMapping;
import lv.company.edup.common.configuration.ResourceBundle;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ping")
public class PingResource {

    public static final String APP_NAME = "Educational planning application";

    @Inject @Bundle("version") private ResourceBundle bundle;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response pong() {
        String version = bundle.get(PropertiesMapping.Version.VERSION);
        return Response.ok(buildHtml(version, APP_NAME)).build();
    }

    @GET
    @Produces({MediaType.TEXT_HTML})
    public Response pongHtml() {
        String version = bundle.get(PropertiesMapping.Version.VERSION);
        return Response.ok(getMessage(version)).build();
    }

    private String getMessage(String version) {
        return APP_NAME + " version: " + version;
    }

    private String buildHtml(String version, String appName) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n");
        builder.append("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        builder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        builder.append("<head>");
        builder.append("<title>");
        builder.append(appName);
        builder.append("</title>");
        builder.append("</head>");
        builder.append("<body>");
        builder.append("<h2>");
        builder.append(getMessage(version));
        builder.append("</h2>");
        builder.append("<p>");
        builder.append("To open application please click  <a href=\"/edup/app/index.html\">EDUP</a>");
        builder.append("</p>");
        builder.append("</body>");
        builder.append("</html>");
        return builder.toString();
    }

}
