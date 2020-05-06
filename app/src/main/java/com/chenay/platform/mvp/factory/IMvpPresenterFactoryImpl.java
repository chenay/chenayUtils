package com.chenay.platform.mvp.factory;

import android.annotation.SuppressLint;
import android.util.Log;

import com.chenay.platform.mvp.presenter.IMvpBasePresenterN;
import com.chenay.platform.mvp.view.IMvpBaseView;

/**
 * 接口实现类
 * Created by Y.Chen5 on 1/5/2018.
 */
public class IMvpPresenterFactoryImpl<V extends IMvpBaseView, P extends IMvpBasePresenterN<V>> implements IMvpPresenterFactory<V, P> {

    /**
     * 需要创建的Presenter的类型
     */
    private final Class<P> mPresenterClass;

    @SuppressLint("LongLogTag")
    public static <V extends IMvpBaseView, P extends IMvpBasePresenterN<V>> IMvpPresenterFactoryImpl<V, P> createFactory(Class<?> viewClass) {
        Log.e("IMvpPresenterFactoryImpl", "createFactory: viewClass=" + viewClass.getName());
        IMvpCreatePresenter annotation = viewClass.getAnnotation(IMvpCreatePresenter.class);
        Class<P> aClass = null;
        if (annotation != null) {
            Log.e("IMvpPresenterFactoryImpl", "createFactory: annotation= " + annotation.getClass());
            aClass = (Class<P>) annotation.value();
            Log.e("IMvpPresenterFactoryImpl", "createFactory: value= " + aClass.getName());
        }
        Log.e("IMvpPresenterFactoryImpl", "createFactory: aClass is null");
        return aClass == null ? null : new IMvpPresenterFactoryImpl<>(aClass);
    }

    private IMvpPresenterFactoryImpl(Class<P> presenterClass) {
        this.mPresenterClass = presenterClass;
    }

    @Override
    public P createMvpPresenter() {
        try {
            return mPresenterClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Presenter创建失败!，检查是否声明了@CreatePresenter(xx.class)注解");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Presenter创建失败!，检查是否声明了@CreatePresenter(xx.class)注解");
        }
    }


}
