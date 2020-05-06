package com.chenay.platform.mvp.presenter;

import com.chenay.platform.mvp.view.IMvpBaseView;

/**
 *
 * @author Y.Chen5
 * @date 1/5/2018
 * @description 指定绑定的View必须继承自IMvpBaseView4
 */
@Deprecated
public abstract class IMvpBasePresenter<V extends IMvpBaseView>{

    private V mMvpView;

    /**
     * 绑定V层
     * @param view
     */
    public void attachMvpView(V view) {
        this.mMvpView = view;
    }

    /**
     * 解除绑定
     */
    public void deachMvpView() {
        mMvpView=null;
    }

    /**
     * 获取V层
     * @return
     */
    public V getmMvpView() {
        return mMvpView;
    }
}
