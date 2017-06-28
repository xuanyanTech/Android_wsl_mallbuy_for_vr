package com.huotu.mall.config;


import com.huotu.mall.wenslimall.BuildConfig;
import com.huotu.mall.wenslimall.partnermall.BaseApplication;

/**
 * Created by Administrator on 2017/1/17.
 */

public class UrlConfig {

    public static String getFooterConfigUrl() {
        String id = BaseApplication.single.readMerchantId();
        String url = BuildConfig.SMART_Url;
        url += "merchantWidgetSettings/search/findByMerchantIdAndScopeDependsScopeOrDefault/nativeCode/" + id + "/global";
        return url;
    }

}
