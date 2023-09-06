package com.example.spring.parse.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .formLogin()
        .and().headers().frameOptions().disable()
        .and().csrf().disable()
        .csrf().ignoringAntMatchers("/h2-console/**", "/wd/**")
        .and().authorizeRequests()
        .antMatchers("/h2-console/**").permitAll()
        .antMatchers("/wd/**").permitAll()
        .anyRequest().authenticated();
  }
}
