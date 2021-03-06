package com.github.guawazi.common.data.network;

import com.github.guawazi.common.R;
import com.github.guawazi.common.data.network.exception.ApiException;
import com.github.guawazi.common.data.network.okhttputils.NetworkUtil;
import com.github.guawazi.common.ui.mvp.MvpView;
import com.github.guawazi.common.util.CommonUtils;
import com.github.guawazi.common.util.Constant;

import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;


public abstract class RxObserver<T> extends DisposableObserver<T> {

    private MvpView mView;
    private String mMsg;
    private boolean isShowDialog;

    public RxObserver(MvpView view, String msg, boolean showDialog) {
        this.mView = view;
        this.mMsg = msg;
        this.isShowDialog = showDialog;
    }

    public RxObserver(MvpView view){
        this(view, CommonUtils.getString(R.string.api_loading), true);
    }

    public RxObserver(MvpView view, boolean showDialog){
        this(view, CommonUtils.getString(R.string.api_loading), showDialog);
    }

    @Override
    public void onStart() {
        if(mView == null || mView.isDestroyed()) return;
        if(isShowDialog) mView.showLoading(mMsg);
    }

    @Override
    public void onNext(T bean) {
        if(mView == null || mView.isDestroyed()) return;
        onSuccess(bean);
    }

    @Override
    public void onError(Throwable e) {
        if(mView == null || mView.isDestroyed()) return;
        ResultBean bean;
        if(!NetworkUtil.isConnected()){
            bean = new ResultBean(Constant.STATUS_DISCONNECT, CommonUtils.getString(R.string.api_net_disable));
        }else if(e instanceof ApiException){
            bean = ((ApiException) e).getBean();
        }else if(e instanceof SocketTimeoutException){
            bean = new ResultBean(Constant.STATUS_TIMEOUT, CommonUtils.getString(R.string.api_net_timeout));
        }else {
            bean = new ResultBean(Constant.STATUS_ERROR, CommonUtils.getString(R.string.api_net_error));
        }
        mView.showError(bean.getMsg());
        onFailed(bean);
    }

    /**
     * 成功回调方法
     */
    public abstract void onSuccess(T bean);

    /**
     * 失败回调方法
     */
    public void onFailed(ResultBean bean) {
        mView.showError(bean.getMsg());
    }

    @Override
    public void onComplete() {
        RxFlowable.disposable(this);
    }

}
