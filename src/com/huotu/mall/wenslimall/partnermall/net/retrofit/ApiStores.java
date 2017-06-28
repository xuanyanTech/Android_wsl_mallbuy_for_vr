package com.huotu.mall.wenslimall.partnermall.net.retrofit;

import com.huotu.mall.wenslimall.partnermall.entity.GiftPathEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by WuXiaolong
 * on 2016/3/24.
 */
public interface ApiStores {
    @GET("ar_img/{ar_img_key}/gift/")
    Observable<GiftPathEntity> getGiftPath(@Path("ar_img_key") String ar_img_key, @Query("system") String system);
}
