package com.huotu.mall.wenslimall.partnermall.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.huotu.mall.wenslimall.partnermall.BaseApplication;
import com.huotu.mall.wenslimall.partnermall.config.Constants;
import com.huotu.mall.wenslimall.partnermall.model.AccountModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 授权参数构建类
 */
public class AuthParamUtils {
    protected static String TAG = AuthParamUtils.class.getName();
    private BaseApplication application;
    private String url;
    private long timestamp;

    public AuthParamUtils(BaseApplication application, long timestamp, String url) {
        this.application = application;
        this.timestamp = timestamp;
        this.url = url;
    }

    public AuthParamUtils(String url) {
        this.application = BaseApplication.single;
        this.timestamp = System.currentTimeMillis();
        this.url = url;
    }

    /***
     * 移除url中的特殊参数（unionid，sign，userid , timestamp）
     */
    protected String removeUrlSpecialParameter(String url) {

        Uri uri = Uri.parse(url);
        String path = uri.getPath().toLowerCase();

        String params = uri.getQuery();
        String nparams = "";
        String[] str = params == null ? null : params.split("&");
        if (str != null && str.length > 0) {
            for (String map : str) {
                //获取参数
                String[] values = map.split("=");
                if (values.length < 1) continue;
                String k = values[0].toLowerCase().trim();
                if (k.equals("unionid") || k.equals("buserid") || k.equals("operation") || k.equals("version")
                        || k.equals("sign") || k.equals("timestamp") || k.equals("appid")) {
                    continue;
                }
                if (!TextUtils.isEmpty(nparams)) nparams += "&";
                nparams += map;
            }
        }

        String ho = url.contains("?") ? url.substring(0, url.indexOf("?")) : url;
        String nurl = ho + (TextUtils.isEmpty(nparams) ? "" : "?" + nparams);
        return nurl;
    }


    public String obtainUrl() {
        StringBuilder builder = new StringBuilder();
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            if (application.obtainMerchantUrl() != null && !application.obtainMerchantUrl().equals(url)) {

                url = removeUrlSpecialParameter(url);

                //获取url中的参数
                //String params = url.substring ( url.indexOf ( ".aspx?" ) + 6, url.length ( ) );
                String params = "";
                if (url.contains("?")) {
                    params = url.substring(url.indexOf("?") + 1, url.length());
                }
                String[] str = null;
                if (!TextUtils.isEmpty(params)) {
                    str = params.split("&");
                }
                if (null != str && str.length > 0) {
                    for (String map : str) {
                        //获取参数
                        String[] values = map.split("=");
                        if (2 == values.length) {
                            //paramMap.put ( values[ 0 ], URLEncoder.encode ( values[ 1 ], "UTF-8" ) );
                            paramMap.put(values[0], values[1]);
                        } else if (1 == values.length) {
                            //paramMap.put ( values[ 0 ], null );
                        }
                    }
                }

                //添加额外固定参数
                paramMap.put("version", application.getAppVersion());
                paramMap.put("operation", Constants.OPERATION_CODE);
                paramMap.put("buserId", application.readUserId());
                //1、timestamp
                paramMap.put("timestamp", URLEncoder.encode(String.valueOf(timestamp), "UTF-8"));
                //appid
                paramMap.put("appid", URLEncoder.encode(Constants.getAPP_ID(), "UTF-8"));
                //unionid
                paramMap.put("unionid", URLEncoder.encode(application.readUserUnionId(), "UTF-8"));

                //生成sigin
                paramMap.put("sign", getSign(paramMap));

                builder.append(url);
                builder.append("&timestamp=" + paramMap.get("timestamp"));
                builder.append("&appid=" + paramMap.get("appid"));
                builder.append("&unionid=" + paramMap.get("unionid"));
                builder.append("&sign=" + paramMap.get("sign"));
                builder.append("&buserId=" + application.readUserId());
                builder.append("&version=" + application.getAppVersion());
                builder.append("&operation=" + Constants.OPERATION_CODE);
            } else {
                paramMap.put("version", application.getAppVersion());
                paramMap.put("operation", Constants.OPERATION_CODE);
                paramMap.put("buserId", application.readUserId());
                paramMap.put("customerid", application.readMerchantId());
                //添加额外固定参数
                //1、timestamp
                paramMap.put("timestamp", URLEncoder.encode(String.valueOf(timestamp), "UTF-8"));
                //appid
                paramMap.put("appid", URLEncoder.encode(Constants.getAPP_ID(), "UTF-8"));
                //unionid
                paramMap.put(
                        "unionid", URLEncoder.encode(
                                application.readUserUnionId(),
                                "UTF-8"));
                //生成sigin
                paramMap.put("sign", getSign(paramMap));

                builder.append(url);
                builder.append("?timestamp=" + paramMap.get("timestamp"));
                builder.append("&customerid=" + application.readMerchantId());
                builder.append("&appid=" + paramMap.get("appid"));
                builder.append("&unionid=" + paramMap.get("unionid"));
                builder.append("&sign=" + paramMap.get("sign"));
                builder.append("&buserId=" + application.readUserId());
                builder.append("&version=" + application.getAppVersion());
                builder.append("&operation=" + Constants.OPERATION_CODE);
            }

            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

    }

