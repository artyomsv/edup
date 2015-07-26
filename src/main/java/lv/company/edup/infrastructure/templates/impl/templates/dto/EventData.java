package lv.company.edup.infrastructure.templates.impl.templates.dto;

import lv.company.edup.persistence.subjects.view.AttendanceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventData {

    private Long xCoordinate;
    private Long xWidth;
    private String date;
    private List<TimeData> times;

    public EventData(Long xCoordinate, String date, List<TimeData> times) {
        this.xCoordinate = xCoordinate;
        this.date = date;
        this.times = times;
    }

    public Long getxCoordinate() {
        return xCoordinate;
    }

    public String getDate() {
        return date;
    }

    public List<TimeData> getTimes() {
        return times;
    }

    public Long getxWidth() {
        return times.size() * 40L;
    }

    public EventData copyObject(Map<Long, AttendanceView> studentAttendance) {
        ArrayList<TimeData> timeDatas = new ArrayList<>();
        for (TimeData time : times) {
            TimeData clone = time.copyObject(studentAttendance.get(time.getEventId()));
            timeDatas.add(clone);
        }

        return new EventData(xCoordinate, date, timeDatas);
    }
}
