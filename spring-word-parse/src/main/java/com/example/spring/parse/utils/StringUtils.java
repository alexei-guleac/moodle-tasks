package com.example.spring.parse.utils;

import java.util.Optional;

public class StringUtils {

  public static String removeLastCharacter(String str) {
    return Optional.ofNullable(str)
        .filter(sStr -> sStr.length() != 0)
        .map(sStr -> sStr.substring(0, sStr.length() - 1))
        .orElse(str);
  }
}
