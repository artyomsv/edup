package lv.company.edup.infrastructure.templates.impl.templates;

import lv.company.edup.infrastructure.templates.api.ContextCreator;
import lv.company.edup.infrastructure.templates.impl.templates.dto.EventData;
import lv.company.edup.infrastructure.templates.impl.templates.dto.EventPlanningJournal;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventPlanningJournalContextCreator implements ContextCreator<EventPlanningJournal> {

    @Override
    public Map<String, Object> create(EventPlanningJournal from) {
        Map<String, Object> map = new HashMap<>();

        map.put("journalContextEventName", from.getEventName());
        map.put("journalContextEvents", CollectionUtils.isNotEmpty(from.getEvents()) ? from.getEvents() : Collections.emptyList());
        map.put("journalContextStudents", CollectionUtils.isNotEmpty(from.getStudents()) ? from.getStudents() : Collections.emptyList());
        map.put("journalContextDetailsBandHeight", CollectionUtils.size(from.getStudents()) * 20L);
        map.put("journalContextStudentsUuid", new UUID(0L, 0L));

        long count = 0;
        for (EventData eventData : from.getEvents()) {
            count += CollectionUtils.size(eventData.getTimes());
        }

        map.put("journalContextColumnsWidth", count * 40L);

        return map;
    }
}
