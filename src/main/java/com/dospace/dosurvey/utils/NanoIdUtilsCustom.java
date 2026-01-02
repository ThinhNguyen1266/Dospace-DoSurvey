package com.dospace.dosurvey.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public class NanoIdUtilsCustom {

    public static String genNanoIdLimit(int size) {
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
                "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(),
                size);
    }

    public static String genNanoIdLimit16() {
        return genNanoIdLimit(16);
    }

    public static String genNanoIdLimit12() {
        return genNanoIdLimit(12);
    }

    public static String genNanoIdLimit24() {
        return genNanoIdLimit(24);
    }

    public static String genNanoIdLimit36() {
        return genNanoIdLimit(36);
    }
}
