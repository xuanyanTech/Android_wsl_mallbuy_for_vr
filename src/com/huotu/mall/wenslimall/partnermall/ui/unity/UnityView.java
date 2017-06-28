package com.huotu.mall.wenslimall.partnermall.ui.unity;


import com.huotu.mall.wenslimall.partnermall.entity.GiftGridData;
import com.huotu.mall.wenslimall.partnermall.mvp.MvpView;

/**
 * Created by Shoon on 2017/5/5.
 */

interface UnityView extends MvpView {
    void checkFile(String url, String type, GiftGridData data);

    void hiddenView();

    void showView();

}
