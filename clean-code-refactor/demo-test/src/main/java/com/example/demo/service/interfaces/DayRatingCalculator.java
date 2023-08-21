package com.example.demo.service.interfaces;

import com.example.demo.model.Weather;

public interface DayRatingCalculator {

  int getDayRating(Weather weather, int coeffDayOfWeek, int coeffRain, int coeffWind,
      int coeffClouds,
      int coeffTemperature,
      boolean clouds, boolean rain, boolean wind, int coeffCombo);

}