package lv.company.edup.infrastructure.templates.impl.templates.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FakturaData {

    private PageData page;

    public PageData getPage() {
        return page;
    }

    public void setPage(PageData page) {
        this.page = page;
    }
}
