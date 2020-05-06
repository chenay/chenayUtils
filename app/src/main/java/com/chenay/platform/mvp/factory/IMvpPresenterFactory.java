package com.chenay.platform.mvp.factory;

import com.chenay.platform.mvp.presenter.IMvpBasePresenterN;
import com.chenay.platform.mvp.view.IMvpBaseView;

/**
 * Created by Y.Chen5 on 1/5/2018.
 */
public interface IMvpPresenterFactory<V extends IMvpBaseView,P extends IMvpBasePresenterN<V>> {
    /**
     * 创建Presenter的接口方法
     * @return 需要创建的Presenter
     */
    P createMvpPresenter();
}
