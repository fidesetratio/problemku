package com.app;

import com.app.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

@SpringBootTest
public class DateFormatTest {

    @Autowired
    private DateUtils dateUtils;

    @Test
    public void testDateFormat() throws ParseException {
        Date start_date = dateUtils.getFormatterFormat("2020-01", DateUtils.YEAR_MONTH);
        Date end_date = dateUtils.getFormatterFormat("2020-12", DateUtils.YEAR_MONTH);
        LocalDate startDateLocaldate = dateUtils.convertToLocalDateViaMilisecond(start_date);
        LocalDate endDateLocaldate = dateUtils.convertToLocalDateViaMilisecond(end_date);
        System.out.println("tanggal output start date = " + startDateLocaldate);
        System.out.println("tanggal output end date = " + endDateLocaldate.plusMonths(1).minusDays(1));
    }
}
