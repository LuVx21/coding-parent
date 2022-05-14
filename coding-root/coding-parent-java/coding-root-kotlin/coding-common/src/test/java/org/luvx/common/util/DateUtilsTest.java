package org.luvx.common.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import io.vavr.API;

class DateUtilsTest {
    @Test
    public void m1() {
        LocalDate date = LocalDate.of(2022, 11, 11);
        LocalDate now = LocalDate.now();
        API.println(
                // DateUtils.daysBetween(ChronoUnit.DAYS, now, date),
                // DateUtils.daysBetween(ChronoUnit.YEARS, now, date)
                DateUtils.getCurrentMonthDateTime()
        );
    }
}