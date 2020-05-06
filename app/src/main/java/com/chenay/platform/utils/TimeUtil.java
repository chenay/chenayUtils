package com.chenay.platform.utils;

import android.text.TextUtils;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
    private static final int seconds_of_1minute = 60;
    private static final int seconds_of_1hour = 60 * 60;
    private static final int seconds_of_2hour = 2 * 60 * 60;
    private static final int seconds_of_3hour = 3 * 60 * 60;

    private static final String YMDHMS_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String search_DateFormat = "MM/dd/yyyy HH:mm:ss";
    private static final String TIME_ZERO = "00:00";
    private static final String TIME_MAX = "23:59:59";

    public static Date stringConvertDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date data = null;
        try {
            data = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getFormat(Long date, TimeZone timeZone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        if (timeZone != null) {
            dateFormat.setTimeZone(timeZone);
        }
        return dateFormat.format(date);
    }

    public static String getFormat1(Long date, TimeZone timeZone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.CHINESE);

        if (timeZone != null) {
            dateFormat.setTimeZone(timeZone);
        }
        return dateFormat.format(date);
    }

    public static String getFormat(long date) {
        return getFormat(date, null);
    }
//    public static String timeAgo(Context context, long createdTime) {
//        return timeAgo(context, new Date(createdTime));
//    }

//    public static String timeAgo(Context context, Date createdTime) {
//        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
//        if (createdTime != null) {
//            long agoTimeInMin = (new Date(System.currentTimeMillis()).getTime() - createdTime.getTime()) / 1000 / 60;
//            if (agoTimeInMin <= 1) {
//                return context.getString(R.string.just_now);
//            } else if (agoTimeInMin <= 60) {
//                return agoTimeInMin + context.getString(R.string.mins_ago);
//            } else if (agoTimeInMin <= 60 * 24) {
//                return agoTimeInMin / 60 + context.getString(R.string.hours_ago);
//            } else if (agoTimeInMin <= 60 * 24 * 2) {
//                return agoTimeInMin / (60 * 24) + context.getString(R.string.days_ago);
//            } else {
//                return format.format(createdTime);
//            }
//        } else {
//            return format.format(new Date(0));
//        }
//    }

