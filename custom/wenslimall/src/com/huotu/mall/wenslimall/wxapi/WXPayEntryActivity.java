package com.huotu.mall.wenslimall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huotu.mall.wenslimall.R;
import com.huotu.mall.wenslimall.partnermall.BaseApplication;
import com.huotu.mall.wenslimall.partnermall.utils.BuyerPayUtil;
import com.huotu.mall.wenslimall.partnermall.utils.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付回调类
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private BaseApplication application;

    @Override
    public void onReq ( BaseReq baseReq ) {
    }

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.pay_result );
        application = (BaseApplication) this.getApplication ();
        api = WXAPIFactory.createWXAPI ( this, application.readWxpayAppId ( ) );
        api.handleIntent ( getIntent ( ), this );
    }

    @Override
    public
    void onResp ( BaseResp resp ) {

        Log.i ( "info","onPayFinish, errCode = " + resp.errCode );

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String msg = "";
            if( resp.errCode== 0){

                BuyerPayUtil.paySuccessCallback( this , resp);

                msg="支付成功";
                this.finish();
                ToastUtils.showLongToast (  msg );
                return;
            }else if( resp.errCode== -1){
                BuyerPayUtil.wxPayErrorCallback(resp);
                msg="支付失败";
                ToastUtils.showLongToast (  msg );
                this.finish();
                return;
            }else if(resp.errCode ==-2){
                BuyerPayUtil.wxPayCancelCallback(resp);
                msg="用户取消支付";
                ToastUtils.showLongToast( msg);
                this.finish();
                return;
            }

            PayResp payResp = (PayResp)resp;
            if(null==payResp){
                Log.i("wxpay>>>payResp=null","");
                msg="支付失败";
                ToastUtils.showLongToast( msg);
                this.finish();
                return;
            }else{
                Log.i("wxpay>>>extData", payResp.extData==null? "": payResp.extData );
                //Log.i("wxpay>>>prepayid",payResp.prepayId);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
}
