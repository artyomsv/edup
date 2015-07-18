package lv.company.edup.faker;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;

public class FakerTest {

    @Test
    public void testFake() throws Exception {
        Faker faker = new Faker(Locale.ENGLISH);
        assertNotNull(RandomUtils.nextInt(28000000, 29000000));

    }
}
