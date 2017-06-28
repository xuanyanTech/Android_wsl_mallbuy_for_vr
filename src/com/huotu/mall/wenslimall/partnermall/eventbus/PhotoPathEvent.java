package com.huotu.mall.wenslimall.partnermall.eventbus;


import com.huotu.mall.wenslimall.partnermall.entity.GiftGridData;

/**
 * Created by Shoon on 2017/5/4.
 */

public class PhotoPathEvent {
    private String path;
    private GiftGridData data;


    public PhotoPathEvent(String path, GiftGridData data) {
        this.path = path;
        this.data = data;
    }


    public GiftGridData getData() {
        return data;
    }

    public String getPath() {
        return path;
    }

}
