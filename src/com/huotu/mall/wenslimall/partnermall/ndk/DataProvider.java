package com.huotu.mall.wenslimall.partnermall.ndk;


import android.util.Log;

import com.huotu.mall.wenslimall.partnermall.eventbus.GiftPathToAndroidEvent;
import com.huotu.mall.wenslimall.partnermall.eventbus.PhotoSuccessEvent;
import com.huotu.mall.wenslimall.partnermall.eventbus.UnityBack;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Shoon on 2017/4/27.
 */

public class DataProvider {

    //C调用java中参数为string的方法
    public void getGiftPathToAndroid(String s) {
        EventBus.getDefault().post(new GiftPathToAndroidEvent(s));
        Log.i("Unity", "gift" + s);
    }

    public void photoSucceedToAndroid(String s) {
        EventBus.getDefault().post(new PhotoSuccessEvent(s));
        Log.i("Unity", "photo" + s);
    }


    public void unityBack() {
        EventBus.getDefault().post(new UnityBack());
    }
}
