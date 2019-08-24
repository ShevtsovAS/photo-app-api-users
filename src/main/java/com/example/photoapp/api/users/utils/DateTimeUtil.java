package com.example.photoapp.api.users.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeUtil {

    public static Date getDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static Date getDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static LocalDateTime getLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDate getLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}
