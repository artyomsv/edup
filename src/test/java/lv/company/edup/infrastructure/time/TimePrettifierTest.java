package lv.company.edup.infrastructure.time;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.core.Is.is;

public class TimePrettifierTest {

    @Test
    public void zeroTime() throws Exception {
        String pretty = TimePrettifier.pretty(0L);
        Assert.assertThat(pretty, is("0 ms"));
    }

    @Test
    public void nullTime() throws Exception {
        String pretty = TimePrettifier.pretty(null);
        Assert.assertThat(pretty, is("0 ms"));
    }

    @Test
    public void milliseconds() throws Exception {
        Date time = DateUtils.addMilliseconds(new Date(0), 800);

        String pretty = TimePrettifier.pretty(time);
        Assert.assertThat(pretty, is("800 ms"));
    }

    @Test
    public void seconds() throws Exception {
        Date time = DateUtils.addSeconds(new Date(0), 1);

        String pretty = TimePrettifier.pretty(time);
        Assert.assertThat(pretty, is("1 sec"));
    }

    @Test
    public void minutesAndSeconds() throws Exception {
        Date time = DateUtils.addMinutes(new Date(0), 15);
        time = DateUtils.addSeconds(time, 14);

        String pretty = TimePrettifier.pretty(time);
        Assert.assertThat(pretty, is("15 minutes 14 sec"));
    }

    @Test
    public void minutesAndSecondsAndMillis() throws Exception {
        Date time = DateUtils.addMinutes(new Date(0), 8);
        time = DateUtils.addSeconds(time, 10);
        time = DateUtils.addMilliseconds(time, 1);

        String pretty = TimePrettifier.pretty(time);
        Assert.assertThat(pretty, is("8 minutes 10 sec 1 ms"));
    }

    @Test
    public void hoursMinutesSecondsMillis() throws Exception {
        Date time = DateUtils.addHours(new Date(0), 1);
        time = DateUtils.addMinutes(time, 8);
        time = DateUtils.addSeconds(time, 10);
        time = DateUtils.addMilliseconds(time, 1);

        String pretty = TimePrettifier.pretty(time);
        Assert.assertThat(pretty, is("1 hours 8 minutes 10 sec 1 ms"));
    }
}