package com.episen.ing3.fise.springbootlockauthentification.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder().encode("user1password"))
                .roles("REDACTEUR");
        auth.inMemoryAuthentication()
                .withUser("user2").password(passwordEncoder().encode("user3password"))
                .roles("RELECTEUR");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // allow documentation to be embedded in an iframe
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT,"/api/**/status").hasRole("RELECTEUR")
                .antMatchers(HttpMethod.POST,"/api/**/documents").hasRole("REDACTEUR")
                .antMatchers(HttpMethod.PUT,"/api/**/documents").hasAnyRole("REDACTEUR","RELECTEUR")
                .antMatchers(HttpMethod.GET,"/api/**/documents/{documentsId}").permitAll()
                .antMatchers(HttpMethod.GET,"/api/**/documents").permitAll()
                .antMatchers(HttpMethod.GET,"/api/**/lock").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/**/lock").hasAnyRole("REDACTEUR","RELECTEUR")
                .antMatchers(HttpMethod.DELETE,"/api/**/lock").hasAnyRole("REDACTEUR","RELECTEUR")

                //.antMatchers("/api/**/documents").permitAll()


                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
