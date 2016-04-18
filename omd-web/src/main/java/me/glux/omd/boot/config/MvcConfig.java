package me.glux.omd.boot.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import freemarker.template.utility.XmlEscape;
import me.glux.omd.rest.JsonArgumentResolver;
import me.glux.omd.rest.JsonRestMappingHandlerAdapter;


@EnableWebMvc
@ComponentScan
@Configuration
public class MvcConfig{
    
    @Bean
    public FreeMarkerConfigurer freeMarkerConfig(){
        FreeMarkerConfigurer freeMarkerConfig=new FreeMarkerConfigurer();
        freeMarkerConfig.setConfigLocation(new ClassPathResource("freemarker.properties"));
        freeMarkerConfig.setTemplateLoaderPath("classpath:/views/ftl");
        freeMarkerConfig.setDefaultEncoding("utf-8");
        Map<String,Object> variables=new HashMap<>();
        variables.put("xml_escape", new XmlEscape());
        freeMarkerConfig.setFreemarkerVariables(variables);
        return freeMarkerConfig;
    }
    
    @Bean
    public FreeMarkerViewResolver freeMarkerResolver(){
        FreeMarkerViewResolver freeMarkerResolver=new FreeMarkerViewResolver();
        freeMarkerResolver.setViewClass(FreeMarkerView.class);
        freeMarkerResolver.setContentType("text/html; charset=utf-8");
        freeMarkerResolver.setCache(true);
        freeMarkerResolver.setSuffix(".ftl");
        freeMarkerResolver.setRequestContextAttribute("request");
        freeMarkerResolver.setExposeSessionAttributes(true);
        freeMarkerResolver.setExposeRequestAttributes(true);
        freeMarkerResolver.setExposeSpringMacroHelpers(true);
        return freeMarkerResolver;
    }
    
    @Bean
    public JsonRestMappingHandlerAdapter jsonHandler(){
        JsonRestMappingHandlerAdapter hander=new JsonRestMappingHandlerAdapter();
        hander.setOrder(1);
        return hander;
    }
    
    @Configuration
    public static class MyMvcConfig extends WebMvcConfigurerAdapter{
        
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(new JsonArgumentResolver());
        }
        
        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.add(new MappingJackson2HttpMessageConverter());
        }
        
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/res/**")
                    .addResourceLocations("classpath:/res/","classpath:/views/jsp/");
        }
    }
}
