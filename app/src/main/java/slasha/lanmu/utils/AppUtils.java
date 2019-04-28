package slasha.lanmu.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.business.chat.ChatActivity;
import slasha.lanmu.business.main.MainActivity;
import slasha.lanmu.business.profile.UserProfileActivity;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.card.UserCard;
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
            return Arrays.asList(postImages.split(":"));
        } else {
            return Collections.emptyList();
        }
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

    public static void jumpToPostDetail(@NonNull Context context, BookPostCard bookPost) {
        context.startActivity(
                PostDetailActivity.newIntent(context, bookPost)
        );
    }

    public static void jumpToMainPage(@NonNull Context context) {
        context.startActivity(
                MainActivity.newIntent(context)
        );
    }

    public static void jumpToUserProfile(@NonNull Context context, UserCard userCard) {
        context.startActivity(
                UserProfileActivity.newIntent(context, userCard)
        );
    }

    public static void jumpToChatPage(Context context, UserCard userCard) {
        context.startActivity(
                ChatActivity.newIntent(context, userCard)
        );
    }

    public static void jumpToEditProfilePage(Context context, UserCard userCard) {
        // todo
    }
}
