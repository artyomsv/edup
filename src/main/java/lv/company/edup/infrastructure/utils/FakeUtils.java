package lv.company.edup.infrastructure.utils;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lv.company.edup.services.students.dto.StudentDto;
import lv.company.edup.services.subjects.dto.EventDto;
import lv.company.edup.services.subjects.dto.SubjectDto;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

@Deprecated
public class FakeUtils {

    @Deprecated
    public static StudentDto buildStudent() {
        Faker faker = new Faker();
        StudentDto dto = new StudentDto();
        Name name = faker.name();
        dto.setName(name.firstName());
        dto.setLastName(name.lastName());
        dto.setParentsInfo(faker.lorem().paragraph());
        dto.setCharacteristics(faker.lorem().paragraph());
        dto.setMail(faker.internet().emailAddress());

        int year = random(1995, 2010);
        int month = random(1, 12);
        int date = random(1, 28);

        String birthDate = String.format("%04d%02d%02d", year, month, date);

        dto.setPersonId(String.format("%02d%02d%04d-%5d", date, month, year, random(10000, 50000)));
        dto.setMobile(String.format("%s", random(28000000, 29000000)));
        try {
            dto.setBirthDate(DateUtils.parseDate(birthDate, "yyyyMMdd"));
        } catch (ParseException e) {

        }
        return dto;
    }

    @Deprecated
    private static Integer random(int from, int to) {
        return RandomUtils.nextInt(from, to);
    }

    @Deprecated
    /**
     * yyyymmdd
     */
    public static String getRandomStringDate(int from, int to) {
        StrBuilder strBuilder = new StrBuilder();
        strBuilder.append(RandomUtils.nextInt(from, to))
                .append(String.format("%2d", RandomUtils.nextInt(0, 11)))
                .append(String.format("%2d", RandomUtils.nextInt(0, 28)));
        return strBuilder.toString();
    }

    @Deprecated
    public static Date getRandomDate(int from, int to) {
        try {
            return DateUtils.parseDate(getRandomStringDate(from, to), "yyyyMMdd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    @Deprecated
    public static EventDto buildEvent(long subjectId) {
        EventDto dto = new EventDto();
        dto.setPrice(RandomUtils.nextLong(100, 1000));
        Date date = getRandomDate(2015, 2017);
        dto.setEventDate(date);
        int startHour = RandomUtils.nextInt(9, 16);
        dto.setFrom(setTime(date, startHour, 0));
        dto.setTo(setTime(date, startHour + 1, 0));
        SubjectDto subject = new SubjectDto();
        subject.setSubjectName("Fake event");
        subject.setSubjectId(subjectId);
        dto.setSubject(subject);
        return dto;
    }

    @Deprecated
    public static Date setTime(Date date, int hours, int minutes) {
        return DateUtils.addMinutes(DateUtils.addHours(date, hours), minutes);
    }
}
