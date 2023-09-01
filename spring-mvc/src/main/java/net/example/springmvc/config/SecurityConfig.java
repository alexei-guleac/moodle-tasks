package net.example.springmvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@ComponentScan
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .anyRequest().authenticated()
        .antMatchers("/homePage").access("hasRole('ROLE_USER')")
        .and()
        .formLogin()
        .usernameParameter("username").passwordParameter("password")
        .and()
        .logout();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
    authenticationMgr.inMemoryAuthentication()
        .withUser("user")
        .password("{noop}jd@123")
        .authorities("ROLE_USER");
  }

  @Bean
  public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
    return new HandlerMappingIntrospector();
  }
}