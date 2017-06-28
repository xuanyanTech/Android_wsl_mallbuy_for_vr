package com.huotu.mall.wenslimall.partnermall.unity;

import android.content.ContextWrapper;

import com.unity3d.player.UnityPlayer;

/**
 * Created by Shoon on 2017/6/21.
 */

public class MyUnityPlayer extends UnityPlayer {
    public MyUnityPlayer(ContextWrapper contextWrapper) {
        super(contextWrapper);
    }

    @Override
    protected void kill() {
    }
}
