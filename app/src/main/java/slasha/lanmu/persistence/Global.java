package slasha.lanmu.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Global {

    public static final String API_URL = "http://192.168.42.63:8080/api/";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private static Gson gson;

    static {
        gson = new GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    public static Gson gson() {
        return gson;
    }

    public static class Debug {
        public static boolean sUseFakeData = false;
        public static boolean sUserFakeLoginResult = true;
        public static boolean sUserFakeBookPostFlow = true;
        public static boolean sUserFakeComments = true;
        public static long sLoadingTime = 2000;
    }
}
