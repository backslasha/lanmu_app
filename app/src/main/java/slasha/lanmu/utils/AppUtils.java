package slasha.lanmu.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import androidx.annotation.Nullable;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.entity.local.BookPost;
import slasha.lanmu.business.post_detail.PostDetailActivity;

public class AppUtils {
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

    public static void jumpToPostDetail(@Nullable Context context, BookPost bookPost) {
        if (context == null) {
            return;
        }
        context.startActivity(
                PostDetailActivity.newIntent(context, bookPost)
        );
    }
}
