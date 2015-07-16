package pl.marek1and.myworktime.db;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConversion {

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static Date parseDate(String time) {
        Date date = null;
        if(time != null) {
            DateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
            try {
                date = format.parse(time);
            } catch (ParseException e) {
                //TODO Enhance this log message
                Log.d("", e.getMessage());
            }
        }
        return date;
    }

    public static String formatDateTime(Date date) {
        if(date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        String d = sdf.format(date);
        return d;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String d = sdf.format(date);
        return d;
    }
}
