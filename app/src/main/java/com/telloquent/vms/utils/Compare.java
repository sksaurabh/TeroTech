package com.telloquent.vms.utils;

import java.math.BigDecimal;

/**
 * Created by Telloquent-DM6M on 9/5/2017.
 */

public final class Compare {
    public static final String TAG = Compare.class.getSimpleName();

    private Compare() {
        throw new UnsupportedOperationException("Cannot construct an utility class");
    }

    public static boolean isEqual(Object lhs, Object rhs) {
        boolean result;
        try {
            result = lhs == rhs || lhs != null && rhs != null && lhs.equals(rhs);
        } catch (ClassCastException var4) {
            result = false;
        }

        return result;
    }

    public static boolean isEqual(int[] lhs, int[] rhs) {
        boolean result = lhs == rhs;
        if(!result && lhs != null && rhs != null && lhs.length == rhs.length) {
            result = true;

            for(int i = 0; result && i < lhs.length; ++i) {
                result = lhs[i] == rhs[i];
            }
        }

        return result;
    }

    public static int compare(Number lhs, Number rhs) {
        return (new BigDecimal(lhs.toString())).compareTo(new BigDecimal(rhs.toString()));
    }
}

