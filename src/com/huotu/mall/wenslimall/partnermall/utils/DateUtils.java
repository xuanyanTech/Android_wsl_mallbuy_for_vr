package com.huotu.mall.wenslimall.partnermall.utils;

import android.util.Log;

import com.huotu.mall.wenslimall.partnermall.config.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils {
    protected static String  TAG = DateUtils.class.getName();
    /**
     * @throws
     * @方法描述：格式化获取的时间
     * @方法名：formatDate yyyy-MM-dd HH:mm:ss
     * @参数：@param currentTime
     * @参数：@return
     * @返回：String
     */
    public static String formatDate(Long currentTime) {
        DateFormat format = null;
        try {
            format = new SimpleDateFormat(Constants.TIME_FORMAT);
            Date date = new Date(currentTime);
            return format.format(date);
        } catch (Exception e) {
            //发现异常时，返回当前时间
            Log.e(TAG , e.getMessage());
            return format.format(new Date());
        }
    }

    /**
     * 秒数 转为 天小时分秒格式
     *
     * @throws
     * @创建人：jinxiangdong
     * @修改时间：2015年6月18日 下午3:05:24
     * @方法描述：
     * @方法名：toTime
     * @参数：@param totalSecond
     * @参数：@return
     * @返回：String
     */
    public static String toTime(int totalSecond) {
        String timeStr = "";
        int days = totalSecond / 60 / 60 / 24;
        int remain = totalSecond % (60 * 60 * 24);
        int hours = remain / (60 * 60);
        remain = remain % (60 * 60);
        int minute = remain / (60);
        int second = remain % 60;
        if (days > 0) {
            timeStr = days + "天";
        }
        if (hours > 0) {
            timeStr += hours + "小时";
        }
        if (minute > 0) {
            timeStr += minute + "分";
        }
        if (second > 0) {
            timeStr += second + "秒";
        }
        return timeStr;
    }

    public static String formatDate(Long currentTime, String fromat) {
        DateFormat format = null;
        try {
            format = new SimpleDateFormat(fromat);
            Date date = new Date(currentTime);
            return format.format(date);
        } catch (Exception e) {
            //发现异常时，返回当前时间
            Log.e(TAG , e.getMessage());
            return format.format(new Date());
        }
    }

    /**
     * 判断日期是否是今天
     *
     * @throws
     * @创建人：jinxiangdong
     * @修改时间：2015年6月13日 下午4:22:37
     * @方法描述：
     * @方法名：isToday
     * @参数：@param currentTime
     * @参数：@return
     * @返回：Boolean
     */
    public static Boolean isToday(Long currentTime) {
        boolean b = false;
        String currentDateStr = formatDate(currentTime, Constants.DATE_FORMAT);
        Long today = System.currentTimeMillis();

        String nowDateStr = formatDate(today, Constants.DATE_FORMAT);

        if (nowDateStr.equals(currentDateStr)) {
            b = true;
        }
        return b;
    }

    /**
     * @throws
     * @方法描述：
     * @方法名：toDate
     * @参数：@param str
     * @参数：@return
     * @返回：long
     */
    public static long toDate(String str, String formatStr) {
        DateFormat format = null;

        try {
            format = new SimpleDateFormat(formatStr);
            return format.parse(str).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return 0L;
        }
    }

    public static String newDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }
}
