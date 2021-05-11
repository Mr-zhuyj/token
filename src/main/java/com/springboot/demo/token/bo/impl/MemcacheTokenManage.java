package com.springboot.demo.token.bo.impl;

import com.springboot.demo.token.component.SpringContext;
import com.springboot.demo.token.model.TokenProperties;
import com.springboot.demo.token.model.TokenModel;
import net.rubyeye.xmemcached.MemcachedClient;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * memcache存储token管理类
 * @author zhuyj
 * @date 2020-11-05
 */
@Component
@DependsOn("springContext")
public class MemcacheTokenManage extends AbstractTokenManage{
    private final Logger logger = Logger.getLogger(this.getClass());

    private MemcachedClient memcachedClient = null;

    private TokenProperties tokenProperties = null;

    private MemcacheTokenManage(){
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        if (tokenProperties == null){
            tokenProperties = SpringContext.getBean("tokenProperties", TokenProperties.class);
        }
        if (memcachedClient == null){
            memcachedClient = SpringContext.getBean(tokenProperties.getClient(),MemcachedClient.class);
        }
    }

    /**
     * 创建token
     * @param tokenModel
     * @return
     */
    @Override
    public String createToken(TokenModel tokenModel){
        config(tokenModel,tokenProperties);
        tokenModel.setCode(getKey());
        String key = getKey(tokenModel);
        try {
            memcachedClient.add(key,tokenModel.getTimeout(),tokenModel.getToken());
        } catch (Exception e) {
            logger.error("生成token失败：",e);
            return null;
        }
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
        try {
            return memcachedClient.get(key);
        } catch (Exception e) {
            logger.error("获取token失败：",e);
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
        try {
            memcachedClient.set(key,tokenModel.getTimeout(),tokenModel.getToken());
        } catch (Exception e) {
            logger.error("更新token失败：",e);
            return false;
        }
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
        try {
            memcachedClient.delete(key);
        } catch (Exception e) {
            logger.error("删除token失败：",e);
            return false;
        }
        return true;
    }
}
