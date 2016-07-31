package com.drucare.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ThirupathiReddy V
 *
 */
public class WyzbeeUtil {

    public static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String FORMAT_MONGO = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String TIMEZONE = TimeZone.getTimeZone("UTC").getID();

    /** Reference to logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(WyzbeeUtil.class);


    public static SimpleDateFormat dateFormatter() {
        final SimpleDateFormat formatter = new SimpleDateFormat(WyzbeeUtil.FORMAT);
        return formatter;
    }

    public static SimpleDateFormat dateFormatterMongo() {
        final SimpleDateFormat formatter = new SimpleDateFormat(WyzbeeUtil.FORMAT_MONGO);
        return formatter;
    }

    public static boolean isTodayDate(Date inputDate) {

        final Calendar inputCalendar = Calendar.getInstance();
        inputCalendar.setTime(inputDate);
        final Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(new Date());

        final boolean yearIsSame = inputCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR);
        final boolean monthIsSame = inputCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH);
        final boolean dateIsSame = inputCalendar.get(Calendar.DATE) == todayCalendar.get(Calendar.DATE);

        return yearIsSame && monthIsSame && dateIsSame;

    }

    public static boolean isPastDate(Date input) throws ParseException {

        return input.before(new Date());
    }

    public static boolean isPastDate(String input) throws ParseException {

        return dateFormatter().parse(input).before(new Date());
    }

    public static boolean isFutureDate(String input) throws ParseException {

        return dateFormatter().parse(input).after(new Date());
    }

    public static boolean isFutureDate(Date input) throws ParseException {

        return input.after(new Date());
    }


}
