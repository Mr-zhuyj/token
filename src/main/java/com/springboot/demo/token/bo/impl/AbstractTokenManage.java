package com.springboot.demo.token.bo.impl;

import com.springboot.demo.token.bo.TokenMange;
import com.springboot.demo.token.model.TokenProperties;
import com.springboot.demo.token.model.TokenModel;

import java.util.UUID;

/**
 * token管理抽象类，提供token存取操作的公共方法
 * @author zhuyj
 * @date 2020-11-05
 */
public abstract class AbstractTokenManage implements TokenMange {

    protected String getKey(){
        return UUID.randomUUID().toString().replace("-","");
    }

    protected String getKey(TokenModel tokenModel){
        return tokenModel.getSystem() + "." + tokenModel.getCode();
    }

    /**
     * 从配置中获取configTokenModel值，传给tokenModel
     * @param tokenModel
     * @param tokenProperties
     */
    protected void config(TokenModel tokenModel, TokenProperties tokenProperties){
        if (tokenProperties.getSystem() != null && tokenProperties.getSystem() != ""){
            tokenModel.setSystem(tokenProperties.getSystem());
        }
        tokenModel.setRefresh(tokenProperties.isRefresh());
        if (tokenProperties.getTimeout() > 0){
            tokenModel.setTimeout(tokenProperties.getTimeout());
        }
    }
}
