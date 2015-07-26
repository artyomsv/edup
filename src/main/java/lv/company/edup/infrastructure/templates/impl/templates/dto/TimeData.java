package lv.company.edup.infrastructure.templates.impl.templates.dto;

public class TimeData {

    private Long xCoordinate;
    private String time;
    private String participated;

    public TimeData(Long xCoordinate, String time) {
        this.xCoordinate = xCoordinate;
        this.time = time;
    }

    public TimeData(Long xCoordinate, String time, String participated) {
        this.xCoordinate = xCoordinate;
        this.time = time;
        this.participated = participated;
    }

    public Long getxCoordinate() {
        return xCoordinate;
    }

    public String getTime() {
        return time;
    }

    public String getParticipated() {
        return participated;
    }

    public TimeData clone() {
        return new TimeData(xCoordinate, "");
    }
}
