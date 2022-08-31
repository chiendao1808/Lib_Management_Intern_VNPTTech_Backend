package com.example.intern_vnpttech_libmanagement.utilities;

import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Calendar;

@UtilityClass
public class DateAndTimeUtils {


    // convert a Timestamp to calendar
    @NotNull
    public static Calendar covertTimestampToCalendar(Timestamp ts)
    {
      //  Calendar calendar = Calendar.getInstance();
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(ts);
        return calendar;
    }

    // check if a timestamp in a month/year
    // option = 1 -> monthly, option = 2 -> yearly
    public static boolean inTimeCheck(Timestamp ts, int month, int year,int option)
    {
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(ts);
        if(option==1){
            return calendar.get(Calendar.MONTH)+1==month && calendar.get(Calendar.YEAR)==year;
        }
        else return calendar.get(Calendar.YEAR) == year;
    }
}
