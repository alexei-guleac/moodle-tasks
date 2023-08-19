package com.example.demo;

import com.example.demo.model.Weather;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WeatherDataTests {

  static Weather weatherSun = new Weather();
  static Weather weatherRain = new Weather();
  static Weather weatheCloud = new Weather();
  static Weather weatheCold = new Weather();
  static Weather weatheWarm = new Weather();

  @BeforeAll
  static void setup() {
    System.out.println("@BeforeAll executed");

    weatherSun.setDate(new Date());
    weatherSun.setCloudsPercentage(35);
    weatherSun.setDistanceVisible(100);
    weatherSun.setPressure(750);
    weatherSun.setRaining(false);
    weatherSun.setTemperature(29);
    weatherSun.setWindSpeed(10);

    weatherRain.setDate(new Date());
    weatherRain.setCloudsPercentage(75);
    weatherRain.setDistanceVisible(20);
    weatherRain.setPressure(730);
    weatherRain.setRaining(true);
    weatherRain.setTemperature(22);
    weatherRain.setWindSpeed(20);

    weatheCloud.setDate(new Date());
    weatheCloud.setCloudsPercentage(100);
    weatheCloud.setDistanceVisible(10);
    weatheCloud.setPressure(750);
    weatheCloud.setRaining(false);
    weatheCloud.setTemperature(28);
    weatheCloud.setWindSpeed(5);

    weatheCold.setDate(new Date());
    weatheCold.setCloudsPercentage(15);
    weatheCold.setDistanceVisible(50);
    weatheCold.setPressure(750);
    weatheCold.setRaining(false);
    weatheCold.setTemperature(-10);
    weatheCold.setWindSpeed(10);

    weatheWarm.setDate(new Date());
    weatheWarm.setCloudsPercentage(0);
    weatheWarm.setDistanceVisible(150);
    weatheWarm.setPressure(760);
    weatheWarm.setRaining(false);
    weatheWarm.setTemperature(35);
    weatheWarm.setWindSpeed(0);
  }

  @Test
  void contextLoads() {
  }

  @Test
  void testWeatherSun() {
    Assertions.assertEquals(35, weatherSun.getCloudsPercentage());
    Assertions.assertEquals(100, weatherSun.getDistanceVisible());
    Assertions.assertEquals(750, weatherSun.getPressure());
    Assertions.assertFalse(weatherSun.isRaining());
    Assertions.assertEquals(29, weatherSun.getTemperature());
    Assertions.assertEquals(10, weatherSun.getWindSpeed());
  }

  @Test
  void testWeatherRain() {
    Assertions.assertEquals(75, weatherRain.getCloudsPercentage());
    Assertions.assertEquals(20, weatherRain.getDistanceVisible());
    Assertions.assertEquals(730, weatherRain.getPressure());
    Assertions.assertTrue(weatherRain.isRaining());
    Assertions.assertEquals(22, weatherRain.getTemperature());
    Assertions.assertEquals(20, weatherRain.getWindSpeed());
  }

  @Test
  void testWeatherCloud() {
    Assertions.assertEquals(100, weatheCloud.getCloudsPercentage());
    Assertions.assertEquals(10, weatheCloud.getDistanceVisible());
    Assertions.assertEquals(750, weatheCloud.getPressure());
    Assertions.assertFalse(weatheCloud.isRaining());
    Assertions.assertEquals(28, weatheCloud.getTemperature());
    Assertions.assertEquals(5, weatheCloud.getWindSpeed());
  }

  @Test
  void testWeatherCold() {
    Assertions.assertEquals(15, weatheCold.getCloudsPercentage());
    Assertions.assertEquals(50, weatheCold.getDistanceVisible());
    Assertions.assertEquals(750, weatheCold.getPressure());
    Assertions.assertFalse(weatheCold.isRaining());
    Assertions.assertEquals(-10, weatheCold.getTemperature());
    Assertions.assertEquals(10, weatheCold.getWindSpeed());
  }

  @Test
  void testWeatherWarm() {
    Assertions.assertEquals(0, weatheWarm.getCloudsPercentage());
    Assertions.assertEquals(150, weatheWarm.getDistanceVisible());
    Assertions.assertEquals(760, weatheWarm.getPressure());
    Assertions.assertFalse(weatheWarm.isRaining());
    Assertions.assertEquals(35, weatheWarm.getTemperature());
    Assertions.assertEquals(0, weatheWarm.getWindSpeed());
  }

  @Tag("DEV")
  @Test
  void testIsDayGoodForWalkSun() {
    Assertions.assertFalse(weatherSun.isDayGoodForWalk());
  }

  @Test
  void testIsDayGoodForWalkRain() {
    Assertions.assertFalse(weatherRain.isDayGoodForWalk());
  }

  @Test
  void testIsDayGoodForWalkCloud() {
    Assertions.assertFalse(weatheCloud.isDayGoodForWalk());
  }

  @Test
  void testIsDayGoodForWalkCold() {
    Assertions.assertFalse(weatheCold.isDayGoodForWalk());
  }

  @Test
  void testIsDayGoodForWalkWarm() {
    Assertions.assertFalse(weatheWarm.isDayGoodForWalk());
  }

  @Tag("DEV")
  @Test
  void testIsDayGoodForFishingSun() {
    Assertions.assertTrue(weatherSun.isDayGoodForFishing());
  }

  @Test
  void testIsDayGoodForFishingRain() {
    Assertions.assertFalse(weatherRain.isDayGoodForFishing());
  }

  @Test
  void testIsDayGoodForFishingCloud() {
    Assertions.assertFalse(weatheCloud.isDayGoodForFishing());
  }

  @Test
  void testIsDayGoodForFishingCold() {
    Assertions.assertFalse(weatheCold.isDayGoodForFishing());
  }

  @Test
  void testIsDayGoodForFishingWarm() {
    Assertions.assertFalse(weatheWarm.isDayGoodForFishing());
  }

  @Tag("DEV")
  @Test
  void testIsDayGoodForDrivingSun() {
    Assertions.assertTrue(weatherSun.isDayGoodForDriving());
  }

  @Test
  void testIsDayGoodForDrivingRain() {
    Assertions.assertFalse(weatherRain.isDayGoodForDriving());
  }

  @Test
  void testIsDayGoodForDrivingCloud() {
    Assertions.assertFalse(weatheCloud.isDayGoodForDriving());
  }

  @Test
  void testIsDayGoodForDrivingCold() {
    Assertions.assertFalse(weatheCold.isDayGoodForDriving());
  }

  @Test
  void testIsDayGoodForDrivingWarm() {
    Assertions.assertTrue(weatheWarm.isDayGoodForDriving());
  }


  @Tag("DEV")
  @Test
  void testIsValidSun() {
    Assertions.assertTrue(weatherSun.isValid());
  }

  @Test
  void testIsValidRain() {
    Assertions.assertTrue(weatherRain.isValid());
  }

  @Test
  void testIsValidCloud() {
    Assertions.assertFalse(weatheCloud.isValid());
  }

  @Test
  void testIsValidCold() {
    Assertions.assertTrue(weatheCold.isValid());
  }

  @Test
  void testIsValidWarm() {
    Assertions.assertFalse(weatheWarm.isValid());
  }

  @Tag("DEV")
  @Test
  void testGetDayRatingSun() {
    Assertions.assertEquals(259, weatherSun.getDayRating(
        0, 0, 5, 7, 10,
        false, false, false, 2));
  }

  @Test
  void testGetDayRatingRain() {
    Assertions.assertEquals(-22, weatherRain.getDayRating(
        0, -1, 1, -1, 10,
        false, false, false, 2));
  }

  @Test
  void testGetDayRatingClouds() {
    Assertions.assertEquals(69, weatheCloud.getDayRating(
        0, 0, 2, 1, 15,
        false, false, false, 2));
  }

  @Test
  void testGetDayRatingCold() {
    Assertions.assertEquals(-46, weatheCold.getDayRating(
        0, 0, 5, 5, -5,
        true, false, true, 2));
  }

  @Test
  void testGetDayRatingWarm() {
    Assertions.assertEquals(44, weatheWarm.getDayRating(
        0, 0, 0, 0, -4,
        false, false, false, 2));
  }

}
