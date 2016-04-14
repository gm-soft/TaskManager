package io.github.maximgorbatyuk.taskmanager.help;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Maxim on 11.04.2016.
 */
public class DateHelper {

    public Date parseDate(String date){
        try {
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
            return format.parse(date);

        } catch (Exception ex){
            Log.d(Constants.LOG_TAG, "Error while parsing date from str: " + ex.getMessage());
            return null;
        }
    }

    public String dateToString(Date date){
        try {
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
            String result = format.format(date);
            return result;

        } catch (Exception ex){
            Log.d(Constants.LOG_TAG, "Error while parsing date to str: " + ex.getMessage());
            return "";
        }
    }

    public String DateToShortString(Date date){
        try {
            DateFormat format = new SimpleDateFormat("dd.MM", Locale.ENGLISH);
            return format.format(date);

        } catch (Exception ex){
            return "No date";
        }
    }

    public String getFormatDifference(long dif){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("England"));
        return format.format(new Date(dif));
    }

}
