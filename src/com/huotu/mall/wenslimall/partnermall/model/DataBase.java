package com.huotu.mall.wenslimall.partnermall.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/9.
 */
public class DataBase implements Serializable{
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
