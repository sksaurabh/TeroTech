package com.telloquent.vms.utils;

import android.annotation.SuppressLint;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Telloquent-DM6M on 9/5/2017.
 */

@SuppressLint({"SimpleDateFormat", "UseValueOf"})
public final class StringUtil {
    private static String TAG = "StringUtil";
    private static List value;
    private static final NumberFormat CURRENCY_FORMATTER;
    private static final DecimalFormat NUMBER_FORMATTER;

    private StringUtil() {
        throw new UnsupportedOperationException("Cannot construct an utility class");
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static boolean isListNullOrEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    /** @deprecated */
    public static boolean isEqual(String lhs, String rhs) {
        return Compare.isEqual(lhs, rhs);
    }

    public static boolean isEqualIgnoreCase(String lhs, String rhs) {
        return lhs == rhs || lhs != null && rhs != null && lhs.equalsIgnoreCase(rhs);
    }

    public static String formatUSD(double amount) {
        return formatUSD((new Double(amount)).doubleValue());
    }

    public static String formatUSD(String amount) {
        return formatUSD(Convert.toDouble(amount));
    }

    public static String formatPct(Number amount) {
        return amount == null?"--":amount.doubleValue() + "%";
    }

    public static String formatPCTDelta(Number amount) {
        return amount == null?"--":(amount.doubleValue() < 0.0D?Convert.toString(formatPct(amount)):(amount.doubleValue() > 0.0D?"+" + Convert.toString(formatPct(amount)):formatPct(amount)));
    }

    public static String formatPercent(Number value) {
        return value == null?"--":value.doubleValue() + "%";
    }

    public static CharSequence convertToString(Number object) {
        return object == null?"--":object.toString();
    }

    public static String formatIncludeCommas(Number object) {
        return object == null?"":NUMBER_FORMATTER.format(object);
    }

    public static String affixPercent(Number amount) {
        if(amount == null) {
            return "--";
        } else if(amount.doubleValue() < 0.0D) {
            String number = Double.toString(amount.doubleValue());
            return number.substring(1) + "%";
        } else {
            return amount + "%";
        }
    }

    public static String removeSpaces(String s) {
        StringTokenizer st = new StringTokenizer(s.trim(), " ", false);

        String t;
        for(t = ""; st.hasMoreElements(); t = t + st.nextElement()) {
            ;
        }

        return t;
    }

    public static String cleanUpHtmlDataForWebView(String htmlData) {
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile("<a [^>]*href *= *\"([^\"]*)\"[^>]*>");
        Matcher m = p.matcher(htmlData);
        boolean result = m.find();

        boolean deletedIllegalChars;
        for(deletedIllegalChars = false; result; result = m.find()) {
            deletedIllegalChars = true;
            m.appendReplacement(sb, "");
        }

        m.appendTail(sb);
        String cleanData = sb.toString();
        if(deletedIllegalChars) {
            cleanData = cleanData.replaceAll("</a>", "");
        }

        cleanData = cleanData.replaceAll("%", "&#37;");
        return cleanData;
    }

    public static String cleanRCDContentForWebView(String htmlData) {
        String cleanData = htmlData.replaceAll("<Content> *< *!\\[CDATA\\[ *", "");
        cleanData = cleanData.replaceAll(" *\\]\\] *> *</Content>", "");
        cleanData = cleanData.trim();
        cleanData = cleanData.replaceAll("%", "&#37;");
        return cleanData;
    }

    public static String replaceAllHtmlTags(String htmlData) {
        return !isEmpty(htmlData)?htmlData.replaceAll("<[a-z|/| ]*/?>", ""):htmlData;
    }

    public static String join(List<String> strList, String delim) {
        if(strList != null && strList.size() != 0) {
            StringBuilder builder = new StringBuilder();

            for(int i = 0; i < strList.size() - 1; ++i) {
                builder.append((String)strList.get(i));
                builder.append(delim);
            }

            builder.append((String)strList.get(strList.size() - 1));
            return builder.toString();
        } else {
            return "";
        }
    }

    public static Integer parseVersion(String versionString) {
        String PERIOD_REGEX = "\\.";
        boolean SIG_DIGITS = true;
        String[] versionSections = versionString.split("\\.");
        if(versionSections.length == 3) {
            String iTmp = "([0-9]*)(.*)";
            Pattern i = Pattern.compile("([0-9]*)(.*)");
            Matcher len = i.matcher(versionSections[2]);
            if(len.find() && len.groupCount() >= 2) {
                versionSections[2] = len.group(1);
            }
        }

        int var9 = 0;
        int var10 = 0;

        for(int var11 = versionSections.length; var10 < var11; ++var10) {
            try {
                Integer tmpI = Integer.valueOf(Integer.parseInt(versionSections[var10]));
                var9 *= 100;
                var9 += tmpI.intValue();
            } catch (NumberFormatException var8) {
                ;
            }
        }

        return Integer.valueOf(var9);
    }

    public static String formatDataTimeStamp(long timeInMillis) {
        String timeFormat = "yy-MM-dd HH:mm:ss.SSS";
        return formatDateTimeStamp(timeInMillis, "yy-MM-dd HH:mm:ss.SSS");
    }

    public static String formatDateTimeStamp(long timeInMillis, String dateFormatString) {
        String tmpString = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMillis);
        Date date = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(dateFormatString);
        tmpString = format.format(date);
        return tmpString;
    }

