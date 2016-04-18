package me.glux.omd.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import me.glux.omd.security.OmdUserDetailService;

@Configuration
@EnableWebSecurity
public class Security {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Configuration
    public static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private OmdUserDetailService userDetailService;
        

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
        }
    }

    @Bean
    public SiteWebSecurityConfigurationAdapter applicationSecurity() {
        return new SiteWebSecurityConfigurationAdapter();
    }

    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    public static class SiteWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/auth/login**","/res/**").permitAll()
                    .antMatchers("/redis/**").hasAnyAuthority("REDIS")
                    .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                    .antMatchers("/main","/profile/**").hasAnyAuthority("USER")
                    .and()
                    .formLogin()
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/auth/login.do")
                        .loginPage("/auth/login")
                        .failureUrl("/auth/login?error=true")
                        .defaultSuccessUrl("/main")
                    .and()
                        .logout()
                            .logoutUrl("/auth/logout")
                            .logoutSuccessUrl("/auth/login")
                            .invalidateHttpSession(true)
                    .and()
                    .authorizeRequests()
                            .anyRequest().permitAll();
            http
                .headers().frameOptions().disable()
                .and().headers().xssProtection().disable();
                
          // @formatter:on
        }
    }
}
