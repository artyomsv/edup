package lv.company.edup.services.students;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AgeService {

    private static final double DAY_IN_YEAR = 0.00273790926;

    public static Long getAge(Date birthDate) {
        if (birthDate != null) {
            Date today = new Date();
            long time = TimeUnit.MILLISECONDS.toDays(today.getTime() - birthDate.getTime());
            return (long) (time * DAY_IN_YEAR);
        }
        return null;
    }

}
