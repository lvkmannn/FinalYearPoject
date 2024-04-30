package model;

import android.media.Image;

public class Weather {
    String location_id, location_name, date, morning_forecast, afternoon_forecast, night_forecast,
    summary_forecast, summary_when, min_temp, max_temp;

    public Weather(){

    }

    public Weather(String location_id, String location_name, String date, String morning_forecast, String afternoon_forecast, String night_forecast, String summary_forecast, String summary_when, String min_temp, String max_temp) {
        this.location_id = location_id;
        this.location_name = location_name;
        this.date = date;
        this.morning_forecast = morning_forecast;
        this.afternoon_forecast = afternoon_forecast;
        this.night_forecast = night_forecast;
        this.summary_forecast = summary_forecast;
        this.summary_when = summary_when;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMorning_forecast() {
        return morning_forecast;
    }

    public void setMorning_forecast(String morning_forecast) {
        this.morning_forecast = morning_forecast;
    }

    public String getAfternoon_forecast() {
        return afternoon_forecast;
    }

    public void setAfternoon_forecast(String afternoon_forecast) {
        this.afternoon_forecast = afternoon_forecast;
    }

    public String getNight_forecast() {
        return night_forecast;
    }

    public void setNight_forecast(String night_forecast) {
        this.night_forecast = night_forecast;
    }

    public String getSummary_forecast() {
        return summary_forecast;
    }

    public void setSummary_forecast(String summary_forecast) {
        this.summary_forecast = summary_forecast;
    }

    public String getSummary_when() {
        return summary_when;
    }

    public void setSummary_when(String summary_when) {
        this.summary_when = summary_when;
    }

    public String getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(String min_temp) {
        this.min_temp = min_temp;
    }

    public String getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(String max_temp) {
        this.max_temp = max_temp;
    }
}
