package com.rapidftr.utilities;

public class Arrays {

    public static boolean equals(byte[] first, byte[] second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null && second != null) {
            return false;
        }
        if (first != null && second == null) {
            return false;
        }
        if (first.length != second.length) {
            return false;
        }
        for (int i = 0; i < first.length; i++) {
            if (first[i] != (second[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(Object[] first, Object[] second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null && second != null) {
            return false;
        }
        if (first != null && second == null) {
            return false;
        }
        if (first.length != second.length) {
            return false;
        }
        for (int i = 0; i < first.length; i++) {
            if (first[i] != null) {
                if (!first[i].equals(second[i])) {
                    return false;
                }
            } else if (second[i] != null) {
                return false;
            }
        }
        return true;
    }

    public static int hashCode(Object[] array) {
        int result = 0;
        if (array != null) {
            for(int i=0; i< array.length; i++) {
                result = 31 * result  + (array[i] != null ? array[i].hashCode() : 0);
            }
        }
        return result;
    }

    public static int hashCode(byte[] array) {
        int result = 0;
        if (array != null) {
            for(int i=0; i< array.length; i++) {
                result = 31 * result + array[i];
            }
        }
        return result;
    }
}
