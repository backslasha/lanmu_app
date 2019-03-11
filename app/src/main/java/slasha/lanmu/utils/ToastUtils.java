package slasha.lanmu.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastUtils {

    private static Context sAppContext;

    public static void init(Context context) {
        if (context != null) {
            sAppContext = context.getApplicationContext();
        }
    }

    public static void showToast(@StringRes int stringRes) {
        Toast.makeText(sAppContext, stringRes, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String context) {
        Toast.makeText(sAppContext, context, Toast.LENGTH_SHORT).show();
    }
}
