package com.rapidftr.utilities;

public class StringUtility {

    public static boolean isBlank(String s) {
        boolean blank = false;

        if (s == null) {
            blank = true;
        } else if (s.length() == 0) {
            blank = true;
        } else {
            for (int i = 0; i < s.length(); i++) {
                char charAt = s.charAt(i);

                if (!isWhiteSpace(charAt)) {
                    break;
                }
            }
        }

        return blank;
    }

    public static boolean isWhiteSpace(char c) {
        return c == ' ' || c == '\t'  || c == '\n' || c == '\r' || c == '\f';
    }

}
