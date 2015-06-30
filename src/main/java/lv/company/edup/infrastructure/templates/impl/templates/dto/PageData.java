package lv.company.edup.infrastructure.templates.impl.templates.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PageData {

    private String pageTitle;
    private String host;

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
