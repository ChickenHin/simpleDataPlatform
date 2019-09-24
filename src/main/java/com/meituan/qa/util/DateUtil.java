package com.meituan.qa.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date str_to_date = formatter.parse(strDate, pos);
        return str_to_date;
    }

    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static long strToTimeStamp(String strDate) {
        Date str_to_date = strToDate(strDate);
        return str_to_date.getTime()/1000;
    }
}
