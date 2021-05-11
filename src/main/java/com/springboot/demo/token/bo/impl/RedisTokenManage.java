package com.springboot.demo.token.bo.impl;


import com.springboot.demo.token.component.SpringContext;
import com.springboot.demo.token.constant.TokenConstant;
import com.springboot.demo.token.model.TokenProperties;
import com.springboot.demo.token.model.TokenModel;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redis存储token管理类
 * @author zhuyj
 * @date 2020-11-05
 */
@Component
@DependsOn("springContext")
public class RedisTokenManage extends AbstractTokenManage {

    private RedisTemplate redisTemplate = null;
    @Resource
    private TokenProperties tokenProperties;

    private ValueOperations<String, Object> operations = null;

    private RedisTokenManage(){
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        if (redisTemplate == null && TokenConstant.REDIS.equals(tokenProperties.getType())){
            redisTemplate = SpringContext.getBean(tokenProperties.getClient(),RedisTemplate.class);
        }
        operations = redisTemplate.opsForValue();
    }

    /**
     * 创建token
     * @param tokenModel
     * @return
     */
    @Override
    public String createToken(TokenModel tokenModel) {
        config(tokenModel,tokenProperties);
        tokenModel.setCode(getKey());
        String key = getKey(tokenModel);
        operations.set(key,tokenModel.getToken(),tokenModel.getTimeout(), TimeUnit.SECONDS);
        return tokenModel.getCode();
    }

    /**
     * 获取token
     * @param tokenModel
     * @return
     */
    @Override
    public Object getToken(TokenModel tokenModel) {
        config(tokenModel,tokenProperties);
        String key = getKey(tokenModel);
        if (redisTemplate.hasKey(key)){
            return operations.get(key);
        }
        return null;
    }

    /**
     * 修改token
     * @param tokenModel
     * @return
     */
    @Override
    public boolean updateToken(TokenModel tokenModel) {
        config(tokenModel,tokenProperties);
        String key = getKey(tokenModel);
        operations.set(key, tokenModel.getToken(), tokenModel.getTimeout(), TimeUnit.SECONDS);
        return true;
    }

    /**
     * 删除token
     * @param tokenModel
     * @return
     */
    @Override
    public boolean deleteToken(TokenModel tokenModel) {
        config(tokenModel,tokenProperties);
        String key = getKey(tokenModel);
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
        return true;
    }
}
