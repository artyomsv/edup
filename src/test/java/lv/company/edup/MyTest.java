package lv.company.edup;

import org.junit.Ignore;
import org.junit.Test;

import java.util.TimeZone;

public class MyTest {

    @Test
    @Ignore
    public void testName() throws Exception {

        TimeZone aDefault = TimeZone.getDefault();
        System.out.println(aDefault);
        System.out.println(TimeZone.getDefault().getID());
        System.out.println(TimeZone.getDefault().getDisplayName());
        System.out.println(TimeZone.getDefault().getRawOffset());


    }
}
