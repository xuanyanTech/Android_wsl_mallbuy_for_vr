package com.huotu.mall.wenslimall.partnermall.ui.unity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.huotu.mall.wenslimall.R;
import com.huotu.mall.wenslimall.partnermall.entity.GiftGridData;
import com.huotu.mall.wenslimall.partnermall.eventbus.GiftPathToAndroidEvent;
import com.huotu.mall.wenslimall.partnermall.eventbus.PhotoPathEvent;
import com.huotu.mall.wenslimall.partnermall.eventbus.PhotoSuccessEvent;
import com.huotu.mall.wenslimall.partnermall.eventbus.UnityBack;
import com.huotu.mall.wenslimall.partnermall.helper.Event;
import com.huotu.mall.wenslimall.partnermall.ui.base.BaseActivity;
import com.huotu.mall.wenslimall.partnermall.unity.MyUnityPlayer;
import com.huotu.mall.wenslimall.partnermall.utils.ClickFilter;
import com.huotu.mall.wenslimall.partnermall.utils.DateUtils;
import com.huotu.mall.wenslimall.partnermall.utils.ImgUtils;
import com.huotu.mall.wenslimall.partnermall.utils.ToastUtils;
import com.unity3d.player.UnityPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;

public class UnityActivity extends BaseActivity implements UnityView {
    private static final String TAG = UnityActivity.class.getSimpleName();
    protected UnityPlayer mUnityPlayer;

    @Bind(R.id.unity_frame_layout)
    FrameLayout unityFrameLayout;
    @Bind(R.id.unity_back)
    ImageView unityBack;
    @Bind(R.id.open_photo)
    ImageView openPhoto;
    @Bind(R.id.take_pictures)
    ImageView takePictures;

    private SoundPool soundPool;//声明一个SoundPool
    private int soundID;//创建某个声音对应的音频ID
    private UnityPresenter presenter;
    private RxDownload mRxDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity);
        ButterKnife.bind(this);
        addUnity();
        initSound();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        addUnity();
    }

    private void addUnity() {
        mUnityPlayer = new MyUnityPlayer(this);
        unityFrameLayout.addView(mUnityPlayer.getView(), FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        //Requesting the Focus
        mUnityPlayer.requestFocus();

        //The main fix of resolving BLACK SCREEN PLAYER ISSUE
        // mUnityPlayer.windowFocusChanged(true);//First fix Line

        initData();
        Register();
    }


    private void initData() {
        if (presenter == null)
            presenter = new UnityPresenter(this);
        if (mRxDownload == null)
            mRxDownload = RxDownload.getInstance(this);
    }

    @SuppressLint("NewApi")
    private void initSound() {
        soundPool = new SoundPool.Builder().build();
        soundID = soundPool.load(this, R.raw.u_takephoto, 1);
    }

    public static final int IMAGE_REQUEST_CODE = 0x102;

    public void openAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
//当然这两步可以合并成  Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);//调用相册照片
    }

    @OnClick({R.id.unity_back, R.id.open_photo, R.id.take_pictures})
    public void onViewClicked(View view) {
        if (ClickFilter.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.unity_back:
                finish();
                break;
            case R.id.open_photo:
                openAlbum();
                break;
            case R.id.take_pictures:
                playSound();
                UnityPlayer.UnitySendMessage("GameManager", "OnPhotoClick", DateUtils.newDate());
                break;
        }
    }

    private void playSound() {
        soundPool.play(
                soundID,
                0.1f,      //左耳道音量【0~1】
                0.5f,      //右耳道音量【0~1】
                0,         //播放优先级【0表示最低优先级】
                0,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1          //播放速度【1是正常，范围从0~2】
        );
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        try {
            mUnityPlayer.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        UnRegister();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void initView() {

    }

    // Pause Unity
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        mUnityPlayer.resume();
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showShortToast(getApplicationContext(), msg);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(PhotoPathEvent event) {
        PhotoPathEvent stickyEvent = EventBus.getDefault().removeStickyEvent(PhotoPathEvent.class);
        // Better check that an event was actually posted before
        if (stickyEvent != null) {
            // Now do something with it
            UnityPlayer.UnitySendMessage("GameManager", "GiftInformation", event.getPath());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GiftPathToAndroidEvent event) {
        presenter.loadGift(getApplicationContext(), event.getGiftPath());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnityBack event) {
        showView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PhotoSuccessEvent event) {
        ImgUtils.saveImageToGallery(getApplicationContext(), event.getPhotoPath());
    }

    @Override
    public void checkFile(final String url, final String type, final GiftGridData data) {
        mRxDownload.receiveDownloadStatus(url)
                .subscribe(new Consumer<DownloadEvent>() {
                    @Override
                    public void accept(DownloadEvent event) throws Exception {
                        //当事件为Failed时, 才会有异常信息, 其余时候为null.
                        if (event.getFlag() == DownloadFlag.FAILED) {
                            Throwable throwable = event.getError();
                            Log.w("Error", throwable);
                        } else if (event.getFlag() == DownloadFlag.COMPLETED) {
                            //利用url获取
                            File[] files = mRxDownload.getRealFiles(url);
                            if (files != null) {
                                File file = files[0];
                               if (type.equals(Event.VIDEO))
                                    presenter.videoToUnity(file.getPath(), data);
                                else if (type.equals(Event.MODEL))
                                    presenter.unzip(file.getPath(), Event.AR_FILE, data);
                            }
                        }
                    }
                });
    }

    @Override
    public void hiddenView() {
        unityBack.setVisibility(View.GONE);
        openPhoto.setVisibility(View.GONE);
        takePictures.setVisibility(View.GONE);
    }

    @Override
    public void showView() {
        unityBack.setVisibility(View.VISIBLE);
        openPhoto.setVisibility(View.VISIBLE);
        takePictures.setVisibility(View.VISIBLE);
    }

}
