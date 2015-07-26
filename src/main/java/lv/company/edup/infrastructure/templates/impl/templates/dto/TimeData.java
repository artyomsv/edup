package lv.company.edup.infrastructure.templates.impl.templates.dto;

import lv.company.edup.persistence.subjects.view.AttendanceView;

public class TimeData {

    private Long xCoordinate;
    private String time;
    private String participated;
    private Long eventId;
    private String backColor;

    public TimeData(Long xCoordinate, String time, Long eventId) {
        this.xCoordinate = xCoordinate;
        this.time = time;
        this.eventId = eventId;
    }

    public TimeData(Long xCoordinate, String time, String participated, Long eventId) {
        this.xCoordinate = xCoordinate;
        this.time = time;
        this.participated = participated;
        this.eventId = eventId;
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

    public TimeData copyObject(AttendanceView attendance) {
        Boolean participated = attendance != null ? attendance.getParticipated() : null;
        String value = participated != null ? participated ? "V" : "-" : "";

        switch (value) {
            case "V":
                backColor = "#CCFFCC";
                break;
            case "-":
                backColor = "#FFC4C4";
                break;
            default:
                backColor = "#FFFFFF";
                break;
        }
        TimeData timeData = new TimeData(xCoordinate, "", value, eventId);
        timeData.setBackColor(backColor);
        return timeData;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }
}
