package com.telloquent.vms.utils;

import org.joda.time.DateTime;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {

    public static final String UNEXPIRED_DATE = "12-12-2999";

    public static String documentDateFormat = "dd-MM-yyyy";

    public static String todayStandardDateString(String dateFormat){
        DateTime date = DateTime.now().withTimeAtStartOfDay();
        return date.toString(dateFormat);
    }

    public static String todayPlusXStandardDateString(String dateFormat, Integer days){
        Date dt = new Date();
        DateTime dtOrg = new DateTime(dt);
        DateTime dtPlusX = dtOrg.plusDays(days);
        return dtPlusX.toString(dateFormat);
    }

    public static String todayMinusXStandardDateString(String dateFormat, Integer days){
        Date dt = new Date();
        DateTime dtOrg = new DateTime(dt);
        DateTime dtMinusX = dtOrg.minusDays(days);
        return dtMinusX.toString(dateFormat);
    }

    public static String standardDateString(String dateString){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        DateFormat newDateFormat = new SimpleDateFormat("MMM dd yy hh:mm a");
        String newDateString = null;
        try {
            Date startDate = df.parse(dateString);
            newDateString = newDateFormat.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  newDateString;
    }
}
