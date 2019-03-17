package slasha.lanmu.utils;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import slasha.lanmu.application.LanmuApplication;

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
}
