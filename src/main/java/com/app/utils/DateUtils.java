package com.app.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class DateUtils {

    public static final String FORMAT_YEAR_MONTH = "yyyy-MM-dd";
    public static final String FORMAT_DAY_MONTH_YEAR = "dd-MM-yyyy";
    public static final String FORMAT_YYYY_MONTH_DAY_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MONTH_DAY_MILLI_SEC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public String getFormatterFormat(Date date, String pattern, String timezone) {
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(pattern, Locale.getDefault());
        dateFormat2.setTimeZone(TimeZone.getTimeZone(timezone));
        return dateFormat2.format(date);
    }

    public String getFormatterFormat(String date, String pattern, String timezone) throws ParseException {
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(pattern);
        return getFormatterFormat(dateFormat2.parse(date), pattern, timezone);
    }


}
