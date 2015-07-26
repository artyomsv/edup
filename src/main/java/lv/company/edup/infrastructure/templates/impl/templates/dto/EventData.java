package lv.company.edup.infrastructure.templates.impl.templates.dto;

import java.util.ArrayList;
import java.util.List;

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

    public EventData clone() {
        ArrayList<TimeData> timeDatas = new ArrayList<>();
        for (TimeData time : times) {
            timeDatas.add(time.clone());
        }

        return new EventData(xCoordinate, date, timeDatas);
    }
}
