package slasha.lanmu.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.business.main.MainActivity;
import slasha.lanmu.entity.local.BookPost;
import slasha.lanmu.business.post_detail.PostDetailActivity;
import slasha.lanmu.persistence.Global;

public class AppUtils {

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
    public static String readStringFromAsset(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    LanmuApplication.instance().getAssets().open(fileName)
            );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufReader.readLine()) != null)
                builder.append(line);
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Handler sMainHandler = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        sMainHandler.post(runnable);
    }

    public static void postOnUiThread(Runnable runnable, long delayMillis) {
        sMainHandler.postDelayed(runnable, delayMillis);
    }

    public static void jumpToPostDetail(@NonNull Context context, BookPost bookPost) {
        context.startActivity(
                PostDetailActivity.newIntent(context, bookPost)
        );
    }

    public static void jumpToMainPage(@NonNull Context context) {
        context.startActivity(
                MainActivity.newIntent(context)
        );
    }
}
