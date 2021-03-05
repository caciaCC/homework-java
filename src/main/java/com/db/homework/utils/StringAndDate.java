package com.db.homework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StringAndDate {
    public static Date getDetailedDate(String s) throws ParseException {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        return sdf.parse(s);
    }
    public static String getDetailedString(Date date) throws ParseException {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        return sdf.format(date);
    }
    public static Date getSimpleDate(String s) throws ParseException {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd" );
        return sdf.parse(s);
    }
    public static String getSimpleString(Date date) throws ParseException {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd" );
        return sdf.format(date);
    }

    public static String getSimpleStringFromDetailedString(String s) throws ParseException {
        Date date = StringAndDate.getDetailedDate(s);
        return StringAndDate.getSimpleString(date);
    }

    public static Date getSimpleDateFromDetailedDate(Date date) throws ParseException {
        String s = StringAndDate.getSimpleString(date);
        return StringAndDate.getSimpleDate(s);
    }

    public static long getOverTime(String backDate, String dueDate) {
        long Days = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            long time1 = sdf.parse(backDate).getTime();
            long time2 = sdf.parse(dueDate).getTime();
            Days = (int) ((time1 -time2) /(24
                    * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Days;
    }
}
