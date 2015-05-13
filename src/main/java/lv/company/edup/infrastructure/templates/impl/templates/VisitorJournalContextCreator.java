package lv.company.edup.infrastructure.templates.impl.templates;

import lv.company.edup.infrastructure.templates.api.ContextCreator;
import lv.company.edup.infrastructure.templates.impl.templates.dto.VisitingJournalDto;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class VisitorJournalContextCreator implements ContextCreator<VisitingJournalDto> {

    @Override
    public Map<String, Object> create(VisitingJournalDto from) {
        Map<String, Object> map = new HashMap<>();

        map.put("journalSubject", from.getSubject());
        map.put("journalDate", from.getDate());
        map.put("journalTime", from.getTime());
        map.put("journalStudents", from.getStudents());

        return map;
    }
}
