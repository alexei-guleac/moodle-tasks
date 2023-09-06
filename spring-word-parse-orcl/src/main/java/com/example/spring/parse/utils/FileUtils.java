package com.example.spring.parse.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FileUtils {

  public static String getResourceFileAsString(String fileName) {
    InputStream is = getResourceFileAsInputStream(fileName);
    if (is != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    } else {
      throw new RuntimeException("resource not found");
    }
  }

  public static InputStream getResourceFileAsInputStream(String fileName) {
    ClassLoader classLoader = FileUtils.class.getClassLoader();
    return classLoader.getResourceAsStream(fileName);
  }


}
