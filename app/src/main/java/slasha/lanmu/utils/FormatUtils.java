package slasha.lanmu.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import slasha.lanmu.persistence.Global;

public class FormatUtils {
    private static final SimpleDateFormat dataFormatter
            = new SimpleDateFormat(Global.DATE_FORMAT, Locale.CHINA);
    private static final SimpleDateFormat simpleFormatter
            = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);

    public static String toServerFormat(String date, String pattern) {
        simpleFormatter.applyPattern(pattern);
        try {
            return dataFormatter.format(simpleFormatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String format(Date date, String pattern) {
        simpleFormatter.applyPattern(pattern);
        return simpleFormatter.format(date);
    }

    public static String asOneString(List<String> urls) {
        StringBuilder builder = new StringBuilder();
        if (urls != null) {
            for (int i = 0; i < urls.size(); i++) {
                String entity = urls.get(i);
                builder.append(entity);
                if (i != urls.size() - 1) {
                    builder.append(":");
                }
            }
        }
        return builder.toString();
    }

    public static List<String> asUrlList(String postImages) {
        if (!TextUtils.isEmpty(postImages)) {
            return Arrays.asList(postImages.split(";"));
        } else {
            return Collections.emptyList();
        }
    }

}
