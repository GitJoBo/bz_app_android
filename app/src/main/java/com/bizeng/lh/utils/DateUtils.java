package com.bizeng.lh.utils;

import android.text.TextUtils;
import android.text.format.Time;

import com.wzq.mvvmsmart.utils.KLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Desc: 时间计算工具类
 * @author: admin wsj
 * @Date: 2021/4/28 2:09 PM
 */
public class DateUtils {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";
    public static final String MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String MM_DD_HH_MM2 = "MM.dd HH:mm";
    private static volatile DateUtils instance = null;

    private DateUtils() {

    }

    public static DateUtils getInstance() {
        if (instance == null) {
            synchronized (DateUtils.class) {
                if (instance == null) {
                    instance = new DateUtils();
                }
            }
        }
        return instance;
    }

    private SimpleDateFormat sf = null;

    /**
     * 获取当前时间
     * year + "-" + month + "-" + monthDay + " " + hour + ":"+ minute;
     *
     * @return
     */
    public String getNowTime() {
        String timeString = null;
        Time time = new Time();
        time.setToNow();
        String year = thanTen(time.year);
        String month = thanTen(time.month + 1);
        String monthDay = thanTen(time.monthDay);
        String hour = thanTen(time.hour);
        String minute = thanTen(time.minute);

        timeString = year + "-" + month + "-" + monthDay + " " + hour + ":"
                + minute;
        // System.out.println("-------timeString----------" + timeString);
        return timeString;
    }

    /**
     * 获取当前时间
     * month + "-" + monthDay + " " + hour + ":" + minute;
     *
     * @return
     */
    public String getNowTimeMDHM() {
        String timeString = null;
        Time time = new Time();
        time.setToNow();
        String month = thanTen(time.month + 1);
        String monthDay = thanTen(time.monthDay);
        String hour = thanTen(time.hour);
        String minute = thanTen(time.minute);

        timeString = month + "-" + monthDay + " " + hour + ":" + minute;
        // System.out.println("-------timeString----------" + timeString);
        return timeString;
    }

    /**
     * 获取当前时间
     * 时间戳
     *
     * @return
     */
    public long getNowTimeLong() {
//        long currentTimeMillis = System.currentTimeMillis();
        long timeMillis = Calendar.getInstance().getTimeInMillis();
        return timeMillis;
    }

    public int calculate(int year, int month) {

        boolean yearleap = judge(year);
        int day;
        if (yearleap && month == 2) {
            day = 29;
        } else if (!yearleap && month == 2) {
            day = 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            day = 30;
        } else {
            day = 31;
        }
        return day;
    }

    public boolean judge(int year) {
        boolean yearleap = (year % 400 == 0) || (year % 4 == 0)
                && (year % 100 != 0);// 采用布尔数据计算判断是否能整除
        return yearleap;
    }

    /**
     * 十一下加零
     *
     * @param str
     * @return
     */
    public String thanTen(int str) {

        String string = null;

        if (str < 10) {
            string = "0" + str;
        } else {

            string = "" + str;

        }
        return string;
    }

    /**
     * 计算时间差
     *
     * @param starTime 开始时间
     * @param endTime  结束时间
     * @param type     返回类型 0天，1天时分秒，2时
     * @return 返回时间差
     */
    public String getTimeDifference(String starTime, String endTime, int type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM);

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();

            long day = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000) - day * 24);
            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                    - min * 60 * 1000 - s * 1000);
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +
            // "秒");
            long hour1 = diff / (60 * 60 * 1000);
            String hourString = hour1 + "";
            long min1 = ((diff / (60 * 1000)) - hour1 * 60);
