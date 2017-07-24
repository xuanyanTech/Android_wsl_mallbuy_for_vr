package com.huotu.mall.wenslimall.partnermall.net.retrofit;


import com.google.gson.Gson;
import com.huotu.mall.wenslimall.partnermall.helper.JSONConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Shoon
 * on 2016/8/16.
 */
public class AppClient {
    private static final String BASETESTURL = "http://www.silkafx.com:8081/api/";
    private static final String FILEPATH = "";
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor
            (new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();


    private AppClient() {
        //construct
    }

    public static <T> T createApi(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASETESTURL)
                .client(okHttpClient)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JSONConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }

    public static <T> T createFileApi(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FILEPATH)
                .client(okHttpClient)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JSONConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }

}
