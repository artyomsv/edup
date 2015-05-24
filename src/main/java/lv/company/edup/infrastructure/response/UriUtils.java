package lv.company.edup.infrastructure.response;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.net.URI;

@Provider
@RequestScoped
public class UriUtils {

    @Context UriInfo info;

    public String getResourceUrl() {
        return String.valueOf(info.getAbsolutePath());
    }

    public String getRootUrl() {
        String path = info.getPath().replaceAll(" ", "%20");
        String full = String.valueOf(info.getAbsolutePath());
        int end = full.lastIndexOf(path);
        return full.substring(0, end);
    }

    public URI buildCreated(Long id) {
        return info.getAbsolutePathBuilder().build(id);
    }

    public MultivaluedMap<String, String> getQueryParameters() {
        return info.getQueryParameters(true);
    }
}
