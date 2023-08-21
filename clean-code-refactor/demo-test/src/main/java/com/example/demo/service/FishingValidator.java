package com.example.demo.service;

import com.example.demo.model.Weather;
import com.example.demo.service.interfaces.ActionValidator;
import org.springframework.stereotype.Service;

@Service
public class FishingValidator implements ActionValidator {

  @Override
  public boolean isDayGoodForAction(Weather weather) {
    return (weather.getTemperature() > 15 && weather.getTemperature() < 30) && !weather.isRaining()
        && (weather.getCloudsPercentage() > 0 && weather.getCloudsPercentage() < 50)
        && (weather.getPressure() > 700 && weather.getPressure() < 800);
  }

}
