package com.example.bfhqualifier.util;

public final class RegNoUtils {
    private RegNoUtils() {}
    public static int lastTwoDigits(String regNo) {
        if (regNo == null) return 0;
        String digits = regNo.replaceAll("\\D", "");
        if (digits.length() >= 2) {
            String last2 = digits.substring(digits.length() - 2);
            return Integer.parseInt(last2);
        }
        if (digits.length() == 1) {
            return Integer.parseInt(digits);
        }
        return 0;
    }
}
