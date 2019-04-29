package slasha.lanmu.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Global {

    public static final String API_URL = "http://192.168.42.158:8080/api/";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private static Gson gson;

    static {
        gson = new GsonBuilder()
//                .setDateFormat(DATE_FORMAT)
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Date.class, new DateDeserializer())
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

    static class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement element,
                                Type arg1,
                                JsonDeserializationContext arg2) throws JsonParseException {
            String date = element.getAsString();
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                return format.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
