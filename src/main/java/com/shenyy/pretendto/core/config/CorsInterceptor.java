package com.shenyy.pretendto.core.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @description: 拦截器中的方法将按preHandle→Controller→postHandle→afterCompletion的顺序执行。
 * 注意，只有preHandle方法返回true时后面的方法才会执行。
 * 当拦截器链内存在多个拦截器时，postHandler在拦截器链内的所有拦截器返回成功时才会调用，而afterCompletion只有preHandle返回true才调用，但若拦截器链内的第一个拦截器的preHandle方法返回false，则后面的方法都不会执行。
 * @author: shenyy
 * @date: 2023/02/20
 */
public class CorsInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**跨域配置*/
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "*");

        System.out.println("CorsInterceptor>>>preHandle");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("CorsInterceptor>>>postHandle");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("CorsInterceptor>>>afterCompletion");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
