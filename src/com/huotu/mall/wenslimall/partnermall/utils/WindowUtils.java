package com.huotu.mall.wenslimall.partnermall.utils;

import android.app.Activity;
import android.view.WindowManager;

public
class WindowUtils {

    public static void backgroundAlpha(Activity aty, float alpha)
    {
        WindowManager.LayoutParams lp = aty.getWindow ().getAttributes ();
        lp.alpha = alpha;
        aty.getWindow ().setAttributes ( lp );
    }
}
