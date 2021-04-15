package com.nelson.weather.bean;

import java.util.ArrayList;
import java.util.List;

public class HistoryResponse {


    /**
     * code : 200
     * fxLink : http://hfx.link/2ax6
     * weatherDaily : {"date":"2020-07-25","sunrise":"05:08","sunset":"19:33","moonrise":"09:54","moonset":"22:40","moonPhase":"峨眉月","tempMax":"33","tempMin":"23","humidity":"52","precip":"0.0","pressure":"1000"}
     * weatherHourly : [{"time":"2020-07-25 00:00","temp":"28","icon":"100","text":"晴","precip":"0.0","wind360":"246","windDir":"西南风","windScale":"2","windSpeed":"8","humidity":"49","pressure":"1001"},{"time":"2020-07-25 01:00","temp":"27","icon":"101","text":"多云","precip":"0.0","wind360":"350","windDir":"北风","windScale":"1","windSpeed":"4","humidity":"57","pressure":"1001"},{"time":"2020-07-25 02:00","temp":"25","icon":"100","text":"晴","precip":"0.0","wind360":"175","windDir":"南风","windScale":"2","windSpeed":"7","humidity":"63","pressure":"1001"},{"time":"2020-07-25 03:00","temp":"25","icon":"104","text":"阴","precip":"0.0","wind360":"359","windDir":"北风","windScale":"1","windSpeed":"5","humidity":"68","pressure":"1001"},{"time":"2020-07-25 04:00","temp":"23","icon":"100","text":"晴","precip":"0.0","wind360":"126","windDir":"东南风","windScale":"1","windSpeed":"3","humidity":"73","pressure":"1000"},{"time":"2020-07-25 05:00","temp":"23","icon":"100","text":"晴","precip":"0.0","wind360":"166","windDir":"东南风","windScale":"1","windSpeed":"4","humidity":"76","pressure":"1001"},{"time":"2020-07-25 06:00","temp":"23","icon":"100","text":"晴","precip":"0.0","wind360":"69","windDir":"东北风","windScale":"1","windSpeed":"2","humidity":"75","pressure":"1001"},{"time":"2020-07-25 07:00","temp":"26","icon":"104","text":"阴","precip":"0.0","wind360":"358","windDir":"北风","windScale":"1","windSpeed":"4","humidity":"71","pressure":"1001"},{"time":"2020-07-25 08:00","temp":"28","icon":"104","text":"阴","precip":"0.0","wind360":"189","windDir":"南风","windScale":"1","windSpeed":"3","humidity":"57","pressure":"1001"},{"time":"2020-07-25 09:00","temp":"30","icon":"104","text":"阴","precip":"0.0","wind360":"350","windDir":"北风","windScale":"1","windSpeed":"3","humidity":"48","pressure":"1001"},{"time":"2020-07-25 10:00","temp":"32","icon":"104","text":"阴","precip":"0.0","wind360":"180","windDir":"南风","windScale":"1","windSpeed":"2","humidity":"48","pressure":"1001"},{"time":"2020-07-25 11:00","temp":"33","icon":"101","text":"多云","precip":"0.0","wind360":"177","windDir":"南风","windScale":"2","windSpeed":"6","humidity":"40","pressure":"1001"},{"time":"2020-07-25 12:00","temp":"32","icon":"101","text":"多云","precip":"0.0","wind360":"222","windDir":"西南风","windScale":"2","windSpeed":"10","humidity":"39","pressure":"1001"},{"time":"2020-07-25 13:00","temp":"32","icon":"101","text":"多云","precip":"0.0","wind360":"187","windDir":"南风","windScale":"2","windSpeed":"10","humidity":"40","pressure":"1000"},{"time":"2020-07-25 14:00","temp":"33","icon":"101","text":"多云","precip":"0.0","wind360":"266","windDir":"西风","windScale":"2","windSpeed":"9","humidity":"36","pressure":"1000"},{"time":"2020-07-25 15:00","temp":"32","icon":"101","text":"多云","precip":"0.0","wind360":"183","windDir":"南风","windScale":"1","windSpeed":"2","humidity":"39","pressure":"1000"},{"time":"2020-07-25 16:00","temp":"32","icon":"101","text":"多云","precip":"0.0","wind360":"189","windDir":"南风","windScale":"1","windSpeed":"4","humidity":"39","pressure":"999"},{"time":"2020-07-25 17:00","temp":"32","icon":"101","text":"多云","precip":"0.0","wind360":"173","windDir":"南风","windScale":"1","windSpeed":"5","humidity":"42","pressure":"999"},{"time":"2020-07-25 18:00","temp":"32","icon":"101","text":"多云","precip":"0.0","wind360":"173","windDir":"南风","windScale":"2","windSpeed":"9","humidity":"39","pressure":"999"},{"time":"2020-07-25 19:00","temp":"31","icon":"101","text":"多云","precip":"0.0","wind360":"185","windDir":"南风","windScale":"1","windSpeed":"4","humidity":"46","pressure":"999"},{"time":"2020-07-25 20:00","temp":"30","icon":"104","text":"阴","precip":"0.0","wind360":"185","windDir":"南风","windScale":"2","windSpeed":"8","humidity":"44","pressure":"999"},{"time":"2020-07-25 21:00","temp":"29","icon":"104","text":"阴","precip":"0.0","wind360":"208","windDir":"西南风","windScale":"2","windSpeed":"7","humidity":"52","pressure":"1000"},{"time":"2020-07-25 22:00","temp":"29","icon":"104","text":"阴","precip":"0.0","wind360":"188","windDir":"南风","windScale":"2","windSpeed":"7","humidity":"57","pressure":"1000"},{"time":"2020-07-25 23:00","temp":"28","icon":"104","text":"阴","precip":"0.0","wind360":"180","windDir":"南风","windScale":"1","windSpeed":"5","humidity":"61","pressure":"1000"}]
     * refer : {"sources":["qweather.com"],"license":["commercial license"]}
     */