//    public static String getDateTimeAgo(Context context, long timeStamp) {
//        return timeAgo(context, new Date(timeStamp));
//    }

    public static String getUSDateTimeFormat(long timeStamp) {
        SimpleDateFormat usSdf = new SimpleDateFormat("HH:mm, MMMM dd, yyyy", Locale.US);
        return usSdf.format(new Date(timeStamp));
    }

    public static String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * local ---> UTC
     *
     * @return
     */
    public static String Local2UTC() {
        SimpleDateFormat sdf = new SimpleDateFormat(YMDHMS_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String gmtTime = sdf.format(new Date());
        return gmtTime;
    }

    /**
     * UTC --->local
     *
     * @param utcTime UTC
     * @return
     */
    public static String utc2Local(String utcTime) {
        try {
            if (TextUtils.isEmpty(utcTime)) {
                return "";
            }
            SimpleDateFormat utcFormater = new SimpleDateFormat(YMDHMS_FORMAT);
            utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date gpsUTCDate = null;
            try {
                gpsUTCDate = utcFormater.parse(utcTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat localFormater = new SimpleDateFormat(YMDHMS_FORMAT);
            localFormater.setTimeZone(TimeZone.getDefault());
            String localTime = localFormater.format(gpsUTCDate.getTime());
            return localTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

//    /**
//     * @param tTime
//     * @return
//     */
//    public static String getTimeRange(Context context, String tTime) {
//        String mTime = "";
//        try {
//            mTime = utc2Local(tTime);
//            if (TextUtils.isEmpty(mTime)) {
//                return "";
//            }
//            SimpleDateFormat sdf = new SimpleDateFormat(YMDHMS_FORMAT);
//            sdf.setTimeZone(TimeZone.getDefault());
//            Date curDate = new Date(System.currentTimeMillis());
//            String dataStrNew = sdf.format(curDate);
//            Date startTime = null;
//            try {
//                curDate = sdf.parse(dataStrNew);
//                startTime = sdf.parse(mTime);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            long between = (curDate.getTime() - startTime.getTime()) / 1000;
//            int elapsedTime = (int) (between);
//            if (elapsedTime < 0) {
//                return context.getResources().getString(R.string.timeutils_default_oneminageo);
//            }
//            if (elapsedTime < seconds_of_1minute) {
//
//                return context.getResources().getString(R.string.timeutils_default_oneminageo);
//            }
//            if (elapsedTime < seconds_of_1hour) {
//
//                return elapsedTime / seconds_of_1minute + " " + context.getResources().getString(R.string.timeutils_default_moreminsageo);
//            }
//            if (elapsedTime < seconds_of_2hour) {
//
//                return context.getResources().getString(R.string.timeutils_default_onehourageo);
//            }
//            if (elapsedTime < seconds_of_3hour) {
//
//                return elapsedTime / seconds_of_1hour + " " + context.getResources().getString(R.string.timeutils_default_morehoursageo);
//            }
//
//            return "";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return context.getResources().getString(R.string.timeutils_default_threehoursageo);
//    }
//
//    public static String getTimeRange(Context context, long time) {
//        long between = (System.currentTimeMillis() - time) / 1000;
//        int elapsedTime = (int) (between);
//        if (elapsedTime < 0) {
//            return context.getResources().getString(R.string.timeutils_default_oneminageo);
//        }
//        if (elapsedTime < seconds_of_1minute) {
//
//            return context.getResources().getString(R.string.timeutils_default_oneminageo);
//        }
//        if (elapsedTime < seconds_of_1hour) {
//
//            return elapsedTime / seconds_of_1minute + " " + context.getResources().getString(R.string.timeutils_default_moreminsageo);
//        }
//        if (elapsedTime < seconds_of_2hour) {
//
//            return context.getResources().getString(R.string.timeutils_default_onehourageo);
//        }
//        if (elapsedTime < seconds_of_3hour) {
//
//            return elapsedTime / seconds_of_1hour + " " + context.getResources().getString(R.string.timeutils_default_morehoursageo);
//        }
//        return "";
//    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @return
     */
    public static String timeStamp2Date(long seconds, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }

    public static String longToString(long longNum, String dateFormat) {
        if (TextUtils.isEmpty(dateFormat)) {
            dateFormat = YMDHMS_FORMAT;
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Date date = new Date(longNum);
        return format.format(date);
    }

    public static String secondsToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return TIME_ZERO;
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 23) {
                    return TIME_MAX;
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        try {
            if (i >= 0 && i < 10) {
                retStr = "0" + Integer.toString(i);
            } else {
                retStr = "" + i;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retStr;
    }

    public static long searchTimeToLong(String time) {
        if (TextUtils.isEmpty(time)) {
            return 0L;
        }
        try {
            String[] split = time.split(" ");
            String tempTime = split[0] + " " + split[1];
            int diff = 0;
            if ("pm".equals(split[2])) {
                diff = 1000 * 12 * 60 * 60;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(search_DateFormat);
            sdf.setTimeZone(TimeZone.getDefault());
            Date startTime = null;
            startTime = sdf.parse(tempTime);
            return (startTime.getTime() + diff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static String searchTimeFormat(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        try {
            String date = (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", searchTimeToLong(time));
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurDate() {
        String date = (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
        return date;
    }


    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getCurTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("dd-MM-yy hh.mm.ss ");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getCurTime1() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new Date());
        return date;
    }

    public static String getCurTime2() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 比较两次按下的时间间隔
     * 比较当前系统时间和time 的差
     *
     * @param time          传入时间
     * @param interval_time 间隔
     * @return 如果时间间隔大于interval_time 返回 当前时间 否则返回传入时间
     */
    public static long compareTime(long time, long interval_time) {
        long sysTime = System.currentTimeMillis();
        if (sysTime - time > interval_time) {
            return sysTime;
        }
        return time;
    }

    /**
     * 由数字格式转为分钟与秒混全的时间格式
     *
     * @param time
     * @return
     */
    public static String secondToMinute(int time) {
        StringBuffer timeBuffer = null;
        if (time <= 0) {
            return "00:00";
        }

        if (timeBuffer == null) {
            timeBuffer = new StringBuffer();
        } else {
            timeBuffer.delete(0, timeBuffer.length());
        }

        int minute = time / 60;
        int second = time % 60;

        timeBuffer.append(minute / 10);
        timeBuffer.append(minute % 10);
        timeBuffer.append(":");
        timeBuffer.append(second / 10);
        timeBuffer.append(second % 10);
        //String str=""+minute/10+minute%10+":"+second/10+second%10;
        return timeBuffer.toString();
    }


    /**
     * 将String 转换成时间戳
     *
     * @param time
     * @return
     */
    public static long getTimestamp(String time) {
        //Date或者String转化为时间戳
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time1 = date.getTime();
        System.out.print("Format To times:" + time1);
        return time1;
    }

    public static String getCurrentYear() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String sYear = sDateFormat.format(new Date());
        return sYear;
    }

    public static String getCurrentMonth() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
        String sMonth = sDateFormat.format(new Date());
        return sMonth;
    }



}
