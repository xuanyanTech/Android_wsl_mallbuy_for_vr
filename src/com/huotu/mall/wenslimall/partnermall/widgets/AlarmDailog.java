package com.huotu.mall.wenslimall.partnermall.widgets;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huotu.mall.wenslimall.R;


public class AlarmDailog extends Toast {
    private Toast toast;
    private Context context;
    private String dialogNotice;
    private TextView noticeText;

    public AlarmDailog(Context context, String dialogNotice) {
        super(context);
        this.context = context;
        this.dialogNotice = dialogNotice;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_alarm_ui, null);
        noticeText = (TextView) layout.findViewById(R.id.noticeText);
        noticeText.setText(dialogNotice);
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        //让Toast显示为我们自定义的样子 
        toast.setView(layout);
    }

    public void show() {
        toast.show();
    }

}
