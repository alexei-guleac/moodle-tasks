package com.example.demo.service;

import static com.example.demo.constants.Constants.HISTORICAL_MAX_VALUE;
import static com.example.demo.constants.Constants.HISTORICAL_MIN_VALUE;

import com.example.demo.model.Weather;
import com.example.demo.service.interfaces.EntityValidator;
import org.springframework.stereotype.Service;

@Service
public class WeatherValidator implements EntityValidator {

  @Override
  public boolean isValid(Weather weather) {
    return weather.getWindSpeed() > 0
        && (weather.getTemperature() > HISTORICAL_MIN_VALUE
        && weather.getTemperature() < HISTORICAL_MAX_VALUE)
        && (weather.getCloudsPercentage() > 0 && weather.getCloudsPercentage() < 100);
  }

}
