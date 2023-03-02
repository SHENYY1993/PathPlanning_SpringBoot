package com.shenyy.pretendto.core.config;

import com.shenyy.pretendto.core.sal.UserService;
import com.shenyy.pretendto.core.sal.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${spring.security.admin.name}")
    String username;

    @Value("${spring.security.admin.password}")
    String password;

    @Resource
    UserServiceImpl userServiceImpl;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

//    /**
//     * 配置内存用户
//     */
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("root").password(new BCryptPasswordEncoder(10).encode("a123456")).roles("ADMIN", "DBA")
//                .and()
//                .withUser(username).password(new BCryptPasswordEncoder(10).encode(password)).roles("ADMIN", "USER")
//                .and()
//                .withUser("shen").password(new BCryptPasswordEncoder(10).encode("a123456")).roles("USER");
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/test/**").hasRole("ADMIN")
//                .antMatchers("/redis/**").access("hasAnyRole('ADMIN','USER')")
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/")
//                .permitAll()
//                .and()
//                .logout()
//                .logoutSuccessUrl("/")
//                .permitAll()
//                .and()
//                .csrf().disable();
//    }

    /**
     * 基于数据库的认证
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/test/**").hasRole("admin")
                .antMatchers("/redis/**").access("hasAnyRole('admin','dba')")
                .antMatchers("/user/**").access("hasAnyRole('admin','dba','user')")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login").permitAll()
                .and()
                .csrf().disable();
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_dba > ROLE_admin ROLE_admin > ROLE_user";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }
}
