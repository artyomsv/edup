package lv.company.edup.mappers;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.util.Locale;

public class FakerTest {

    @Test
    public void testFake() throws Exception {
        Faker faker = new Faker(Locale.ENGLISH);
        System.out.println(RandomUtils.nextInt(28000000, 29000000));

    }
}
