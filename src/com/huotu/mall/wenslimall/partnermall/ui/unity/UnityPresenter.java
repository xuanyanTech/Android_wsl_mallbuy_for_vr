package com.huotu.mall.wenslimall.partnermall.ui.unity;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.huotu.mall.wenslimall.partnermall.entity.GiftGridData;
import com.huotu.mall.wenslimall.partnermall.entity.GiftPathEntity;
import com.huotu.mall.wenslimall.partnermall.eventbus.PhotoPathEvent;
import com.huotu.mall.wenslimall.partnermall.helper.Event;
import com.huotu.mall.wenslimall.partnermall.helper.Helper;
import com.huotu.mall.wenslimall.partnermall.info.ARVideoInfo;
import com.huotu.mall.wenslimall.partnermall.mvp.BasePresenter;
import com.huotu.mall.wenslimall.partnermall.net.retrofit.ApiStores;
import com.huotu.mall.wenslimall.partnermall.net.retrofit.AppClient;
import com.huotu.mall.wenslimall.partnermall.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload2.RxDownload;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Shoon on 2017/5/5.
 */

class UnityPresenter extends BasePresenter<UnityView> {
    private ARVideoInfo info;

    UnityPresenter(UnityView mvpView) {
        super(mvpView);
        info = new ARVideoInfo();
    }

    void loadGift(final Context context, final String giftName) {
        AppClient
                .createApi(ApiStores.class)
                .getGiftPath(giftName, Event.ANDROID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GiftPathEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        String msg = "请求出错";
                        getMvpView().showToast(msg);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(GiftPathEntity entity) {
                        checkUrl(context, entity);
                    }
                });
    }

    private void download(final Context context, String url, int giftId) {
        RxDownload.getInstance(context)
                .serviceDownload(
                        url,
                        giftId + url.substring(url.lastIndexOf("."), url.length()),
                        Event.AR_FILE)   //只需传url即可，添加一个下载任务
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        ToastUtils.showShortToast(context, "开始下载");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.w("TAG", throwable);
                        ToastUtils.showShortToast(context, "添加任务失败");
                    }
                });
    }

    void videoToUnity(String filePath, GiftGridData data) {
        info.setPath(filePath);
        String jsonStr = new Gson().toJson(info);
        Log.e("json", jsonStr);
        EventBus.getDefault().postSticky(new PhotoPathEvent(jsonStr, data));
        getMvpView().hiddenView();
    }

    @SuppressWarnings("unchecked")
    void unzip(String zipFilePath, String unzipFilePath, GiftGridData data) throws Exception {
        /**验证是否为空*/
        if (isEmpty(zipFilePath) || isEmpty(unzipFilePath)) {

        }
        File zipFile = new File(zipFilePath);
        /**创建解压缩文件保存的路径*/
        unzipFilePath = unzipFilePath + data.getId();
        File unzipFileDir = new File(unzipFilePath);
        if (!unzipFileDir.exists()) {
            unzipFileDir.mkdirs();
        }

        //开始解压
        ZipEntry entry = null;
        String entryFilePath = null;
        int count = 0, bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();

        //循环对压缩包里的每一个文件进行解压
        while (entries.hasMoreElements()) {
            entry = entries.nextElement();
            Log.e("", "log ing5:" + entry.getName());
            /**这里提示如果当前元素是文件夹时，在目录中创建对应文件夹
             * ，如果是文件，得出路径交给下一步处理*/
            entryFilePath = unzipFilePath + File.separator + entry.getName();
            File file = new File(entryFilePath);
            Log.e("", "~~是否是文件夹:" + file.isDirectory());
            if (entryFilePath.endsWith("/")) {
                if (!file.exists()) {
                    file.mkdir();
                }
                continue;
            }
            /***这里即是上一步所说的下一步，负责文件的写入，不服来咬(≖ ‿ ≖)✧*/
            bos = new BufferedOutputStream(new FileOutputStream(entryFilePath + "/"));
            bis = new BufferedInputStream(zip.getInputStream(entry));
            while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
                bos.write(buffer, 0, count);
            }
            bos.flush();
            bos.close();
        }
        info.setPath(unzipFilePath);
        String jsonStr = new Gson().toJson(info);
        EventBus.getDefault().postSticky(new PhotoPathEvent(jsonStr, data));
        getMvpView().showView();
    }


    private void checkUrl(Context mContext, GiftPathEntity entity) {
        boolean goOn = true;
        String fileType = entity.getAr_url()
                .substring(entity.getAr_url().lastIndexOf("."), entity.getAr_url().length());
        fileType = fileType.contains(".zip") ? "" : fileType;
        String filePath = Event.AR_FILE + entity.getId() + fileType;
        boolean is = Helper.fileIsExists(filePath);
        if (entity.getAr_type().equals(Event.VIDEO)) {
            info.setType("1");
        } else if (entity.getAr_type().equals(Event.MODEL)) {
            info.setType("0");
        }
        String arName = entity.getAr_name();
        info.setModelName(arName != null && !arName.equals("") ? arName : entity.getName());
        if (is) {
            info.setPath(filePath);
            String jsonStr = new Gson().toJson(info);
            EventBus.getDefault().postSticky(new PhotoPathEvent(jsonStr, entity.getGiftData()));
            getMvpView().hiddenView();
            goOn = false;
        }
        if (goOn) {
            download(mContext, entity.getAr_url(), entity.getId());
            getMvpView().checkFile(entity.getAr_url(), entity.getAr_type(), entity.getGiftData());
        }
    }
}
