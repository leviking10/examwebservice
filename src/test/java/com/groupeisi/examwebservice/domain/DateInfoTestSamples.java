package com.groupeisi.examwebservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DateInfoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DateInfo getDateInfoSample1() {
        return new DateInfo().id(1L).date("date1").dayOfWeek("dayOfWeek1");
    }

    public static DateInfo getDateInfoSample2() {
        return new DateInfo().id(2L).date("date2").dayOfWeek("dayOfWeek2");
    }

    public static DateInfo getDateInfoRandomSampleGenerator() {
        return new DateInfo()
            .id(longCount.incrementAndGet())
            .date(UUID.randomUUID().toString())
            .dayOfWeek(UUID.randomUUID().toString());
    }
}
