package com.example.jasper.util;

import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class DateUtils {

  public DateTimeFormatter getDateTimeFormatter() {
    return DateTimeFormatter.ofPattern("d/MM/yyyy");
  }

}
