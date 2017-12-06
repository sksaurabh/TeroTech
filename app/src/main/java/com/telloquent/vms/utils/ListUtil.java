package com.telloquent.vms.utils;

import java.util.List;


public class ListUtil {

    public static Boolean isEmpty(List list){
        if (list == null) return true;
        if (list.size() ==  0) return true;
        return false;
    }
}
