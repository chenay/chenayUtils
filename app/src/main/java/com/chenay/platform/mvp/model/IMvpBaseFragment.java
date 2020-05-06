package com.chenay.platform.mvp.model;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chenay.platform.mvp.factory.IMvpPresenterFactory;
import com.chenay.platform.mvp.factory.IMvpPresenterFactoryImpl;
import com.chenay.platform.mvp.presenter.IMvpBasePresenterN;
import com.chenay.platform.mvp.proxy.IMvpBaseProxy;
import com.chenay.platform.mvp.proxy.IMvpPresenterProxyInterface;
import com.chenay.platform.mvp.view.IMvpBaseView;

/**
 * Created by Y.Chen5 on 1/8/2018.
 */

public class IMvpBaseFragment <V extends IMvpBaseView, P extends IMvpBasePresenterN <V>> extends Fragment implements IMvpPresenterProxyInterface<V,P> {
    /**
     * 调用onSaveInstanceState时存入Bundle的key
     */
    private static final String KEY_PRESENTER_SAVE = "presenter_save_key";
    /**
     * 创建被代理对象,传入默认Presenter的工厂
     *
     */
    private IMvpBaseProxy<V, P> mProxy = new IMvpBaseProxy<V,P>(IMvpPresenterFactoryImpl.<V, P>createFactory(getClass()));


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null) {
            mProxy.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mProxy.onResume((V) this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProxy.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_PRESENTER_SAVE,mProxy.onSaveInstanceState());
    }

    @Override
    public void setPresenterFactory(IMvpPresenterFactory<V, P> presenterFactory) {
        mProxy.setPresenterFactory(presenterFactory);
    }

    @Override
    public IMvpPresenterFactory<V, P> getPresenterFactory() {
        return mProxy.getPresenterFactory();
    }

    @Override
    public P getMvpPresenter() {
        return mProxy.getMvpPresenter();
    }
}
