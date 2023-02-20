package com.shenyy.pretendto.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: 自定义类实现WebMvcConfigurer接口，实现接口中的addInterceptors方法。其中，addPathPatterns表示拦截路径，excludePathPatterns表示排除的路径。
 * @author: shenyy
 * @date:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CorsInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/test/testOk");
    }
}
