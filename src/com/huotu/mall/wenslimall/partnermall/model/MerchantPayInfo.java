package com.huotu.mall.wenslimall.partnermall.model;

import java.util.List;

/**
 * 商户支付信息
 */
public class MerchantPayInfo {

    private int code;//返回码
    private String msg;
    private List<MerchantPayModel> data;

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

    public List<MerchantPayModel> getData() {
        return data;
    }

    public void setData(List<MerchantPayModel> data) {
        this.data = data;
    }

    public class MerchantPayModel {
        private int payType;
        private String payTypeName;
        private String partnerId;
        private String appId;
        private String appKey;
        private String notify;
        private boolean webPagePay;
        //支付方式增加了 payCenterDomain  这个字段 为空：按原有方式自己拼上商城域名 不为空：以该字段拼接
        private String payCenterDomain;

        public boolean isWebPagePay() {
            return webPagePay;
        }

        public void setWebPagePay(boolean webPagePay) {
            this.webPagePay = webPagePay;
        }

        public int getPayType() {
            return payType;
        }

        public String getNotify() {
            return notify;
        }

        public void setNotify(String notify) {
            this.notify = notify;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public String getPayTypeName() {
            return payTypeName;
        }

        public void setPayTypeName(String payTypeName) {
            this.payTypeName = payTypeName;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public void setPartnerId(String partnerId) {
            this.partnerId = partnerId;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getPayCenterDomain() {
            return payCenterDomain;
        }

        public void setPayCenterDomain(String payCenterDomain) {
            this.payCenterDomain = payCenterDomain;
        }
    }
}
