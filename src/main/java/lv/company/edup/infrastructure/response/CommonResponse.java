package lv.company.edup.infrastructure.response;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

@XmlRootElement
public class CommonResponse {

    private Collection<?> payload = new ArrayList();

    public Collection<?> getPayload() {
        return payload;
    }

    public void setPayload(Collection<?> payload) {
        this.payload = payload;
    }
}
