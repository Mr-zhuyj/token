package com.springboot.demo.token.model;

/**
 * token实体类
 * @author zhuyj
 * @date 2020-11-05
 */
public class TokenModel extends TokenProperties {
    //key
    private String code;
    //tokenModel
    private Object token;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }
}
