package com.springboot.demo.token.bo;


import com.springboot.demo.token.model.TokenModel;
import net.rubyeye.xmemcached.exception.MemcachedException;

import java.util.concurrent.TimeoutException;

/**
 * token管理接口，提供token存取操作的对外开放方法抽象
 * @author zhuyj
 * @date 2020-11-05
 */
public interface TokenMange {

    String createToken(TokenModel tokenModel);

    Object getToken(TokenModel tokenModel);

    boolean updateToken(TokenModel tokenModel);

    boolean deleteToken(TokenModel tokenModel);

}
