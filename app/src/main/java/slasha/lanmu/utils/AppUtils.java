package slasha.lanmu.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import androidx.annotation.NonNull;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.business.chat.ChatActivity;
import slasha.lanmu.business.conversation.ConversationActivity;
import slasha.lanmu.business.friend.apply.ApplyActivity;
import slasha.lanmu.business.friend.FriendActivity;
import slasha.lanmu.business.main.MainActivity;
import slasha.lanmu.business.notification.NotificationActivity;
import slasha.lanmu.business.post_detail.PostDetailActivity;
import slasha.lanmu.business.profile.UserProfileActivity;
import slasha.lanmu.business.profile.dynamic.DynamicActivity;
import slasha.lanmu.business.profile.edit.EditProfileActivity;
import slasha.lanmu.entity.card.UserCard;

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

    public static void jumpToPostDetail(@NonNull Context context, long postId) {
        context.startActivity(
                PostDetailActivity.newIntent(context, postId)
        );
    }

    public static void jumpToMainPage(@NonNull Context context) {
        context.startActivity(
                MainActivity.newIntent(context)
        );
    }

    public static void jumpToUserProfile(@NonNull Context context, long userId) {
        context.startActivity(
                UserProfileActivity.newIntent(context, userId)
        );
    }

    public static void jumpToChatPage(Context context, UserCard userCard) {
        context.startActivity(
                ChatActivity.newIntent(context, userCard)
        );
    }

    public static void jumpToEditProfilePage(Context context, UserCard userCard) {
        context.startActivity(
                EditProfileActivity.newIntent(context, userCard)
        );
    }

    public static void jumpToConversationPage(Context context) {
        context.startActivity(
                ConversationActivity.newIntent(context)
        );
    }

    public static void jumpToNotificationPage(Context context) {
        context.startActivity(
                NotificationActivity.newIntent(context)
        );
    }

    public static void jumpToFriendPage(Context context) {
        context.startActivity(
                FriendActivity.newIntent(context)
        );
    }

    public static void jumpToApplyPage(Context context) {
        context.startActivity(
                ApplyActivity.newIntent(context)
        );
    }

    public static void jumpToDynamicPage(Context context, long userId) {
        context.startActivity(
                DynamicActivity.newIntent(context, userId)
        );
    }
}
