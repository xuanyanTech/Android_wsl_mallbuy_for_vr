package com.huotu.mall.wenslimall.partnermall.utils;

/**
 * Created by liumingshu on 25/4/16.
 */
public class ClickFilter
{
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}