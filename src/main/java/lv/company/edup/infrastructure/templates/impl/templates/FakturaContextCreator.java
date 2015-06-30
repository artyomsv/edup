package lv.company.edup.infrastructure.templates.impl.templates;

import lv.company.edup.infrastructure.templates.api.ContextCreator;
import lv.company.edup.infrastructure.templates.impl.templates.dto.FakturaData;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class FakturaContextCreator implements ContextCreator<FakturaData> {

    @Override
    public Map<String, Object> create(FakturaData from) {
        Map<String, Object> map = new HashMap<>();

        map.put("page", from.getPage());

        return map;
    }
}
