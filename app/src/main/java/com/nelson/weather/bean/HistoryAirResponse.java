package com.nelson.weather.bean;

import java.util.List;


public class HistoryAirResponse {
    /**
     * code : 200
     * fxLink : http://hfx.link/2ax6
     * airHourly : [{"pubTime":"2020-07-25 00:00","aqi":"52","level":"2","category":"良","primary":"PM10","pm10":"54","pm2p5":"22","no2":"31","so2":"2","co":"0.5","o3":"85"},{"pubTime":"2020-07-25 01:00","aqi":"52","level":"2","category":"良","primary":"PM10","pm10":"54","pm2p5":"22","no2":"45","so2":"2","co":"0.5","o3":"59"},{"pubTime":"2020-07-25 02:00","aqi":"52","level":"2","category":"良","primary":"PM10","pm10":"53","pm2p5":"22","no2":"48","so2":"3","co":"0.5","o3":"53"},{"pubTime":"2020-07-25 03:00","aqi":"49","level":"1","category":"优","primary":"-","pm10":"49","pm2p5":"23","no2":"48","so2":"3","co":"0.5","o3":"48"},{"pubTime":"2020-07-25 04:00","aqi":"51","level":"2","category":"良","primary":"PM10","pm10":"51","pm2p5":"23","no2":"56","so2":"3","co":"0.5","o3":"38"},{"pubTime":"2020-07-25 05:00","aqi":"52","level":"2","category":"良","primary":"PM10","pm10":"53","pm2p5":"24","no2":"57","so2":"3","co":"0.5","o3":"32"},{"pubTime":"2020-07-25 06:00","aqi":"51","level":"2","category":"良","primary":"PM10","pm10":"52","pm2p5":"22","no2":"52","so2":"3","co":"0.5","o3":"35"},{"pubTime":"2020-07-25 07:00","aqi":"55","level":"2","category":"良","primary":"PM10","pm10":"59","pm2p5":"25","no2":"47","so2":"2","co":"0.6","o3":"41"},{"pubTime":"2020-07-25 08:00","aqi":"60","level":"2","category":"良","primary":"PM10","pm10":"69","pm2p5":"27","no2":"41","so2":"3","co":"0.6","o3":"59"},{"pubTime":"2020-07-25 09:00","aqi":"61","level":"2","category":"良","primary":"PM10","pm10":"72","pm2p5":"30","no2":"36","so2":"4","co":"0.7","o3":"88"},{"pubTime":"2020-07-25 10:00","aqi":"62","level":"2","category":"良","primary":"PM10","pm10":"74","pm2p5":"29","no2":"32","so2":"4","co":"0.8","o3":"125"},{"pubTime":"2020-07-25 11:00","aqi":"58","level":"2","category":"良","primary":"PM10","pm10":"66","pm2p5":"30","no2":"25","so2":"4","co":"0.8","o3":"164"},{"pubTime":"2020-07-25 12:00","aqi":"68","level":"2","category":"良","primary":"O3","pm10":"61","pm2p5":"29","no2":"23","so2":"3","co":"0.7","o3":"174"},{"pubTime":"2020-07-25 13:00","aqi":"83","level":"2","category":"良","primary":"O3","pm10":"52","pm2p5":"27","no2":"22","so2":"3","co":"0.7","o3":"186"},{"pubTime":"2020-07-25 14:00","aqi":"97","level":"2","category":"良","primary":"O3","pm10":"41","pm2p5":"25","no2":"18","so2":"3","co":"0.6","o3":"197"},{"pubTime":"2020-07-25 15:00","aqi":"98","level":"2","category":"良","primary":"O3","pm10":"51","pm2p5":"24","no2":"19","so2":"2","co":"0.5","o3":"198"},{"pubTime":"2020-07-25 16:00","aqi":"83","level":"2","category":"良","primary":"O3","pm10":"54","pm2p5":"24","no2":"20","so2":"2","co":"0.5","o3":"186"},{"pubTime":"2020-07-25 17:00","aqi":"74","level":"2","category":"良","primary":"O3","pm10":"59","pm2p5":"24","no2":"21","so2":"2","co":"0.6","o3":"179"},{"pubTime":"2020-07-25 18:00","aqi":"73","level":"2","category":"良","primary":"O3","pm10":"62","pm2p5":"24","no2":"22","so2":"2","co":"0.6","o3":"178"},{"pubTime":"2020-07-25 19:00","aqi":"58","level":"2","category":"良","primary":"PM10","pm10":"66","pm2p5":"25","no2":"24","so2":"3","co":"0.6","o3":"166"},{"pubTime":"2020-07-25 20:00","aqi":"58","level":"2","category":"良","primary":"PM10","pm10":"66","pm2p5":"27","no2":"28","so2":"3","co":"0.6","o3":"147"},{"pubTime":"2020-07-25 21:00","aqi":"66","level":"2","category":"良","primary":"PM10","pm10":"82","pm2p5":"31","no2":"29","so2":"3","co":"0.7","o3":"127"},{"pubTime":"2020-07-25 22:00","aqi":"67","level":"2","category":"良","primary":"PM10","pm10":"83","pm2p5":"32","no2":"26","so2":"4","co":"0.8","o3":"125"},{"pubTime":"2020-07-25 23:00","aqi":"70","level":"2","category":"良","primary":"PM10","pm10":"89","pm2p5":"37","no2":"28","so2":"4","co":"0.9","o3":"120"}]
     * refer : {"sources":["qweather.com"],"license":["commercial license"]}
     */

    private String code;
    private String fxLink;
    private ReferDTO refer;
    private List<AirHourlyDTO> airHourly;

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

    public ReferDTO getRefer() {
        return refer;
    }

    public void setRefer(ReferDTO refer) {
        this.refer = refer;
    }

    public List<AirHourlyDTO> getAirHourly() {
        return airHourly;
    }

    public void setAirHourly(List<AirHourlyDTO> airHourly) {
        this.airHourly = airHourly;
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

    public static class AirHourlyDTO {
        /**
         * pubTime : 2020-07-25 00:00
         * aqi : 52
         * level : 2
         * category : 良
         * primary : PM10
         * pm10 : 54
         * pm2p5 : 22
         * no2 : 31
         * so2 : 2
         * co : 0.5
         * o3 : 85
         */

        private String pubTime = "2021-01-01 00:00";
        private String aqi = "0";
        private String level = "0";
        private String category = "良";
        private String primary = "PM10";
        private String pm10 = "0";
        private String pm2p5= "0";
        private String no2="0";
        private String so2="0";
        private String co="0";
        private String o3="0";

        public String getPubTime() {
            return pubTime;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getPrimary() {
            return primary;
        }

        public void setPrimary(String primary) {
            this.primary = primary;
        }

        public String getPm10() {
            return pm10;
        }

        public void setPm10(String pm10) {
            this.pm10 = pm10;
        }

        public String getPm2p5() {
            return pm2p5;
        }

        public void setPm2p5(String pm2p5) {
            this.pm2p5 = pm2p5;
        }

        public String getNo2() {
            return no2;
        }

        public void setNo2(String no2) {
            this.no2 = no2;
        }

        public String getSo2() {
            return so2;
        }

        public void setSo2(String so2) {
            this.so2 = so2;
        }

        public String getCo() {
            return co;
        }

        public void setCo(String co) {
            this.co = co;
        }

        public String getO3() {
            return o3;
        }

        public void setO3(String o3) {
            this.o3 = o3;
        }
    }
}
