package com.huotu.mall.wenslimall.partnermall.helper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by liumingshu on 23/8/16.
 */
public class JSONRequestBodyConverter<T> implements Converter<T, RequestBody> {
    static final JSONRequestBodyConverter<Object> INSTANCE = new JSONRequestBodyConverter<>();
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain; charset=UTF-8");

    private JSONRequestBodyConverter() {
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, String.valueOf(value));
    }
}