package com.huotu.mall.wenslimall.partnermall.mvp;


/**
 * Created by Shoon on 2016/3/30.
 */
public class BasePresenter<T extends MvpView> implements Presenter<T> {

    private T mMvpView;

    public BasePresenter(T mvpView){
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    private boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    private static class MvpViewNotAttachedException extends RuntimeException {
        MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
