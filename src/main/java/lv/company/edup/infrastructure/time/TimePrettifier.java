package lv.company.edup.infrastructure.time;

import org.apache.commons.lang3.text.StrBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimePrettifier {

    private static final long HOUR = TimeUnit.HOURS.toMillis(1);
    private static final long MINUTE = TimeUnit.MINUTES.toMillis(1);
    private static final long SECOND = TimeUnit.SECONDS.toMillis(1);

    public static String pretty(Date time) {
        return pretty(time != null ? time.getTime() : 0L);
    }

    public static String pretty(long time) {
        long hours = TimeUnit.MILLISECONDS.toHours(time);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time - (HOUR * hours));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time - (HOUR * hours) - (MINUTE * minutes));
        long milliseconds = time - (HOUR * hours) - (MINUTE * minutes) - (SECOND * seconds);
        StrBuilder builder = new StrBuilder();
        if (hours > 0) {
            builder.append(hours).append(" hours").append(minutes > 0 || seconds > 0 || milliseconds > 0 ? " " : "");
        }
        if (minutes > 0) {
            builder.append(minutes).append(" minutes").append(seconds > 0 || milliseconds > 0 ? " " : "");
        }
        if (seconds > 0) {
            builder.append(seconds).append(" sec").append(milliseconds > 0 ? " " : "");
        }

        if (milliseconds > 0) {
            builder.append(milliseconds).append(" ms");
        }

        return builder.length() > 0 ? builder.toString() : "0 ms";
    }

}
