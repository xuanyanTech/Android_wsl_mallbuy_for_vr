package com.huotu.mall.wenslimall.partnermall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huotu.mall.wenslimall.partnermall.BaseApplication;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 创建广播注册微信api
 */
public class AppRegister extends BroadcastReceiver {
    @Override
    public void onReceive ( Context context, Intent intent ) {
        final IWXAPI api = WXAPIFactory.createWXAPI ( context, null );
        //api.registerApp( Constants.WXPAY_ID );
        api.registerApp(BaseApplication.single.readWxpayAppId ());
    }
}
