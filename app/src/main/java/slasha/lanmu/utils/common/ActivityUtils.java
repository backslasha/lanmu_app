package slasha.lanmu.utils.common;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

/**
 * Created by yhb on 18-1-17.
 */

public class ActivityUtils {
    /**
     * The {@code mFragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics out = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(out);
        return out.heightPixels;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics out = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(out);
        return out.widthPixels;
    }

    public static void makeFullScreen(Window window) {
        if (window == null) return;
        window.getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }

}
