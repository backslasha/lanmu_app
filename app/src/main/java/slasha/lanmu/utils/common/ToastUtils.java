package slasha.lanmu.utils.common;

import android.content.Context;
import androidx.annotation.StringRes;
import android.widget.Toast;

public class ToastUtils {

    private static Context sAppContext;

    public static void init(Context context) {
        if (context != null) {
            sAppContext = context.getApplicationContext();
        }
    }

    public static void showToast(@StringRes int stringRes) {
        Toast.makeText(sAppContext, stringRes, Toast.LENGTH_LONG).show();
    }

    public static void showToast(String content) {
        Toast.makeText(sAppContext, content, Toast.LENGTH_LONG).show();
    }
}
