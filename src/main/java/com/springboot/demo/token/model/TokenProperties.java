package com.springboot.demo.token.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * token配置
 * @author zhuyj
 * @date 2020-11-05
 */
@Configuration
@ConfigurationProperties(prefix = "springboot.token")
public class TokenProperties {
    //系统
    private String system = "springboot.token";
    //是否刷新
    private boolean isRefresh = true;
    //超时时间，单位：秒/s
    private int timeout = 15 * 60;
    //类型
    private String type = "memcache";
    //客户端
    private String client = "tokenMemcacheClient";

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
