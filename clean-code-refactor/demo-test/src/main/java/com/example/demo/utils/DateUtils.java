package com.example.demo.utils;

import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class DateUtils {

  public int getDayOFWeek(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(Calendar.DAY_OF_WEEK);
  }

}

