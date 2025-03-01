package slasha.lanmu.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import slasha.lanmu.entity.card.BookCard;
import slasha.lanmu.persistence.Global;

public class FormatUtils {
    private static final SimpleDateFormat dbDateFormatter
            = new SimpleDateFormat(Global.DATE_FORMAT, Locale.CHINA);
    private static final SimpleDateFormat simpleFormatter
            = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);

    public static String dbFormat(String date, String pattern) {
        simpleFormatter.applyPattern(pattern);
        try {
            return dbDateFormatter.format(simpleFormatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dbFormat(Date date) {
        return dbDateFormatter.format(date);
    }

    public static Date dbFormat(String date) {
        try {
            return dbDateFormatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String format(Date date, String pattern) {
        simpleFormatter.applyPattern(pattern);
        return simpleFormatter.format(date);
    }

    public static String format1(Date date) {
        simpleFormatter.applyPattern("MM月dd日 hh时mm分");
        return simpleFormatter.format(date);
    }

    public static String format2(Date date) {
        simpleFormatter.applyPattern("yyyy年MM月dd日");
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

    public static String bookInfo(BookCard card) {
        return String.format(
                "《%s》/%s/%s",
                card.getName(),
                card.getAuthor(),
                FormatUtils.format(card.getPublishDate(), "yyyy-MM")
        );
    }
}
