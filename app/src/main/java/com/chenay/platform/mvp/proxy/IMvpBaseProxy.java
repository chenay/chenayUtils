package com.chenay.platform.mvp.proxy;

import android.os.Bundle;
import android.util.Log;

import com.chenay.platform.mvp.factory.IMvpPresenterFactory;
import com.chenay.platform.mvp.presenter.IMvpBasePresenterN;
import com.chenay.platform.mvp.view.IMvpBaseView;


/**
 *
 * @author Y.Chen5
 * @date 1/5/2018
 * @description 代理实现类，用来管理Presenter的生命周期，还有和view之间的关联
 */

public class IMvpBaseProxy<V extends IMvpBaseView, P extends IMvpBasePresenterN<V>> implements IMvpPresenterProxyInterface<V, P> {
    private static final String TAG = "IMvpBaseProxy";
    /**
     * 获取onSaveInstanceState中bundle的key
     */
    private static final String KEY_PRESENTER = "KEY_PRESENTER";

    private IMvpPresenterFactory<V, P> mFactory;
    private P mPresenter;
    private Bundle mBundle;
    private boolean mIsAttachView;

    public IMvpBaseProxy(IMvpPresenterFactory<V, P> mFactory) {
        this.mFactory = mFactory;
    }

    /**
     * 设置Presenter的工厂类,这个方法只能在创建Presenter之前调用,也就是调用getMvpPresenter()之前，如果Presenter已经创建则不能再修改
     *
     * @param presenterFactory PresenterFactory类型
     */
    @Override
    public void setPresenterFactory(IMvpPresenterFactory<V, P> presenterFactory) {
        if (mPresenter != null) {
            throw new IllegalArgumentException("这个方法只能在getMvpPresenter()之前调用，如果Presenter已经创建则不能再修改");
        }
        this.mFactory = presenterFactory;
    }

    @Override
    public IMvpPresenterFactory<V, P> getPresenterFactory() {
        return mFactory;
    }

    /**
     * 获取创建的Presenter
     *
     * @return 指定类型的Presenter
     * 如果之前创建过，而且是以外销毁则从Bundle中恢复
     */
    @Override
    public P getMvpPresenter() {
        Log.e(TAG, "getMvpPresenter: ");
        if (mFactory != null) {
            if (mPresenter == null) {
                mPresenter = mFactory.createMvpPresenter();
                mPresenter.onCreatePresenter(mBundle == null ? null : mBundle.getBundle(KEY_PRESENTER));
            }
        }
        return mPresenter;
    }

    /**
     * 绑定Presenter 和 view
     *
     * @param mvpView
     */
    public void onResume(V mvpView) {
        getMvpPresenter();
        Log.e(TAG, "onResume: ");
        if (mPresenter != null && !mIsAttachView) {
            mPresenter.onAttachMvpView(mvpView);
            mIsAttachView = true;
        }
    }

    /**
     * 销毁Presenter持有的view
     */
    private void onDetachMvpView() {
        Log.e(TAG, "onDetachMvpView: " );
        if (mPresenter!=null && mIsAttachView) {
            mPresenter.onDetachMvpView();
            mIsAttachView = false;
        }
    }

    /**
     * 销毁Presenter
     */
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        if (mPresenter!=null) {
            onDetachMvpView();
            mPresenter.onDestroyPresenter();
            mPresenter=null;
        }
    }
    /**
     * 意外销毁的时候调用
     * @return Bundle，存入回调给Presenter的Bundle和当前Presenter的id
     */
    public Bundle onSaveInstanceState() {
        Log.e(TAG, "onSaveInstanceState: ");
        Bundle bundle = new Bundle();
        getMvpPresenter();
        if (mPresenter!=null) {
            Bundle presenterBundle = new Bundle();
            //回调presenter
            mPresenter.onSaveInstanceState(presenterBundle);
            bundle.putBundle(KEY_PRESENTER, presenterBundle);
        }
        return bundle;
    }
    /**
     * 意外关闭恢复Presenter
     * @param saveInstanceState 意外关闭时存储的Bundler
     */
    public void onRestoreInstanceState(Bundle saveInstanceState) {
        Log.e(TAG, "onRestoreInstanceState: saveInstanceState=" +saveInstanceState);
        mBundle = saveInstanceState;
    }
}
