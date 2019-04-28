package com.github.guawazi.common.data.network.exception;


import com.github.guawazi.common.data.network.ResultBean;

public class ApiException extends Exception {
    private ResultBean bean;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(ResultBean bean) {
        super(bean.getMsg());
        this.bean = bean;
    }

    public ResultBean getBean() {
        return bean;
    }

    public void setBean(ResultBean bean) {
        this.bean = bean;
    }

//    public int errorCode;
//    public String message;
//
//    public ApiException(int errorCode, String message){
//        this.errorCode = errorCode;
//        this.message = message;
//    }
}