    private String code;
    private String fxLink;
    private WeatherDailyDTO weatherDaily = new WeatherDailyDTO();
    private ReferDTO refer = new ReferDTO();
    private List<WeatherHourlyDTO> weatherHourly = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public WeatherDailyDTO getWeatherDaily() {
        return weatherDaily;
    }

    public void setWeatherDaily(WeatherDailyDTO weatherDaily) {
        this.weatherDaily = weatherDaily;
    }

    public ReferDTO getRefer() {
        return refer;
    }

    public void setRefer(ReferDTO refer) {
        this.refer = refer;
    }

    public List<WeatherHourlyDTO> getWeatherHourly() {
        return weatherHourly;
    }

    public void setWeatherHourly(List<WeatherHourlyDTO> weatherHourly) {
        this.weatherHourly = weatherHourly;
    }

    public static class WeatherDailyDTO {
        /**
         * date : 2020-07-25
         * sunrise : 05:08
         * sunset : 19:33
         * moonrise : 09:54
         * moonset : 22:40
         * moonPhase : 峨眉月
         * tempMax : 33
         * tempMin : 23
         * humidity : 52
         * precip : 0.0
         * pressure : 1000
         */

        private String date = "2020-01-01";
        private String sunrise = "07:00";
        private String sunset = "18:00";
        private String moonrise;
        private String moonset;
        private String moonPhase;
        private String tempMax = "0";
        private String tempMin = "0";
        private String humidity = "0";
        private String precip = "0.0";
        private String pressure = "0";

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getMoonrise() {
            return moonrise;
        }

        public void setMoonrise(String moonrise) {
            this.moonrise = moonrise;
        }

        public String getMoonset() {
            return moonset;
        }

        public void setMoonset(String moonset) {
            this.moonset = moonset;
        }

        public String getMoonPhase() {
            return moonPhase;
        }

        public void setMoonPhase(String moonPhase) {
            this.moonPhase = moonPhase;
        }

        public String getTempMax() {
            return tempMax;
        }

        public void setTempMax(String tempMax) {
            this.tempMax = tempMax;
        }

        public String getTempMin() {
            return tempMin;
        }

        public void setTempMin(String tempMin) {
            this.tempMin = tempMin;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getPrecip() {
            return precip;
        }

        public void setPrecip(String precip) {
            this.precip = precip;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }
    }

    public static class ReferDTO {
        private List<String> sources;
        private List<String> license;

        public List<String> getSources() {
            return sources;
        }

        public void setSources(List<String> sources) {
            this.sources = sources;
        }

        public List<String> getLicense() {
            return license;
        }

        public void setLicense(List<String> license) {
            this.license = license;
        }
    }

    public static class WeatherHourlyDTO {
        /**
         * time : 2020-07-25 00:00
         * temp : 28
         * icon : 100
         * text : 晴
         * precip : 0.0
         * wind360 : 246
         * windDir : 西南风
         * windScale : 2
         * windSpeed : 8
         * humidity : 49
         * pressure : 1001
         */

        private String time = "2020-01-01 00:00";
        private String temp = "0";
        private String icon = "100";
        private String text = "晴";
        private String precip = "0.0";
        private String wind360 = "0";
        private String windDir = "西北风";
        private String windScale = "0";
        private String windSpeed = "0";
        private String humidity = "0";
        private String pressure = "0";

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getPrecip() {
            return precip;
        }

        public void setPrecip(String precip) {
            this.precip = precip;
        }

        public String getWind360() {
            return wind360;
        }

        public void setWind360(String wind360) {
            this.wind360 = wind360;
        }

        public String getWindDir() {
            return windDir;
        }

        public void setWindDir(String windDir) {
            this.windDir = windDir;
        }

        public String getWindScale() {
            return windScale;
        }

        public void setWindScale(String windScale) {
            this.windScale = windScale;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }
    }
}