    /**
     * 获取post参数
     *
     * @return
     */
    public Map obtainParams(AccountModel account) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("customerId", application.readMerchantId());
        paramMap.put("sex", String.valueOf(account.getSex()));
        paramMap.put("nickname", account.getNickname());
        paramMap.put("openid", account.getOpenid());
        paramMap.put("city", account.getCity());
        paramMap.put("country", account.getCountry());
        paramMap.put("province", account.getProvince());
        paramMap.put("headimgurl", account.getAccountIcon());
        paramMap.put("unionid", account.getAccountUnionId());
        paramMap.put("timestamp", String.valueOf(timestamp));
        paramMap.put("appid", Constants.getAPP_ID());
        paramMap.put("version", application.getAppVersion());
        paramMap.put("operation", Constants.OPERATION_CODE);
        paramMap.put("sign", getSign(paramMap));
        //去掉null值
        paramMap = removeNull(paramMap);
        return paramMap;
    }

    private Map removeNull(Map map) {
        Map<String, String> lowerMap = new HashMap<String, String>();
        Iterator lowerIt = map.entrySet().iterator();
        while (lowerIt.hasNext()) {

            Map.Entry entry = (Map.Entry) lowerIt.next();
            Object value = entry.getValue();
            if (null != value) {
                lowerMap.put(String.valueOf(entry.getKey()).toLowerCase(), String.valueOf(value));
            }
        }

        return lowerMap;
    }

    public String obtainUrls() {
        StringBuilder builder = new StringBuilder();
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            //获取url中的参数
            String params = url.substring(url.indexOf("?") + 1, url.length());
            String[] str = params.split("&");
            if (str.length > 0) {
                for (String map : str) {
                    //获取参数
                    String[] values = map.split("=");
                    if (2 == values.length) {
                        //paramMap.put ( values[ 0 ], URLEncoder.encode ( values[ 1 ], "UTF-8" ) );
                        paramMap.put(values[0], values[1]);
                    } else if (1 == values.length) {
                        paramMap.put(values[0], null);
                    }
                }
            }

            //添加额外固定参数
            paramMap.put("version", BaseApplication.getAppVersion());
            paramMap.put("operation", Constants.OPERATION_CODE);
            //1、timestamp
            paramMap.put("timestamp", URLEncoder.encode(String.valueOf(timestamp), "UTF-8"));
            //appid
            paramMap.put("appid", URLEncoder.encode(Constants.getAPP_ID(), "UTF-8"));
            //生成sigin
            paramMap.put("sign", getSign(paramMap));

            builder.append(url);
            builder.append("&timestamp=" + paramMap.get("timestamp"));
            builder.append("&appid=" + paramMap.get("appid"));
            builder.append("&sign=" + paramMap.get("sign"));
            builder.append("&version=" + BaseApplication.getAppVersion());
            builder.append("&operation=" + Constants.OPERATION_CODE);

            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

    }

    public String obtainUrlName() {
        StringBuilder builder = new StringBuilder();
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            //获取url中的参数
            String params = url.substring(url.indexOf("?") + 1, url.length());
            String[] str = params.split("&");
            if (str.length > 0) {
                for (String map : str) {
                    //获取参数
                    String[] values = map.split("=");
                    if (2 == values.length) {
                        //paramMap.put ( values[ 0 ], URLEncoder.encode ( values[ 1 ], "UTF-8" ) );
                        paramMap.put(values[0], values[1]);
                    } else if (1 == values.length) {
                        paramMap.put(values[0], "");
                    }
                }
            }

            //添加额外固定参数
            paramMap.put("version", application.getAppVersion());
            paramMap.put("operation", Constants.OPERATION_CODE);
            //1、timestamp
            paramMap.put("timestamp", URLEncoder.encode(String.valueOf(timestamp), "UTF-8"));
            //appid
            paramMap.put("appid", URLEncoder.encode(Constants.getAPP_ID(), "UTF-8"));
            //生成sigin
            paramMap.put("sign", getSign(paramMap));

            builder.append(url);
            builder.append("&timestamp=" + paramMap.get("timestamp"));
            builder.append("&appid=" + paramMap.get("appid"));
            builder.append("&sign=" + paramMap.get("sign"));
            builder.append("&version=" + application.getAppVersion());
            builder.append("&operation=" + Constants.OPERATION_CODE);

            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

    }

    public String obtainUrlOrder() {
        StringBuilder builder = new StringBuilder();
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            //获取url中的参数
            String params = url.substring(url.indexOf("?") + 1, url.length());
            String[] str = params.split("&");
            if (str.length > 0) {
                for (String map : str) {
                    //获取参数
                    String[] values = map.split("=");
                    if (2 == values.length) {
                        //paramMap.put ( values[ 0 ], URLEncoder.encode ( values[ 1 ], "UTF-8" ) );
                        paramMap.put(values[0], values[1]);
                    } else if (1 == values.length) {
                        paramMap.put(values[0], "");
                    }
                }
            }

            //添加额外固定参数
            paramMap.put("version", application.getAppVersion());
            paramMap.put("operation", Constants.OPERATION_CODE);
            //1、timestamp
            paramMap.put("timestamp", URLEncoder.encode(String.valueOf(timestamp), "UTF-8"));
            //appid
            paramMap.put("appid", URLEncoder.encode(Constants.getAPP_ID(), "UTF-8"));
            //生成sigin
            paramMap.put("sign", getSign(paramMap));

            builder.append(url);
            builder.append("&timestamp=" + paramMap.get("timestamp"));
            builder.append("&appid=" + paramMap.get("appid"));
            builder.append("&sign=" + paramMap.get("sign"));
            builder.append("&version=" + application.getAppVersion());
            builder.append("&operation=" + Constants.OPERATION_CODE);

            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public String getSign(Map map) {
        return getSign(map, Constants.getAPP_SECRET());
    }

    public String getSign(Map map, String secret) {
        String values = this.doSort(map, secret);
        Log.i("sign", values);
        // values = URLEncoder.encode(values);
        //String signHex =DigestUtils.md5DigestAsHex(values.toString().getBytes("UTF-8")).toLowerCase();
        String signHex = EncryptUtil.getInstance().encryptMd532(values);
        Log.i("signHex", signHex);
        return signHex;
    }

    /**
     * @throws
     * @方法描述：获取sign码第二步：参数排序
     * @方法名：doSort
     * @参数：@param map
     * @参数：@return
     * @返回：String
     */
    private String doSort(Map<String, String> map, String secret) {
        //将MAP中的key转成小写
        Map<String, String> lowerMap = new HashMap<String, String>();
        Iterator lowerIt = map.entrySet().iterator();
        while (lowerIt.hasNext()) {
            Map.Entry entry = (Map.Entry) lowerIt.next();
            Object value = entry.getValue();
            if (!TextUtils.isEmpty(String.valueOf(value))) {
                lowerMap.put(String.valueOf(entry.getKey()).toLowerCase(), String.valueOf(value));
            }
        }

        TreeMap<String, String> treeMap = new TreeMap<String, String>(lowerMap);
        StringBuffer buffer = new StringBuffer();
        Iterator it = treeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            buffer.append(entry.getKey() + "=");
            buffer.append(entry.getValue() + "&");
        }
        String suffix = buffer.substring(0, buffer.length() - 1) + secret;//Constants.getAPP_SECRET();
        return suffix;
    }

    public Map obtainParams(Map map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (map != null) {
            paramMap.putAll(map);
        }
        paramMap.put("timestamp", String.valueOf(timestamp));
        paramMap.put("appid", Constants.getAPP_ID());
        paramMap.put("version", BaseApplication.getAppVersion());
        paramMap.put("operation", Constants.OPERATION_CODE);
        paramMap.put("sign", getSign(paramMap));
        //去掉null值
        paramMap = removeNull(paramMap);

        return paramMap;
    }

    public String getEncodeUrl(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
//            //获取url中的参数
//            String params = url.substring ( url.indexOf ( "?" ) + 1, url.length ( ) );
//            String[] str = params.split ( "&" );
//            if ( str.length > 0 ) {
//                for ( String map : str ) {
//                    //获取参数
//                    String[] values = map.split ( "=" );
//                    if ( 2 == values.length ) {
//                        paramMap.put(values[0], values[1]);
//                    }
//                    else if ( 1 == values.length ) {
//                        paramMap.put ( values[ 0 ], "" );
//                    }
//                }
//            }

            if (map != null) {
                paramMap.putAll(map);
            }
            //添加额外固定参数
            paramMap.put("version", application.getAppVersion());
            paramMap.put("operation", Constants.OPERATION_CODE);
            //1、timestamp
            paramMap.put("timestamp", URLEncoder.encode(String.valueOf(timestamp), "UTF-8"));
            //appid
            paramMap.put("appid", URLEncoder.encode(Constants.getAPP_ID(), "UTF-8"));
            //生成sigin
            paramMap.put("sign", getSign(paramMap));

            builder.append(url);
            builder.append("?timestamp=" + paramMap.get("timestamp"));
            builder.append("&appid=" + paramMap.get("appid"));
            builder.append("&sign=" + paramMap.get("sign"));
            builder.append("&version=" + application.getAppVersion());
            builder.append("&operation=" + Constants.OPERATION_CODE);

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue() == null ? "" : entry.getValue().toString();

                try {
                    String valueEncode = URLEncoder.encode(value, "utf-8");
                    builder.append("&" + key + "=" + valueEncode);

                } catch (UnsupportedEncodingException ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }

            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

}
