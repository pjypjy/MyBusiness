package com.myBusiness.products.restaurant.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean filterRegistration(){
        // 新建过滤器注册类
        FilterRegistrationBean registration = new FilterRegistrationBean();
        // 添加自定义 过滤器
        registration.setFilter(stringFilter());
        // 设置过滤器的URL模式
        registration.addUrlPatterns("/*");
        //设置过滤器顺序
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public StringFilter stringFilter(){
        return new StringFilter();
    }
}
