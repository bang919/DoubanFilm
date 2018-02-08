package com.bigbang.doubanfilm.bean.response;

/**
 * Created by Administrator on 2018/2/8.
 */

public class ErrorResponseBean {

    /**
     * msg : Invalid request
     * code : 999
     * request : GET /v2/search
     */

    private String msg;
    private int code;
    private String request;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
