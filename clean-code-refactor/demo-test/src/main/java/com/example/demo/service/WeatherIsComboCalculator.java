package com.example.demo.service;

import com.example.demo.service.interfaces.IsComboCalculator;
import org.springframework.stereotype.Service;

@Service
public class WeatherIsComboCalculator implements IsComboCalculator {

  @Override
  public int isCombo(boolean clouds, boolean rain, boolean wind) {
    if (clouds && rain && wind) {
      return 1;
    }
    if ((clouds && !rain && !wind) || (!clouds && !rain && wind) || (!clouds && rain && !wind)) {
      return 3;
    }
    return 2;
  }

}
