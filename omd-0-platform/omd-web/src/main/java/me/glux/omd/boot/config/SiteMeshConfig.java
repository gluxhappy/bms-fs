package me.glux.omd.boot.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteMeshConfig {

    
    @Bean
    public MySiteMeshFilter sitemesh3(){
        return new MySiteMeshFilter();
    }
    
    public static class MySiteMeshFilter extends ConfigurableSiteMeshFilter {
        
        @Override
        protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
            builder.addDecoratorPaths("/**","/profile/info","/main")
            .addExcludedPath("/auth/**")
            .addExcludedPath("/profile/info");
     
        }
    }
}