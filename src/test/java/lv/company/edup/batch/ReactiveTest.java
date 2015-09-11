package lv.company.edup.batch;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import java.util.Arrays;
import java.util.Collection;

public class ReactiveTest {


    @Test
    public void testName1() throws Exception {

        String[] strings = {"1", "2"};
        Observable.from(strings).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });

    }

    @Test
    public void testName2() throws Exception {
        String[] strings = {"Hello", "20", "Good bye", "Welcome", "4", "Good morning", "5"};
        Collection<Long> coll = Arrays.asList(5L, 6L, 7L);

        Observable<Long> from = Observable.from(coll);
        Observable<Long> observable = Observable.from(strings)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return StringUtils.isNumeric(s);
                    }
                })
                .map(new Func1<String, Long>() {
                    @Override
                    public Long call(String s) {
                        return Long.valueOf(s);
                    }
                });

        Observable.zip(from, observable, new Func2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) {
                return aLong + aLong2;
            }
        }).skip(2)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long o) {
                        System.out.println(o);
                    }
                })


        ;

    }

    public class Student {
        int a;
        int b;

        public Student(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

}
