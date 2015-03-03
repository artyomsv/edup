package lv.company.edup.services.ping.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PingDto {

    private String app;
    private String version;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
