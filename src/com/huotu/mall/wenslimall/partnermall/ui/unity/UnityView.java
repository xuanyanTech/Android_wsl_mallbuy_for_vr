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

<<<<<<< HEAD
    void showModelView();

    void hiddenModelView();
=======
>>>>>>> 06b70125fbd8b8c25e3f55c4f3c7f7bc3b06a613
}
