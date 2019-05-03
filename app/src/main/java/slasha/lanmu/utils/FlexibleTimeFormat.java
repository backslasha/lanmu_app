package slasha.lanmu.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 用来转化成界面显示的字符串格式
 */

public class FlexibleTimeFormat {

    private static final long MILLIS_PER_MINUTE = 60L * 1000;
    private static final long MILLIS_PER_HOUR = MILLIS_PER_MINUTE * 60;
    private static final long MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;

    private static final SimpleDateFormat HHmm
            = new SimpleDateFormat("HH:mm", Locale.CHINA);

    private static final SimpleDateFormat MMdd
            = new SimpleDateFormat("MM月dd日", Locale.CHINA);

    private static final SimpleDateFormat yyyyMMdd =
            new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);


    /**
     * 将time代表的时间转化为一个合适的文字描述具体的描述逻辑如下:
     * 时间间隔 = T）
     * T < 1分钟：刚刚
     * 1分钟 <= T <  60分钟：x分钟前
     * 1小时 <= T <  24小时，并且日期为今天：x小时前
     * 1小时 <= T < 12小时，并且日期为昨天：x小时前
     * T >= 12小时，并且日期为昨天：昨天
     * 日期为前天：前天
     * 3天 <= T < 8天：x天前
     * T >= 8天，并且日期为今年：M月d日 （用系统格式）
     * T >= 8天，并且日期不是今年：yyyy年M月d日 （用系统格式）
     *
     * @param time 1970年1月1日零时零分零秒至time的秒数，注意不是毫秒数
     * @return 返回一个描述该事件的字符串用于评论和影视圈内容发表
     */
    public static String changeTimeToDesc(long time) {
        String timeDesc;

        long now = System.currentTimeMillis();

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeInMillis(now);
        Calendar timeCalendar = Calendar.getInstance();
        Date timeDate = new Date(time);
        timeCalendar.setTimeInMillis(time);

        int differenceInDay = nowCalendar.get(Calendar.DAY_OF_YEAR) - timeCalendar.get(Calendar.DAY_OF_YEAR);
        int differenceInYear = nowCalendar.get(Calendar.YEAR) - timeCalendar.get(Calendar.YEAR);

        long differenceInSecond = (now - time) / 1000;

        if (differenceInYear <= 0) {
            if (differenceInSecond < 60) {
                timeDesc = "刚刚";
                return timeDesc;
            }

            if (differenceInSecond < 3600) {
                timeDesc = differenceInSecond / 60 + "分钟前";
                return timeDesc;
            }

            //86400为24小时所使用的秒数
            if (differenceInSecond < 86400) {
                //T >= 12小时，并且日期为昨天：昨天
                if (differenceInSecond >= 43200 && differenceInDay > 0) {
                    timeDesc = "昨天";
                    return timeDesc;
                }
                timeDesc = (differenceInSecond) / 3600 + "小时前";
                return timeDesc;
            }

            //这种情况应该不会出现，但是为了保险起见
            if (differenceInDay == 1) {
                timeDesc = "昨天";
                return timeDesc;
            }

            if (differenceInDay == 2) {
                timeDesc = "前天";
                return timeDesc;
            }

            if (differenceInDay >= 3 && differenceInDay < 8) {
                timeDesc = differenceInDay + "天前";
                return timeDesc;
            }
            timeDesc = MMdd.format(timeDate);
            return timeDesc;
        }
        timeDesc = yyyyMMdd.format(timeDate);
        return timeDesc;

    }


    /**
     * 倒计时显示，单位秒
     */
    public static String formatDateTime(long times) {
        if (times <= 0) {
            return "00:00:00";
        }
        String time = "";
        long hour = times / (60 * 60);
        if (hour < 10) {
            time += "0" + String.valueOf(hour);
        } else {
            time += "" + String.valueOf(hour);
        }
        long min = (times - hour * 60 * 60) / 60;
        if (min < 10) {
            time += ":0" + String.valueOf(min);
        } else {
            time += ":" + String.valueOf(min);
        }
        long second = times - hour * 60 * 60 - min * 60;
        if (second < 10) {
            time += ":0" + String.valueOf(second);
        } else {
            time += ":" + String.valueOf(second);
        }
        return time;
    }
}
