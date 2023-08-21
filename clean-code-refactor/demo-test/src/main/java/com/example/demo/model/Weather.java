package com.example.demo.model;


import java.util.Date;


public class Weather {

  private Date date;

  private boolean isRaining;

  private int windSpeed;

  private int temperature;

  private int cloudsPercentage;

  private long distanceVisible;

  private int pressure;

  public Weather() {
  }

  public Weather(Date date, boolean isRaining, int windSpeed, int temperature, int cloudsPercentage,
      long distanceVisible, int pressure) {
    this.date = date;
    this.isRaining = isRaining;
    this.windSpeed = windSpeed;
    this.temperature = temperature;
    this.cloudsPercentage = cloudsPercentage;
    this.distanceVisible = distanceVisible;
    this.pressure = pressure;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public boolean isRaining() {
    return isRaining;
  }

  public void setRaining(boolean raining) {
    isRaining = raining;
  }

  public int getWindSpeed() {
    return windSpeed;
  }

  public void setWindSpeed(int windSpeed) {
    this.windSpeed = windSpeed;
  }

  public int getTemperature() {
    return temperature;
  }

  public void setTemperature(int temperature) {
    this.temperature = temperature;
  }

  public int getCloudsPercentage() {
    return cloudsPercentage;
  }

  public void setCloudsPercentage(int cloudsPercentage) {
    this.cloudsPercentage = cloudsPercentage;
  }

  public long getDistanceVisible() {
    return distanceVisible;
  }

  public void setDistanceVisible(long distanceVisible) {
    this.distanceVisible = distanceVisible;
  }

  public int getPressure() {
    return pressure;
  }

  public void setPressure(int pressure) {
    this.pressure = pressure;
  }

}
