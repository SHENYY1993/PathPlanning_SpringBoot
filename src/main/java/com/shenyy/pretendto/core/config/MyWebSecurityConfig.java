package com.shenyy.pretendto.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${spring.security.admin.name}")
    String username;

    @Value("${spring.security.admin.password}")
    String password;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("root").password(new BCryptPasswordEncoder(10).encode("a123456")).roles("ADMIN", "DBA")
                .and()
                .withUser(username).password(new BCryptPasswordEncoder(10).encode(password)).roles("ADMIN", "USER")
                .and()
                .withUser("shen").password(new BCryptPasswordEncoder(10).encode("a123456")).roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/test/**").hasRole("ADMIN")
                .antMatchers("/redis/**").access("hasAnyRole('ADMIN','USER')")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .csrf().disable();
    }
}
