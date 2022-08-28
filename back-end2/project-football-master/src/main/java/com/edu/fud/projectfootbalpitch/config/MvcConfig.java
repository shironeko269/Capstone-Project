package com.edu.fud.projectfootbalpitch.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/resources/ajax/**").addResourceLocations("classpath:/static/ajax/");
        registry.addResourceHandler("/resources/documentation/**").addResourceLocations("classpath:/static/documentation/");
        registry.addResourceHandler("/resources/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/resources/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/resources/plugins/**").addResourceLocations("classpath:/static/plugins/");
        registry.addResourceHandler("/resources/scss/**").addResourceLocations("classpath:/static/scss/");
        registry.addResourceHandler("/resources/usercssjs/**").addResourceLocations("classpath:/static/usercssjs/");
    }

//    private final ApplicationContext applicationContext;
//
//    @Autowired
//    public MvcConfig(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }
//
//
//    @Bean
//    public SpringResourceTemplateResolver templateResolver() {
//        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
//        templateResolver.setApplicationContext(applicationContext);
////        templateResolver.setSuffix(".html");
//        templateResolver.setCharacterEncoding("UTF-8");
//        return templateResolver;
//    }
}