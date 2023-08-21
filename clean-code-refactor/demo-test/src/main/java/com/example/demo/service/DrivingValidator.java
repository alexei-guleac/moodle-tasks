package com.example.demo.service;

import com.example.demo.model.Weather;
import com.example.demo.service.interfaces.ActionValidator;
import org.springframework.stereotype.Service;

@Service
public class DrivingValidator implements ActionValidator {

  @Override
  public boolean isDayGoodForAction(Weather weather) {
    return !weather.isRaining() && (weather.getCloudsPercentage() < 50)
        && weather.getDistanceVisible() > 50;
  }

}
