package com.example.demo.service;

import com.example.demo.model.Weather;
import com.example.demo.service.interfaces.DayRatingCalculator;
import com.example.demo.service.interfaces.IsComboCalculator;
import com.example.demo.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherDayRatingCalculator implements DayRatingCalculator {

  @Autowired
  private DateUtils dateUtils;

  @Autowired
  private IsComboCalculator isComboCalculator;

  @Override
  public int getDayRating(Weather weather, int coeffDayOfWeek, int coeffRain, int coeffWind,
      int coeffClouds, int coeffTemperature, boolean clouds, boolean rain, boolean wind,
      int coeffCombo) {
    return (dateUtils.getDayOFWeek(weather.getDate()) * coeffDayOfWeek +
        Boolean.compare(weather.isRaining(), false) * coeffRain +
        weather.getWindSpeed() * coeffWind +
        weather.getCloudsPercentage() * coeffClouds +
        (25 - weather.getTemperature()) * coeffTemperature) +
        isComboCalculator.isCombo(clouds, rain, wind) * coeffCombo;
  }
}
