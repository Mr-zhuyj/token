package com.springboot.demo.token.interceptor;

import com.springboot.demo.token.annotation.Token;
import com.springboot.demo.token.bo.TokenMange;
import com.springboot.demo.token.component.SpringContext;
import com.springboot.demo.token.constant.TokenConstant;
import com.springboot.demo.token.model.TokenProperties;
import com.springboot.demo.token.model.TokenModel;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * token拦截器
 * @author zhuyj
 * @date 2020-11-05
 */
@Configuration
public class TokenInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = Logger.getLogger(this.getClass());
    @Resource
    private TokenProperties tokenProperties;

    private TokenMange tokenMange = null;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HttpServlet){
            HttpServlet httpServlet = (HttpServlet) handler;
            Token token = httpServlet.getClass().getAnnotation(Token.class);
            return deal(token,request,response);
        }
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Token token = method.getAnnotation(Token.class);
            return deal(token,request,response);
        }
        return true;
    }

    /**
     * 具体处理内容
     * @param token
     * @param request
     * @return
     */
    private boolean deal(Token token,HttpServletRequest request,HttpServletResponse response) throws Exception{
        if (null == token){
            logger.info("##########无需登录#########");
            return true;
        }else {
            String tokenKey = request.getHeader("token");
            if (null == tokenKey || "".equals(tokenKey)){
                logger.info("##########未登录或登录已超时#########");
                createResponse(response);
                return false;
            }
            Object tokenModel = getToken(tokenKey);
            if (null == tokenModel){
                createResponse(response);
                return false;
            };
            request.setAttribute(TokenConstant.TOKEN_MODEL_NAME,tokenModel);
            return true;
        }
    }

    /**
     * 获取token
     */
    private Object getToken(String code) {
        if (TokenConstant.REDIS.equals(tokenProperties.getType())) {
            tokenMange = (TokenMange) SpringContext.getBean(TokenConstant.REDIS_TOKEN_MANAGE);
            TokenModel tokenModel = new TokenModel();
            tokenModel.setCode(code);
            Object token = tokenMange.getToken(tokenModel);
            if (tokenProperties.isRefresh() && token != null){
                tokenModel.setToken(token);
                tokenMange.updateToken(tokenModel);
            }
            return token;
        } else if (TokenConstant.MEMCACHE.equals(tokenProperties.getType())) {
            tokenMange = (TokenMange) SpringContext.getBean(TokenConstant.MEMCACHE_TOKEN_MANAGE);
            TokenModel tokenModel = new TokenModel();
            tokenModel.setCode(code);
            Object token = tokenMange.getToken(tokenModel);
            if (tokenProperties.isRefresh() && token != null){
                tokenModel.setToken(token);
                tokenMange.updateToken(tokenModel);
            }
            return token;
        }
        return null;
    }

    private void createResponse(HttpServletResponse response) throws Exception{
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().write("##########登录已超时，请重新登录#########");
    }
}