    public static String formatCurrentDateTimeStamp(String dateFormatString) {
        String tmpString = "";
        SimpleDateFormat format = new SimpleDateFormat(dateFormatString);
        tmpString = format.format(new Date());
        return tmpString;
    }

    public static long getCalendarDateFromToday(int numDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(5, numDays);
        return cal.getTimeInMillis();
    }

    public static String normalizeDateTimestamp(String datetime) {
        if(datetime != null && datetime.length() > 0 && (datetime.endsWith("AM") || datetime.endsWith("PM"))) {
            datetime = datetime + " ET";
        }

        return datetime;
    }

    public static String assignOrAppend(Object targetObject, String sourceString) {
        if(null != targetObject && !isEmpty(sourceString)) {
            if(targetObject instanceof Integer) {
                int value = Convert.toInt(targetObject);
                if(value == 0) {
                    return sourceString;
                }
            }

            if(targetObject instanceof Double) {
                double value1 = Convert.toDouble(targetObject);
                if(value1 == 0.0D) {
                    return sourceString;
                }
            }

            if(targetObject instanceof Boolean) {
                boolean value2 = Convert.toBoolean(targetObject);
                if(!value2) {
                    return sourceString;
                }
            }

            return targetObject.toString().trim() + sourceString.trim();
        } else {
            return sourceString;
        }
    }

    public static String truncateToLength(String inputString, int truncateLength) {
        try {
            return truncateLength > inputString.length() - 1?inputString:inputString.substring(0, truncateLength);
        } catch (Exception var3) {
            return inputString == null?" ":inputString;
        }
    }

    public static String stripHTMLTags(String source) {
        String output = null;
        if(source != null) {
            try {
                boolean inTag = false;
                StringBuffer buf = new StringBuffer();
                char[] chars = new char[source.length()];
                source.getChars(0, source.length(), chars, 0);

                for(int i = 0; i < chars.length; ++i) {
                    if(chars[i] == 60) {
                        inTag = true;
                    } else if(chars[i] == 62) {
                        inTag = false;
                    } else if(!inTag) {
                        buf.append(chars[i]);
                    }
                }

                output = buf.toString();
            } catch (Exception var6) {
                ;
            }
        }

        return output;
    }

    public static final boolean isValidEmail(CharSequence target) {
        return target == null?false: Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber);
    }

    public static String displayNull(String value) {
        if(isEmpty(value)) {
            value = "null";
        }

        return value;
    }

    public static String passwordSuffix() {
        return "12345";
    }

    public static String displayIndianCurrency(Double value) {
        return value == null?"--":numberFormatter().format(value);
    }

    public static String displayIndianCurrency(Long value) {
        return value == null?"--":numberFormatter().format(value);
    }

    public static String displayIndianCurrency(Integer value) {
        return value == null?"--":numberFormatter().format(value);
    }

    public static NumberFormat numberFormatter() {
        return NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    }

    static {
        CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.US);
        NUMBER_FORMATTER = new DecimalFormat("###,###,###");
    }

}
