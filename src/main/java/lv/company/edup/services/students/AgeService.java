package lv.company.edup.services.students;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AgeService {

    public static Long getAge(Date birthDate) {
        if (birthDate == null) {
            return null;
        }
        Instant instant = birthDate.toInstant();
        LocalDate birthLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        long daysSinceBirth = now.toEpochDay() - birthLocalDate.toEpochDay();
        return daysSinceBirth / 365;


    }

}
