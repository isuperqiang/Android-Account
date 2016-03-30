package com.silence.account.utils;

/**
 * Created by Silence on 2016/3/7 0007.
 */
public class StringUtils {
    private StringUtils() {
    }

    public static boolean checkEmail(String email) {
        String regex = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (email.matches(regex)) {
            return true;
        }
        return false;
    }
}
