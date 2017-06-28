package com.huotu.mall.wenslimall.partnermall.mvp;

/**
 * Created by Shoon on 16/8/16.
 */
public interface MvpView {
    void showLoading(String msg);

    void hideLoading();

    void showToast(String msg);
}
