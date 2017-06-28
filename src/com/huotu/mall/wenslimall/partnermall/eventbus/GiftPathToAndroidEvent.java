package com.huotu.mall.wenslimall.partnermall.eventbus;

/**
 * Created by Shoon on 2017/5/5.
 */

public class GiftPathToAndroidEvent {
    private String giftPath;

    public GiftPathToAndroidEvent(String giftPath) {
        this.giftPath = giftPath;
    }

    public String getGiftPath() {
        return giftPath;
    }
}
