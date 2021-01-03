package cn.bestrivenlf.electronadmin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: liufan
 * @Date: 2019/9/16 19:40
 * @Description:
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Value("${file.uploadAbsolutePath}")
    String uploadAbsolutePath;
    @Value("${file.uploadAccessPath}")
    String uploadAccessPath;

    /**
     * 首页显示
     *
     * @return
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        WebMvcConfigurer configurer = new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
//                registry.addViewController("/main.html").setViewName("redirect:/main/entrance");
//                registry.addViewController("/").setViewName("welcome.html");
//                registry.addViewController("/firstPage").setViewName("redirect:/");
//                registry.addViewController("index").setViewName("redirect:/center/entrance");
            }

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {

                //registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/index.html","/index","/");
            }

        };
        return configurer;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uploadAccessPath + "**").addResourceLocations("file:" + uploadAbsolutePath);
    }
}
