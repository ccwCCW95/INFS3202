package com.ccw.project.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration ir = registry.addInterceptor(loginInterceptor);
        // 拦截路径
        ir.addPathPatterns("/ccw/*");
        ir.addPathPatterns("/ccw/main/*");
        // no interception
        List<String> irs = new ArrayList<String>();
        irs.add("/ccw/login/loadview");
        irs.add("/ccw/login/registration/checkusername");
        irs.add("/ccw/login/registration/checkpassword");
        irs.add("/ccw/login/registration");

        ir.excludePathPatterns(irs);
    }
}
