package com.telloquent.vms.utils;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Telloquent-DM6M on 9/5/2017.
 */

@SuppressLint({"DefaultLocale", "SimpleDateFormat", "UseValueOf"})
public final class Convert {
    private static final String TAG = Convert.class.getSimpleName();
    public static float ROUND_THRESHOLD = 0.5F;
    public static float CEIL_THRESHOLD = 0.99F;
    @SuppressLint({"SimpleDateFormat"})
    private static final SimpleDateFormat m_asOfDateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    private Convert() {
        throw new UnsupportedOperationException("Cannot construct an utility class");
    }

    public static String toString(Object source, String defaultValue) {
        String result;
        if(source == null) {
            result = defaultValue;
        } else if(source instanceof String) {
            result = (String)source;
        } else {
            result = "" + source;
        }
        return result;
    }

    public static String toString(Object source) {
        return toString(source, "--");
    }

    public static int toInt(Object source, int defaultValue) {
        int value = defaultValue;
        if(source != null) {
            if(source instanceof Number) {
                value = ((Number)source).intValue();
            } else {
                String valueString = toString(source);

                try {
                    value = Integer.parseInt(valueString.trim());
                } catch (NumberFormatException var5) {

                }
            }
        }
        return value;
    }

    public static int toInt(Object source) {
        return toInt(source, 0);
    }

    public static double toDouble(Object source, double defaultValue) {
        double value = defaultValue;
        if(source != null) {
            if(source instanceof Number) {
                value = ((Number)source).doubleValue();
            } else {
                String valueString = toString(source);

                try {
                    value = Double.parseDouble(valueString.trim());
                } catch (NumberFormatException var7) {
                    ;
                }
            }
        }

        return value;
    }

    public static double toDouble(Object source) {
        return toDouble(source, 0.0D);
    }

    public static JSONArray toJsonArray(int[] source, JSONArray defaultValue) {
        JSONArray result;
        if(source == null) {
            result = defaultValue;
        } else {
            result = new JSONArray();

            for(int i = 0; i < source.length; ++i) {
                result.put(source[i]);
            }
        }

        return result;
    }

    public static JSONArray toJsonArray(int[] source) {
        return toJsonArray(source, (JSONArray)null);
    }

    public static int[] toIntArray(JSONArray source, int[] defaultValue) throws JSONException {
        int[] result;
        if(source == null) {
            result = defaultValue;
        } else {
            int typeCount = source.length();
            result = new int[typeCount];

            for(int i = 0; i < typeCount; ++i) {
                result[i] = source.getInt(i);
            }
        }

        return result;
    }

    public static int[] toIntArray(JSONArray source) throws JSONException {
        return toIntArray(source, (int[])null);
    }

    public static int toHashCode(Object source) {
        return source == null?0:source.hashCode();
    }

    public static int toHashCode(int[] source) {
        int result = 0;
        if(source != null) {
            for(int i = 0; i < source.length; ++i) {
                result += source[i];
            }
        }

        return result;
    }

    public static boolean toBoolean(Object source, boolean defaultValue) {
        boolean result;
        if(source instanceof Boolean) {
            result = ((Boolean)source).booleanValue();
        } else {
            String valueString = toString(source).trim().toLowerCase();
            result = valueString.equals("true") || valueString.equals("on") || valueString.equals("yes") || toInt(valueString) != 0;
        }

        return result;
    }

    public static boolean toBoolean(Object source) {
        return toBoolean(source, false);
    }

    public static String convertEpochTODate(String time) {
        Long justNumbers = getDateNumber(time);
        String result;
        if(justNumbers == null) {
            result = "--";
        } else {
            Date date = new Date(justNumbers.longValue());
            m_asOfDateFormatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
            result = m_asOfDateFormatter.format(date);
        }

        return result + " ET";
    }

    public static String convertEpochTODateOnly(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Long justNumbers = getDateNumber(time);
        String result;
        if(justNumbers == null) {
            result = "--";
        } else {
            Date date = new Date(justNumbers.longValue());
            m_asOfDateFormatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
            result = formatter.format(date);
        }

        return result;
    }

    public static String convertDateFormat(String date) {
        Date newDate = null;
        m_asOfDateFormatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        try {
            newDate = formatter.parse(date);
            return newDate.toString();
        } catch (Exception var4) {
            return "--";
        }
    }

    public static String convertTimeFormat(String time) {
        Date newDate = null;
        m_asOfDateFormatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        SimpleDateFormat formatter = new SimpleDateFormat("HH.mm.ss");

        try {
            newDate = formatter.parse(time);
            return newDate.toString();
        } catch (Exception var4) {
            return "--";
        }
    }

    public static String convertDateTimeFormat(String date, String time) {
        m_asOfDateFormatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String newDate = "++";
        String newTime = "++";
        Date newDDate = null;

        SimpleDateFormat result;
        try {
            if(!date.equals((Object)null)) {
                result = new SimpleDateFormat("MM/dd/yyyy");
                newDDate = result.parse(date);
            }
        } catch (Exception var12) {
            newDate = "--";
        }

        try {
            if(!time.equals((Object)null)) {
                result = new SimpleDateFormat("HH.mm.ss");
                result.parse(time);
            }
        } catch (Exception var11) {
            newTime = "--";
        }

        result = null;
        String result1;
        if(newTime.equals("--") && newDate.equals("--")) {
            result1 = "--";
        } else if(newTime.equals("--") && !newDate.equals("--")) {
            result1 = m_asOfDateFormatter.format(newDDate);
        } else {
            Date newDateTime = null;
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH.mm.ss");

            try {
                newDateTime = formatter.parse(date + " " + time);
                result1 = m_asOfDateFormatter.format(newDateTime).toString();
            } catch (ParseException var10) {
                result1 = "--";
            }
        }

        return result1;
    }

    public static Long getDateNumber(String str) {
        if(str == null) {
            return null;
        } else {
            String dateNum = str.substring(6, str.length() - 2);

            try {
                return new Long(Long.parseLong(dateNum));
            } catch (Exception var3) {
                return null;
            }
        }
    }

    public static byte[] convertImageToByteArray(Bitmap image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bytes = null;
        if(image.compress(Bitmap.CompressFormat.PNG, 100, out)) {
            bytes = out.toByteArray();
        }

        return bytes;
    }

    public static Bitmap convertByteArrayToImage(byte[] bytes, BitmapFactory.Options options) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public static List<CharSequence> convertStringListToCharList(List<String> strings) {
        ArrayList ret = new ArrayList();
        Iterator var2 = strings.iterator();

        while(var2.hasNext()) {
            String n = (String)var2.next();
            ret.add(n.toString());
        }

        return ret;
    }
}
