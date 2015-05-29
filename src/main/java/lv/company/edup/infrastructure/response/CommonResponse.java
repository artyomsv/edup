package lv.company.edup.infrastructure.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CommonResponse<T> {

    private T payload;

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
