package com.huotu.mall.wenslimall.partnermall.widgets;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.huotu.mall.wenslimall.R;
import com.huotu.mall.wenslimall.partnermall.model.ShareModel;

import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享弹出框
 */
public class SharePopupWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private PlatformActionListener platformActionListener;
    private Platform.ShareParams shareParams;

    public SharePopupWindow ( Context cx) {
        this.context = cx;
    }

    public void setPlatformActionListener ( PlatformActionListener platformActionListener ) {
        this.platformActionListener = platformActionListener;
    }

    public void showShareWindow ( ) {
        View view = LayoutInflater.from ( context ).inflate (  R.layout.share_layout, null  );
        RelativeLayout bgView = (RelativeLayout) view.findViewById(R.id.pop_rootview);
        Button btn_cancel = ( Button ) view.findViewById ( R.id.btn_cancel );
        LinearLayout sharewechat = (LinearLayout) view.findViewById(R.id.sharewechat);
        LinearLayout sharewechatmoments = (LinearLayout) view.findViewById(R.id.sharewechatmoments);
        LinearLayout shareqzone = (LinearLayout) view.findViewById(R.id.shareqzone);
        LinearLayout sharesinaweibo = (LinearLayout) view.findViewById(R.id.sharesinaweibo);
        LinearLayout sharewechatfavorite = (LinearLayout)view.findViewById(R.id.sharewechatFavorite);
        sharewechat.setOnClickListener(this);
        sharewechatmoments.setOnClickListener(this);
        shareqzone.setOnClickListener(this);
        sharesinaweibo.setOnClickListener(this);
        sharewechatfavorite.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        bgView.setOnClickListener(this);

        // 设置SelectPicPopupWindow的View
        this.setContentView ( view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth( LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight( LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimationPop);

        //WindowUtils.backgroundAlpha (  (Activity) context , 0.4f );
        // 实例化一个ColorDrawable颜色为半透明
        //ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        //this.setBackgroundDrawable(dw);

        //this.setBackgroundDrawable(ContextCompat.getDrawable(context , R.drawable.share_window_bg));

        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sharewechat://微信好友
                wechatShare(Wechat.NAME);
                break;
            case R.id.sharewechatmoments://微信朋友圈
                wechatShare(WechatMoments.NAME);
                break;
            case R.id.shareqzone://qq控件分享
                qzone ( );
                break;
            case R.id.sharesinaweibo://sina
                sinaWeibo();
                break;
            case R.id.sharewechatFavorite://微信收藏
                wechatShare(WechatFavorite.NAME);
                break;
            case R.id.btn_cancel://取消
                dismiss();
                break;
            case R.id.pop_rootview://
                dismiss();
                break;
            default:
                break;
        }
        this.dismiss();
    }

    public void wechatShare(String platformName ){
        Platform platform;
        platform = ShareSDK.getPlatform ( context, platformName );
        if (platformActionListener != null) {
            platform.setPlatformActionListener ( platformActionListener );
        }
        platform.share(shareParams);
    }

    /**
     * 初始化分享参数
     *
     * @param shareModel
     */
    public void initShareParams(ShareModel shareModel) {
        if (shareModel != null) {
            Platform.ShareParams sp = new Platform.ShareParams ();
            sp.setShareType(Platform.SHARE_WEBPAGE);
            //sp.setTitle(shareModel.getText());
            sp.setText(shareModel.getText());
            sp.setTitle(shareModel.getTitle());
            sp.setUrl(shareModel.getUrl());
            sp.setImageUrl(shareModel.getImageUrl());
            if( shareModel.getImageData() !=null) {
                sp.setImageData(shareModel.getImageData());
            }
            shareParams = sp;
        }
    }

    /**
     * 分享到QQ空间
     */
    public void qzone() {

        Platform.ShareParams sp = new Platform.ShareParams ();
        sp.setTitle ( shareParams.getTitle ( ) );
        sp.setTitleUrl ( shareParams.getUrl ( ) ); // 标题的超链接
        sp.setText ( shareParams.getText ( ) );
        sp.setImageUrl ( shareParams.getImageUrl ( ) );
        Platform qzone = ShareSDK.getPlatform(context, QZone.NAME);
        qzone.setPlatformActionListener(platformActionListener); // 设置分享事件回调 //
        // 执行图文分享
        qzone.share(sp);
    }

    /**
     * 分享到sina微博
     */
    public void sinaWeibo() {
        Platform.ShareParams sp = new Platform.ShareParams ( );
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(shareParams.getText() + shareParams.getUrl());
        sp.setImageUrl(shareParams.getImageUrl());

        Platform sinaWeibo = ShareSDK.getPlatform ( context, SinaWeibo.NAME );
        sinaWeibo.setPlatformActionListener ( platformActionListener );
        //执行分享
        sinaWeibo.share ( sp );
    }

    /**
     * 判断微博客户端是否可用
     *
     * @param context
     * @return
     */
    public boolean isWeiBoClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.sina.weibo")) {
                    return true;
                }
            }
        }
        return false;
    }
}
