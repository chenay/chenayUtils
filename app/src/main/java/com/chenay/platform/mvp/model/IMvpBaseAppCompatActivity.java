package com.chenay.platform.mvp.model;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chenay.platform.mvp.factory.IMvpPresenterFactory;
import com.chenay.platform.mvp.factory.IMvpPresenterFactoryImpl;
import com.chenay.platform.mvp.presenter.IMvpBasePresenterN;
import com.chenay.platform.mvp.proxy.IMvpBaseProxy;
import com.chenay.platform.mvp.proxy.IMvpPresenterProxyInterface;
import com.chenay.platform.mvp.view.IMvpBaseView;

/**
 *
 * @author Y.Chen5
 * @date 1/8/2018
 */

@SuppressLint("LongLogTag")
public class IMvpBaseAppCompatActivity <V extends IMvpBaseView,P extends IMvpBasePresenterN<V>> extends AppCompatActivity implements IMvpPresenterProxyInterface {
    private static final String TAG = "IMvpBaseAppCompatActivity";
    public static final String KEY_SAVE_PRESENTER = "presenter_save_key";
    private IMvpBaseProxy<V, P> mProxy = new IMvpBaseProxy<>(IMvpPresenterFactoryImpl.<V,P>createFactory(getClass()));

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.e(TAG, "onCreate: mProxy="+ mProxy );
        if (savedInstanceState!=null) {
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(KEY_SAVE_PRESENTER));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " );
        mProxy.onResume((V) this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " + isChangingConfigurations());
        mProxy.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState: ");
        outState.putBundle(KEY_SAVE_PRESENTER, mProxy.onSaveInstanceState());
    }

    @Override
    public void setPresenterFactory(IMvpPresenterFactory presenterFactory) {
        Log.e(TAG, "setPresenterFactory: ");
        mProxy.setPresenterFactory(presenterFactory);
    }

    @Override
    public IMvpPresenterFactory getPresenterFactory() {
        Log.e(TAG, "getPresenterFactory: " );
        return mProxy.getPresenterFactory();
    }

    @Override
    public P getMvpPresenter() {
        Log.e(TAG, "getMvpPresenter: ");
        return mProxy.getMvpPresenter();
    }
}
