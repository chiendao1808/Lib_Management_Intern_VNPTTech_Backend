package com.example.intern_vnpttech_libmanagement.utilities;

import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Calendar;

@UtilityClass
public class DateAndTimeUtils {

    public static Calendar calendar = Calendar.getInstance();

    // convert a Timestamp to calendar
    @NotNull
    public static Calendar covertTimestampToCalendar(Timestamp ts)
    {
      //  Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        return calendar;
    }

    // check if a timestamp in a month/year
    public static boolean inMonthCheck(Timestamp ts, int month, int year)
    {
        calendar.setTime(ts);
        if(calendar.get(Calendar.MONTH+1)==month && calendar.get(Calendar.YEAR)==year)
            return true;
        else return false;
    }
}
