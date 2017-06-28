package com.huotu.mall.wenslimall.partnermall.eventbus;

/**
 * Created by Shoon on 2017/5/5.
 */

public class PhotoSuccessEvent {
    private String photoPath;

    public PhotoSuccessEvent(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoPath() {
        return photoPath;
    }
}
