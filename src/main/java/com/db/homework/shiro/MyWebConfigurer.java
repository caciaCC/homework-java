package com.db.homework.shiro;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class MyWebConfigurer implements WebMvcConfigurer {

//    @Bean
//    public LoginInterceptor getLoginIntercepter() {
//        return new LoginInterceptor();
//    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
//                .allowedOrigins("http://localhost:8080")
                .allowedOrigins("http://47.115.200.202:9000")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*");
    }
//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(getLoginIntercepter())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/index.html")
//                .excludePathPatterns("/api/login")
//                .excludePathPatterns("/api/logout")
//                .excludePathPatterns("/api/register");
//                感觉没必要，因为loginhelogout没添加到拦截路径下。而不是全局拦截。
//
//    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/api/file/**").addResourceLocations("file:" + "/www/wwwroot/47.105.166.82/img/");
        registry.addResourceHandler("/api/file/**").addResourceLocations("file:" + "d:/img/");

    }
}
