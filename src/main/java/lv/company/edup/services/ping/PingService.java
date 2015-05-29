package lv.company.edup.services.ping;

import lv.company.edup.infrastructure.configuration.ApplicationProperties;
import lv.company.edup.infrastructure.configuration.PropertiesMapping;
import lv.company.edup.services.ping.dto.PingDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PingService {

    @Inject private ApplicationProperties bundle;

    public PingDto getDto() {
        String version = bundle.get(PropertiesMapping.Application.VERSION);
        String name = bundle.get(PropertiesMapping.Application.NAME);
        return buildDto(version, name);
    }

    public String getHtml() {
        String version = bundle.get(PropertiesMapping.Application.VERSION);
        String name = bundle.get(PropertiesMapping.Application.NAME);
        return buildHtml(version, name);
    }

    private String getMessage(String version, String appName) {
        return appName + " version: " + version;
    }

    private PingDto buildDto(String version, String appName) {
        PingDto pingDto = new PingDto();
        pingDto.setApp(appName);
        pingDto.setVersion(version);
        return pingDto;
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
        builder.append(getMessage(version, appName));
        builder.append("</h2>");
        builder.append("<p>");
        builder.append("To open application please click  <a href=\"/edup/index.html\">EDUP</a>");
        builder.append("</p>");
        builder.append("</body>");
        builder.append("</html>");
        return builder.toString();
    }

}
