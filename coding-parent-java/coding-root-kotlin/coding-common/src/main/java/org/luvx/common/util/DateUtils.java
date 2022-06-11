package org.luvx.common.util;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.annotation.Nullable;

public class DateUtils {
    private static final String            DATE_TIME_FORMAT    = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern(DATE_TIME_FORMAT)
            .withZone(ZoneId.systemDefault());

    private static final ZoneOffset ZONE = ZoneOffset.ofHours(8);

    private DateUtils() {
        throw new IllegalStateException();
    }

    public static Date m1(LocalDate date) {
        return Date.from(date.atStartOfDay(ZONE).toInstant());
    }

    public static long dateToTimestamp(LocalDate date) {
        return date.atStartOfDay(ZONE).toInstant().toEpochMilli();
    }

    public static long daysBetween(ChronoUnit chronoUnit, LocalDate fromDate, LocalDate toDate) {
        return chronoUnit.between(fromDate, toDate);
    }

    public static Date dateTimeToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZONE).toInstant());
    }

    public static LocalDate dateTimeToLocalDate(LocalDateTime dateTime) {
        return dateTime.toInstant(ZONE).atZone(ZONE).toLocalDate();
    }

    public static long dateTimeToTimestamp(LocalDateTime datetime) {
        return datetime.toInstant(ZONE).toEpochMilli();
    }

    public static LocalDate dateOfTimestamp(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZONE).toLocalDate();
    }

    public static LocalDateTime timestampToDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZONE).toLocalDateTime();
    }

    /**
     * 月第一天时间
     */
    public static ZonedDateTime getCurrentMonthDateTime() {
        return LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault());
    }

    public static LocalDateTime stringToDateTime(String timeStr) throws DateTimeParseException {
        return LocalDateTime.parse(timeStr, DATE_TIME_FORMATTER);
    }

    public static void m3(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZONE).toLocalDateTime();
        LocalDate localDate = date.toInstant().atZone(ZONE).toLocalDate();
    }

    /**
     * 计算日期{@code startDate}与{@code endDate}的间隔天数
     *
     * @return 间隔天数
     */
    public static long localDateCompare(LocalDate startDate, LocalDate endDate, ChronoUnit unit) {
        return startDate.until(endDate, unit);
    }

    /**
     * 字符串转换成日期
     *
     * @param day 日期字符串
     * @param pattern 日期的格式
     */
    @Nullable
    public static LocalDate stringToLocalDate(String day, String pattern) {
        return isBlank(day) ? null : LocalDate.parse(day, ofPattern(pattern));
    }
}
