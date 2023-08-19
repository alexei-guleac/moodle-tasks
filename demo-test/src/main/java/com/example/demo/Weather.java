package com.example.demo;


import java.util.Calendar;
import java.util.Date;

import static com.example.demo.Constants.HISTORICAL_MAX_VALUE;
import static com.example.demo.Constants.HISTORICAL_MIN_VALUE;


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

    public Weather(Date date, boolean isRaining, int windSpeed, int temperature, int cloudsPercentage, long distanceVisible, int pressure) {
        this.date = date;
        this.isRaining = isRaining;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.cloudsPercentage = cloudsPercentage;
        this.distanceVisible = distanceVisible;
        this.pressure = pressure;
    }

    public int getDayRating(int coeffDayOfWeek, int coeffRain, int coeffWind, int coeffClouds, int coeffTemperature,
                            boolean clouds, boolean rain, boolean wind, int coeffCombo) {
        return (this.getDayOFWeek() * coeffDayOfWeek +
                Boolean.compare(this.isRaining, false) * coeffRain +
                this.windSpeed * coeffWind +
                this.cloudsPercentage * coeffClouds +
                (25 - this.temperature) * coeffTemperature) +
                isCombo(clouds, rain, wind) * coeffCombo;
    }

    private int isCombo(boolean clouds, boolean rain, boolean wind) {
        if (clouds && rain && wind) {
            return 1;
        }
        if ((clouds && !rain && !wind) || (!clouds && !rain && wind) || (!clouds && rain && !wind)) {
            return 3;
        }
        return 2;
    }

    public boolean isDayGoodForWalk() {
        return (this.temperature > 15 && this.temperature < 30) && !this.isRaining
                && (this.cloudsPercentage > 0 && this.cloudsPercentage < 50)
                && (this.windSpeed > 0 && this.windSpeed < 3);
    }

    public boolean isDayGoodForFishing() {
        return (this.temperature > 15 && this.temperature < 30) && !this.isRaining
                && (this.cloudsPercentage > 0 && this.cloudsPercentage < 50)
                && (this.pressure > 700 && this.pressure < 800);
    }

    public boolean isDayGoodForDriving() {
        return !this.isRaining && (this.cloudsPercentage < 50)
                && this.distanceVisible > 50;
    }

    public boolean isValid() {
        return this.windSpeed > 0
                && (this.temperature > HISTORICAL_MIN_VALUE && this.temperature < HISTORICAL_MAX_VALUE)
                && (this.cloudsPercentage > 0 && this.cloudsPercentage < 100);
    }

    private int getDayOFWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.date);
        return c.get(Calendar.DAY_OF_WEEK);
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