//            timeString = hour1 + "小时" + min1 + "分";
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +
            // "秒");
            switch (type) {
                case 0:
                    return String.valueOf(day);
                case 1:
                    return day + "天，" + hour + "时，" + min + "分，" + s + "秒";
                case 2:
                    return String.valueOf(hour1);
            }
        } catch (ParseException e) {
            KLog.e(e.getMessage());
            return "0";
        }
        return "0";
    }

    /**
     * 计算时间差
     *
     * @param starTime 开始时间
     * @param endTime  结束时间
     * @param type     返回类型 0天，1天时分秒，2时
     * @return 返回时间差 0返回小于1
     */
    public String getTimeDifferenceNo0(String starTime, String endTime, int type) {
        String timeDifference = getTimeDifference(starTime, endTime, type);
        if (timeDifference.startsWith("0")) {
            return timeDifference.replace("0", "小于1");
        }
        return timeDifference;
    }

    /**
     * 计算相差的小时
     *
     * @param starTime
     * @param endTime
     * @return
     */
    public String getTimeDifferenceHour(String starTime, String endTime) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM);

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();
            String string = Long.toString(diff);

            float parseFloat = Float.parseFloat(string);

            float hour1 = parseFloat / (60 * 60 * 1000);

            timeString = Float.toString(hour1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return timeString;

    }

    /**
     * 获取时间中的某一个时间点
     *
     * @param str
     * @param type
     * @return
     */
    public String getJsonParseShiJian(String str, int type) {
        String shijanString = null;
        String nian = str.substring(0, str.indexOf("-"));
        String yue = str.substring(str.indexOf("-") + 1, str.lastIndexOf("-"));
        String tian = str.substring(str.lastIndexOf("-") + 1, str.indexOf(" "));
        String shi = str.substring(str.indexOf(" ") + 1, str.lastIndexOf(":"));
        String fen = str.substring(str.lastIndexOf(":") + 1, str.length());

        switch (type) {
            case 1:
                shijanString = nian;
                break;
            case 2:
                shijanString = yue;
                break;
            case 3:
                shijanString = tian;
                break;
            case 4:
                shijanString = shi;
                break;
            case 5:
                shijanString = fen;
                break;

        }
        return shijanString;
    }

    /**
     * Sring变int
     *
     * @param str
     * @return
     */
    public int strToInt(String str) {
        int value = 0;
        value = Integer.parseInt(str);
        return value;
    }

    /**
     * 与当前时间比较早晚
     *
     * @param time 需要比较的时间
     * @return 输入的时间比现在时间晚则返回true
     */
    public boolean compareNowTime(String time) {
        boolean isDayu = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM);

        try {
            Date parse = dateFormat.parse(time);
            Date parse1 = dateFormat.parse(getNowTime());

            long diff = parse1.getTime() - parse.getTime();
            if (diff <= 0) {
                isDayu = true;
            } else {
                isDayu = false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return isDayu;
    }

    /**
     * 把时间戳变yyyy-MM-dd HH:mm格式时间
     *
     * @param time
     * @return
     */
    public String getDateToString(long time) {
        Date d = new Date(time * 1000);
        sf = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
        return sf.format(d);
    }

    /**
     * 把时间戳变yyyy-MM-dd HH:mm格式时间
     *
     * @param time
     * @return
     */
    public String getDateToString(long time, String formatString) {
        Date d = new Date(time * 1000);
        sf = new SimpleDateFormat(formatString);
        return sf.format(d);
    }

    /**
     * 返回时间戳
     *
     * @param time
     * @param temp YYYY_MM_DD 这种，建议用静态常量
     * @return
     */
    public long str2Timestamp(String time, String temp) {
        SimpleDateFormat sdr = new SimpleDateFormat(temp,
                Locale.CHINA);
        Date date;
        long l = 0;
        try {
            date = sdr.parse(time);
            l = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }


    /**
     * 比较两个时间
     *
     * @param starTime  开始时间
     * @param endString 结束时间
     * @return 结束时间大于开始时间返回true，否则反之֮
     */
    public boolean compareTwoTime(String starTime, String endString) {
        boolean isDayu = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endString);

            long diff = parse1.getTime() - parse.getTime();
            if (diff >= 0) {
                isDayu = true;
            } else {
                isDayu = false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isDayu;

    }

    public boolean compareTwoTime2(String starTime, String endString) {
        boolean isDayu = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM);

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endString);

            long diff = parse1.getTime() - parse.getTime();
            if (diff >= 0) {
                isDayu = true;
            } else {
                isDayu = false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isDayu;

    }

    /**
     * 获取年
     *
     * @param time
     * @return
     */
    public String getTimeYear(String time) {

        String substring = time.substring(0, time.lastIndexOf(" "));
        return substring;

    }

    /**
     * 换算小时，0.5小时-->0小时30分
     *
     * @param hour
     * @return
     */
    private String convertTime(String hour) {

        String substring = hour.substring(0, hour.lastIndexOf("."));
        String substring2 = hour.substring(hour.lastIndexOf(".") + 1,
                hour.length());
        substring2 = "0." + substring2;
        float f2 = Float.parseFloat(substring2);
        f2 = f2 * 60;
        String string = Float.toString(f2);
        String min = string.substring(0, string.lastIndexOf("."));
        return substring + "小时" + min + "分";

    }

    /**
     * 获取前n天日期，如获取距离今日7天前那一天的具体日期则getOldDate（-7）即可，后7天日期则getOldDate（7）；
     *
     * @param distanceDay
     * @return
     */
    public String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        KLog.d("前7天==" + dft.format(endDate));
        return dft.format(endDate);
    }

    public String getMakeUp0(String str) {
        if (TextUtils.isEmpty(str)) {
            if (str.length() < 2) {
                return "0" + str;
            }
        }
        return str;
    }

    public String getMakeUp0(int str) {
        if (str < 10) {
            return "0" + str;
        }
        return str + "";
    }


//    @NonNull
//    public static Long getDaysBetween(java.util.Date startDate, java.util.Date endDate) {
//        Calendar fromCalendar = Calendar.getInstance();
//        fromCalendar.setTime(startDate);
//        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
//        fromCalendar.set(Calendar.MINUTE, 0);
//        fromCalendar.set(Calendar.SECOND, 0);
//        fromCalendar.set(Calendar.MILLISECOND, 0);
//
//        Calendar toCalendar = Calendar.getInstance();
//        toCalendar.setTime(endDate);
//        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
//        toCalendar.set(Calendar.MINUTE, 0);
//        toCalendar.set(Calendar.SECOND, 0);
//        toCalendar.set(Calendar.MILLISECOND, 0);
//        //将获取的日期转换为天数。
//        return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
//    }


}
