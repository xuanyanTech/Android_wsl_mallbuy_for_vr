package com.huotu.mall.wenslimall.partnermall.widgets;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huotu.mall.wenslimall.R;
import com.huotu.mall.wenslimall.partnermall.listener.PoponDismissListener;
import com.huotu.mall.wenslimall.partnermall.utils.DensityUtils;
import com.huotu.mall.wenslimall.partnermall.utils.WindowUtils;


/**
 * 消息显示框
 */
public class NoticePopWindow extends PopupWindow {
    private Activity aty;
    private ImageView closeImg;
    private TextView notice;
    private String msg;

    public NoticePopWindow(Activity aty, String msg) {
        this.aty = aty;
        this.msg = msg;
    }

    public void showNotice() {
        View view = LayoutInflater.from(aty).inflate(R.layout.notice_ui, null);
        closeImg = (ImageView) view.findViewById(R.id.notice_close);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissView();
            }
        });
        notice = (TextView) view.findViewById(R.id.notice_text);
        notice.setText(msg);

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((DensityUtils.getScreenW(aty) / 4) * 3);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((DensityUtils.getScreenW(aty) / 6) * 2);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        WindowUtils.backgroundAlpha(aty, 0.4f);
    }

    public void dismissView() {
        setOnDismissListener(new PoponDismissListener(aty));
        dismiss();
    }
}
