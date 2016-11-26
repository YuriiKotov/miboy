package com.boco.miboy.other;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TextUtil {
    private static final String TAG = TextUtil.class.getSimpleName();

    public static String getTime(long value) {
        DateFormat temp = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        return temp.format(value);
    }
}