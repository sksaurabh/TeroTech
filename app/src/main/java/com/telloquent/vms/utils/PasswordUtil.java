package com.telloquent.vms.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PasswordUtil {

    /**
     *
     * (			# Start of group
     (?=.*\d)		#   must contains one digit from 0-9
     (?=.*[a-z])		#   must contains one lowercase characters
     (?=.*[A-Z])		#   must contains one uppercase characters
     (?=.*[@#$%])		#   must contains one special symbols in the list "@#$%"
     .		#     match anything with previous condition checking
     {6,20}	#        length at least 6 characters and maximum of 20
     )			# End of group
     *
     * Whole combination is means, 6 to 20 characters string with at least one digit, one upper case letter,
     * one lower case letter and one special symbol (“@#$%”). This regular expression pattern is very useful to implement a strong and complex password.
     *
     */

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

    public static Boolean doesSatisfy(String password) {
        if (StringUtil.isEmpty(password)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
            Matcher matcher = pattern.matcher(password);
            return matcher.matches();
        }
    }
}
