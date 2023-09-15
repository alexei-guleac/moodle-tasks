package com.example.spring.libra.util;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Start {

  @Autowired
  PasswordEncoder passwordEncoder;

  @PostConstruct
  public void st() {
    System.out.println(passwordEncoder.encode("password"));
  }


}
