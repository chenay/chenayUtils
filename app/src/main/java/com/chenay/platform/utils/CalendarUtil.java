package com.chenay.platform.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Y.Chen5
 */
public class CalendarUtil {


    /**
     * 获取当天某个时间点的时间
     *
     * @return
     */
    public static long getDate(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final long timeInMillis = calendar.getTimeInMillis();
        System.out.println(sdf.format(timeInMillis));
        return timeInMillis;
    }


    //获取当天0点时间
    public static String getDate0() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);//控制时
        cal.set(Calendar.MINUTE, 0);//控制分
        cal.set(Calendar.SECOND, 0);//控制秒
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }    //获取当天0点时间

    public static long getLongDate0() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    //获取当天12点时间
    public static String getDate12() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    //获取本周一0点时间
    public static String getDateWeek1() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    //获取本月第一天0点时间
    public static String getDateMonth1() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    //获得本月最后一天24点时间
    public static String getDateMonthEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }


    /**
     * 计算2个时间直接差值
     * 多少分钟
     *
     * @param date
     * @param date1
     * @param type  C
     * @return
     */
    public static long getDateInterpolation(long date, long date1, int type) {
        long value = 0;
        long diff = date - date1;
        if (type == Calendar.DATE) {
            value = diff / (60 * 1000 * 60 * 12);
        } else if (type == Calendar.HOUR) {
            value = diff / (60 * 1000 * 60);
        } else if (type == Calendar.MINUTE) {
            value = diff / (60 * 1000);
        } else if (type == Calendar.SECOND) {
            value = diff / (1000);
        }


        return value;
    }

    /**
     * 00:00:00
     *
     * @param hhssmm
     * @return
     */
    public static long getDate(String hhssmm) {
        String[] strs = hhssmm.split(":");
        int hourOfDay = Integer.parseInt(strs[0]);
        int minute = Integer.parseInt(strs[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final long timeInMillis = calendar.getTimeInMillis();
        System.out.println(sdf.format(timeInMillis));
        return timeInMillis;
    }

    public static String getDateHHssmm(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(millis);
    }

    /**
     * @param ymd
     */
    public static long getLongdate(String ymd) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = sdf.parse(ymd);
        return date.getTime();
    }


    /**
     *
     * @param day 需要比较的时间
     * @param i
     * @return
     */
    public static boolean compareTime(String day, long i) {
        long todayTime = CalendarUtil.getLongDate0();

        try {
            long tempTime = CalendarUtil.getLongdate(day);
            if (todayTime - tempTime >= i) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
