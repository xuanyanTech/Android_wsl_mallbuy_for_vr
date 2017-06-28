package com.huotu.mall.wenslimall.partnermall.model;

/**
 * 订单详情
 */
public class OrderModel {
    private int code;
    private String msg;
    private OrderData data;

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

    public OrderData getData() {
        return data;
    }

    public void setData(OrderData data) {
        this.data = data;
    }

    public class OrderData {
        private String Final_Amount;
        private String ToStr;

        public void setFinal_Amount(String final_Amount) {
            Final_Amount = final_Amount;
        }

        public String getFinal_Amount() {
            return Final_Amount;
        }

        public String getToStr() {
            return ToStr;
        }

        public void setToStr(String toStr) {
            ToStr = toStr;
        }
    }
}
