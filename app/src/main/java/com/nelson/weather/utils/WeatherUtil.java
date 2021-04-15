package com.nelson.weather.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.nelson.weather.R;
import com.nelson.mvplibrary.view.AlwaysMarqueeTextView;

import java.util.Random;

/**
 * 天气工具类
 *
 * @author llw
 */
public class WeatherUtil {

    /**
     * 根据传入的状态码修改填入的天气图标
     *
     * @param weatherStateIcon 显示的ImageView
     * @param code             天气状态码
     */
    public static void changeIcon(ImageView weatherStateIcon, int code) {
        switch (code) {
            //晴
            case 100:
                weatherStateIcon.setBackgroundResource(R.drawable.sunny);
                break;
            //多云
            case 101:
                weatherStateIcon.setBackgroundResource(R.drawable.cloudy);
                break;
            //少云
            case 102:
                weatherStateIcon.setBackgroundResource(R.drawable.cloudy);
                break;
            //晴间多云
            case 103:
                weatherStateIcon.setBackgroundResource(R.drawable.partly_cloudy);
                break;
            //阴 V7
            case 104:
                weatherStateIcon.setBackgroundResource(R.drawable.overcast);
                break;
            //晴 晚上  V7
            case 150:
                weatherStateIcon.setBackgroundResource(R.drawable.clear);
                break;
            //晴间多云 晚上  V7
            case 153:
                weatherStateIcon.setBackgroundResource(R.drawable.partly_cloudy);
                break;
            //阴 晚上  V7
            case 154:
                weatherStateIcon.setBackgroundResource(R.drawable.overcast);
                break;
            //阵雨
            case 300:
                weatherStateIcon.setBackgroundResource(R.drawable.shower_rain_day);
                break;
            //强阵雨
            case 301:
                weatherStateIcon.setBackgroundResource(R.drawable.heavy_rain);
                break;
            //雷阵雨
            case 302:
                weatherStateIcon.setBackgroundResource(R.drawable.thundershower);
                break;
            //强雷阵雨
            case 303:
                weatherStateIcon.setBackgroundResource(R.drawable.thundershower);
                break;
            //雷阵雨伴有冰雹
            case 304:
                weatherStateIcon.setBackgroundResource(R.drawable.thundershower);
                break;
            //小雨
            case 305:
                weatherStateIcon.setBackgroundResource(R.drawable.light_rain);
                break;
            //中雨
            case 306:
                weatherStateIcon.setBackgroundResource(R.drawable.moderate_rain);
                break;
            //大雨
            case 307:
                weatherStateIcon.setBackgroundResource(R.drawable.heavy_rain);
                break;
            //极端降雨
            case 308:
                weatherStateIcon.setBackgroundResource(R.drawable.heavy_rain);
                break;
            //毛毛雨/细雨
            case 309:
                weatherStateIcon.setBackgroundResource(R.drawable.light_rain);
                break;
            //暴雨
            case 310:
                weatherStateIcon.setBackgroundResource(R.drawable.storm);
                break;
            //大暴雨
            case 311:
                weatherStateIcon.setBackgroundResource(R.drawable.heavy_rain);
                break;
            //特大暴雨
            case 312:
                weatherStateIcon.setBackgroundResource(R.drawable.heavy_rain);
                break;
            //冻雨
            case 313:
                weatherStateIcon.setBackgroundResource(R.drawable.heavy_rain);
                break;
            //小到中雨
            case 314:
                weatherStateIcon.setBackgroundResource(R.drawable.light_rain);
                break;
            //中到大雨
            case 315:
                weatherStateIcon.setBackgroundResource(R.drawable.moderate_rain);
                break;
            //大到暴雨
            case 316:
                weatherStateIcon.setBackgroundResource(R.drawable.heavy_rain);
                break;
            //大暴雨到特大暴雨
            case 317:
                weatherStateIcon.setBackgroundResource(R.drawable.heavy_rain);
                break;
            //雨
            case 399:
                weatherStateIcon.setBackgroundResource(R.drawable.light_rain);
                break;
            //阵雨
            case 350:
                weatherStateIcon.setBackgroundResource(R.drawable.shower_rain_day);
                break;
            //强阵雨
            case 351:
                weatherStateIcon.setBackgroundResource(R.drawable.shower_rain_day);
                break;
            //小雪
            case 400:
                weatherStateIcon.setBackgroundResource(R.drawable.light_snow);
                break;
            //中雪
            case 401:
                weatherStateIcon.setBackgroundResource(R.drawable.moderate_snow);
                break;
            //大雪
            case 402:
                weatherStateIcon.setBackgroundResource(R.drawable.heavy_snow);
                break;
            //暴雪
            case 403:
                weatherStateIcon.setBackgroundResource(R.drawable.snowstorm);
                break;
            //雨夹雪
            case 404:
                weatherStateIcon.setBackgroundResource(R.drawable.sleet);
                break;
            //雨雪天气
            case 405:
                weatherStateIcon.setBackgroundResource(R.drawable.snow);
                break;
            //阵雨夹雪
            case 406:
                weatherStateIcon.setBackgroundResource(R.drawable.shower_rain_day);
                break;
            //阵雪
            case 407:
                weatherStateIcon.setBackgroundResource(R.drawable.light_snow);
                break;
            //小到中雪
            case 408:
                weatherStateIcon.setBackgroundResource(R.drawable.light_snow);
                break;
            //中到大雪
            case 409:
                weatherStateIcon.setBackgroundResource(R.drawable.moderate_snow);
                break;
            //大到暴雪
            case 410:
                weatherStateIcon.setBackgroundResource(R.drawable.heavy_snow);
                break;
            //雪
            case 499:
                weatherStateIcon.setBackgroundResource(R.drawable.snow);
                break;
            //薄雾
            case 500:
                weatherStateIcon.setBackgroundResource(R.drawable.foggy);
                break;
            //雾
            case 501:
                weatherStateIcon.setBackgroundResource(R.drawable.foggy);
                break;
            //霾
            case 502:
                weatherStateIcon.setBackgroundResource(R.drawable.haze);
                break;
            //扬沙
            case 503:
                weatherStateIcon.setBackgroundResource(R.drawable.sand);
                break;
            //扬沙
            case 504:
                weatherStateIcon.setBackgroundResource(R.drawable.dust);
                break;
            //沙尘暴
            case 507:
                weatherStateIcon.setBackgroundResource(R.drawable.dust);
                break;
            //强沙尘暴
            case 508:
                weatherStateIcon.setBackgroundResource(R.drawable.dust);
                break;
            //浓雾
            case 509:
                weatherStateIcon.setBackgroundResource(R.drawable.foggy);
                break;
            //强浓雾
            case 510:
                weatherStateIcon.setBackgroundResource(R.drawable.foggy);
                break;
            //大雾
            case 514:
                weatherStateIcon.setBackgroundResource(R.drawable.foggy);
                break;
            //特强浓雾
            case 515:
                weatherStateIcon.setBackgroundResource(R.drawable.foggy);
                break;
            //中度霾
            case 511:
                weatherStateIcon.setBackgroundResource(R.drawable.haze);
                break;
            //重度霾
            case 512:
                weatherStateIcon.setBackgroundResource(R.drawable.haze);
                break;
            //严重霾
            case 513:
                weatherStateIcon.setBackgroundResource(R.drawable.haze);
                break;
            //热
            case 900:
                weatherStateIcon.setBackgroundResource(R.drawable.sunny);
                break;
            //冷
            case 901:
                weatherStateIcon.setBackgroundResource(R.drawable.sunny);
                break;
            //未知
            case 999:
                weatherStateIcon.setBackgroundResource(R.drawable.sunny);
                break;
            default:
                weatherStateIcon.setBackgroundResource(R.drawable.sunny);
                break;
        }
    }

    /**
     * 根据天气描述改变首页背景图
     *
     * @param iv
     * @param code
     */
    public static void changeIllustration(ImageView iv, int code, String time, Context context) {
        boolean isDay = isSunrise(time, context);
        //雷电
        if (code > 301 && code < 305) {
            iv.setImageResource(R.drawable.illu_thunder);
        }
        //阴
        else if (code ==104 || code==154) {
            iv.setImageResource(R.drawable.illu_overcast);
        }
        //雨
        else if (code / 100 == 3) {
            if (isDay)
                iv.setImageResource(R.drawable.illu_rain);
            else
                iv.setImageResource(R.drawable.illu_rain_night);
        }
        //雪
        else if (code / 100 == 4) {
            if (isDay)
                iv.setImageResource(R.drawable.illu_snow);
            else
                iv.setImageResource(R.drawable.illu_snow_night);
        }
        //晴
        if (isDay)
            iv.setImageResource(R.drawable.illu_sunny);
        else
            iv.setImageResource(R.drawable.illu_sunny_night);

    }

    /**
     * 根据天气改变首页综合文案
     *
     * @param tv
     * @param weather
     */
    public static void changeDescription(AlwaysMarqueeTextView tv, String weather) {
        String text = "";
        switch (weather) {
            case "晴":
                text = "今日天气晴朗阳光灿烂，祝你天天都有好心情～";
                break;

            case "晴间多云":
                text = "今日有云，多多开心～";
                break;

            case "多云":
                text = "云朵都是爱你的形状。";
                break;

            case "多云转晴":
                text = "今天多云转甜，烦恼消失大法！";
                break;

            case "阴":
                if (new Random().nextInt() % 2 == 0)
                    text = "愿情话终有主，你我不孤独";
                else
                    text = "没有你的日子，乌云密布";
                break;

            case "阴转晴":
                text = "每一天，都刚刚好";
                break;

            case "雨":
                text = "乌云乌云快走开，你可知道我不常带把伞";
                break;

            case "阵雨":
                text = "雨会停，天会晴，没有什么会永远糟糕透顶";
                break;

            case "雷阵雨":
                text = "你笑时，雷声温柔，暴雨无声";
                break;

            case "小雨":
                text = "在和风细雨中行走，也是一种浪漫";
                break;

            case "中雨":
                text = "天在下雨，我在等雨后的彩虹";
                break;

            case "大雨":
                text = "你知道为什么要下雨吗？因为地球也要洗澡呀！";
                break;

            case "暴雨":
                text = "你笑时，雷声温柔，暴雨无声";
                break;

            case "特大暴雨":
                text = "回首向来萧瑟处，归去，也无风雨也无晴";
                break;

            case "小雪":
                text = "愿所有美好与温暖如约而至";
                break;

            case "中雪":
                text = "张开小手，让雪花躲进小手里面，别让它摔啦";
                break;

            case "大雪":
                text = "好想变成雪花，这样就可以落在你的肩头";
                break;

            case "雪":
                text = "既然青春留不住，下雪还得穿秋裤";
                break;

            case "阵雪":
                text = "应是天仙狂醉，乱把白云揉碎";
                break;

            case "雨夹雪":
                text = "既然青春留不住，下雪还得穿秋裤";
                break;

            case "雾":
                text = "众里寻她千百度，蓦然回首，那人却在灯火阑珊处";
                break;

            case "霾":
                text = "你是散落人间的日常，三三两两";
                break;

            case "浮尘":
                text = "今日有浮尘，记得戴口罩哦～";
                break;

            case "沙尘暴":
                text = "我的小可爱，注意防尘呐";
                break;

        }
        tv.setText(text);
    }

    /**
     * 删除轻度污染的污染二字
     */
    public static String getAirText(String airLevel) {
        if (airLevel.length() > 2) return airLevel.substring(0, airLevel.length()-2);
        else return airLevel;
    }

    /**
     * 根据传入的时间显示时间段描述信息
     *
     * @param timeData
     * @return
     */
    public static String showTimeInfo(String timeData) {
        String timeInfo = null;
        int time = 0;

        if (timeData == null || timeData.equals("")) {
            timeInfo = "获取失败";
        } else {
            time = Integer.parseInt(timeData.trim().substring(0, 2));
            if (time >= 0 && time <= 6) {
                timeInfo = "凌晨";
            } else if (time > 6 && time <= 12) {
                timeInfo = "上午";
            } else if (time > 12 && time <= 13) {
                timeInfo = "中午";
            } else if (time > 13 && time <= 18) {
                timeInfo = "下午";
            } else if (time > 18 && time <= 24) {
                timeInfo = "晚上";
            } else {
                timeInfo = "未知";
            }
        }


        return timeInfo;
    }

    public static void changeAirIcon(ImageView aircontect,  int code) {
        if(code >301) code = 301;
        code = (code-1)/50;
        switch (code) {
            //晴int
            case 0:
                aircontect.setImageResource(R.drawable.bg_7_days_air_quality_1);
                break;
            case 1:
                aircontect.setImageResource(R.drawable.bg_7_days_air_quality_2);
                break;
            case 2:
                aircontect.setImageResource(R.drawable.bg_7_days_air_quality_3);
                break;
            case 3:
                aircontect.setImageResource(R.drawable.bg_7_days_air_quality_4);
                break;
            case 4:
                aircontect.setImageResource(R.drawable.bg_7_days_air_quality_5);
                break;
            case 5:
                aircontect.setImageResource(R.drawable.bg_7_days_air_quality_6);
                break;
        }
    }
    /**
     * 紫外线等级描述
     *
     * @param uvIndex
     * @return
     */
    public static String uvIndexInfo(String uvIndex) {
        String result = null;
        Log.d("uvIndex-->", uvIndex);
        int level = Integer.parseInt(uvIndex);
        if (level <= 2) {
            result = "较弱";
        } else if (level <= 5) {
            result = "弱";
        } else if (level <= 7) {
            result = "中等";
        } else if (level <= 10) {
            result = "强";
        } else if (level <= 15) {
            result = "很强";
        }
        return result;
    }

    /**
     * 根据api的提示转为更为人性化的提醒
     *
     * @param apiInfo
     * @return
     */
    public static String daily_suggest(String apiInfo) {
        String result = null;
        String str = null;
        if (apiInfo.contains("AQI ")) {
            str = apiInfo.replace("AQI ", " ");
        } else {
            str = apiInfo;
        }
        //优，良，轻度污染，中度污染，重度污染，严重污染
        switch (str) {
            case "优":
                result = "今日空气清新，打开窗户呼吸一下新鲜空气吧。";
                break;
            case "良":
                result = "今日空气良好，可适当运动哦。";
                break;
            case "轻度":
                result = "今日空气有轻度污染，防护同时也别忘了偶尔透气。";
                break;
            case "中度":
                result = "今日空气有污染，外出记得带上口罩哦。";
                break;
            case "重度":
                result = "今日空气重度污染，记得关窗，外出记得带口罩。";
                break;
            case "严重":
                result = "今日空气污染严重，尽量不要再出门啦，避免户外运动";
                break;
            default:
                break;
        }
        return result;
    }
    public static String apiToTip(String apiInfo) {
        String result = null;
        String str = null;
        if (apiInfo.contains("AQI ")) {
            str = apiInfo.replace("AQI ", " ");
        } else {
            str = apiInfo;
        }
        //优，良，轻度污染，中度污染，重度污染，严重污染
        switch (str) {
            case "优":
                result = "♪(^∇^*)  空气很好。";
                break;
            case "良":
                result = "ヽ(✿ﾟ▽ﾟ)ノ  空气不错。";
                break;
            case "轻度污染":
                result = "(⊙﹏⊙)  空气有些糟糕。";
                break;
            case "中度污染":
                result = " ε=(´ο｀*)))  唉 空气污染较为严重，注意防护。";
                break;
            case "重度污染":
                result = "o(≧口≦)o  空气污染很严重，记得戴口罩哦！";
                break;
            case "严重污染":
                result = "ヽ(*。>Д<)o゜  完犊子了!空气污染非常严重，要减少出门，定期检查身体，能搬家就搬家吧！";
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 紫外线详细描述
     *
     * @param uvIndexInfo
     * @return
     */
    public static String uvIndexToTip(String uvIndexInfo) {
        String result = null;
        switch (uvIndexInfo) {
            case "较弱":
                result = "紫外线较弱，不需要采取防护措施；若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。";
                break;
            case "弱":
                result = "紫外线弱，可以适当采取一些防护措施，涂擦SPF在12-15之间、PA+的防晒护肤品。";
                break;
            case "中等":
                result = "紫外线中等，外出时戴好遮阳帽、太阳镜和太阳伞等；涂擦SPF高于15、PA+的防晒护肤品。";
                break;
            case "强":
                result = "紫外线较强，避免在10点至14点暴露于日光下.外出时戴好遮阳帽、太阳镜和太阳伞等，涂擦SPF20左右、PA++的防晒护肤品。";
                break;
            case "很强":
                result = "紫外线很强，尽可能不在室外活动，必须外出时，要采取各种有效的防护措施。";
                break;
        }
        return result;
    }

    /**
     * 根据PM2.5含量转换背景图片颜色
     *
     * @param imageView
     * @param primary
     */
    public static void PM2_5_ToIcon(ImageView imageView, String primary) {
        if (primary == null || primary.equals("")) {
            primary = "获取失败";
        } else {
            float prim = Float.parseFloat(primary);
            if (prim >= 0 && prim <= 35) {
                imageView.setImageResource(R.drawable.block_1);
            } else if (prim > 36 && prim <= 75) {
                imageView.setImageResource(R.drawable.block_2);
            } else if (prim > 76 && prim <= 115) {
                imageView.setImageResource(R.drawable.block_3);
            } else if (prim > 116 && prim <= 150) {
                imageView.setImageResource(R.drawable.block_4);
            } else if (prim > 151 && prim <= 250) {
                imageView.setImageResource(R.drawable.block_5);
            } else if (prim > 251 && prim <= 500) {
                imageView.setImageResource(R.drawable.block_6);
            }
        }
    }

    /**
     * 根据PM10含量转换背景图片颜色
     *
     * @param imageView
     * @param primary
     */
    public static void PM10_ToIcon(ImageView imageView, String primary) {
        if (primary == null || primary.equals("")) {
            primary = "获取失败";
        } else {
            float prim = Float.parseFloat(primary);
            if (prim >= 0 && prim <= 50) {
                imageView.setImageResource(R.drawable.block_1);
            } else if (prim > 51 && prim <= 150) {
                imageView.setImageResource(R.drawable.block_2);
            } else if (prim > 151 && prim <= 250) {
                imageView.setImageResource(R.drawable.block_3);
            } else if (prim > 251 && prim <= 350) {
                imageView.setImageResource(R.drawable.block_4);
            } else if (prim > 351 && prim <= 420) {
                imageView.setImageResource(R.drawable.block_5);
            } else if (prim > 421 && prim <= 600) {
                imageView.setImageResource(R.drawable.block_6);
            }
        }
    }

    /**
     * 根据SO2含量转换背景图片颜色
     *
     * @param imageView
     * @param primary
     */
    public static void SO2_ToIcon(ImageView imageView, String primary) {
        if (primary == null || primary.equals("")) {
            primary = "获取失败";
        } else {
            float prim = Float.parseFloat(primary);
            if (prim >= 0 && prim <= 50) {
                imageView.setImageResource(R.drawable.block_1);
            } else if (prim > 51 && prim <= 150) {
                imageView.setImageResource(R.drawable.block_2);
            } else if (prim > 151 && prim <= 475) {
                imageView.setImageResource(R.drawable.block_3);
            } else if (prim > 476 && prim <= 800) {
                imageView.setImageResource(R.drawable.block_4);
            } else if (prim > 801 && prim <= 1600) {
                imageView.setImageResource(R.drawable.block_5);
            } else if (prim > 1601 && prim <= 2100) {
                imageView.setImageResource(R.drawable.block_6);
            }
        }
    }

    /**
     * 根据NO2含量转换背景图片颜色
     *
     * @param imageView
     * @param primary
     */
    public static void NO2_ToIcon(ImageView imageView, String primary) {
        if (primary == null || primary.equals("")) {
            primary = "获取失败";
        } else {
            float prim = Float.parseFloat(primary);
            if (prim >= 0 && prim <= 100) {
                imageView.setImageResource(R.drawable.block_1);
            } else if (prim > 101 && prim <= 200) {
                imageView.setImageResource(R.drawable.block_2);
            } else if (prim > 201 && prim <= 700) {
                imageView.setImageResource(R.drawable.block_3);
            } else if (prim > 701 && prim <= 1200) {
                imageView.setImageResource(R.drawable.block_4);
            } else if (prim > 1201 && prim <= 2340) {
                imageView.setImageResource(R.drawable.block_5);
            } else if (prim > 2341 && prim <= 3840) {
                imageView.setImageResource(R.drawable.block_6);
            }
        }
    }

    /**
     * 根据CO含量转换背景图片颜色
     *
     * @param imageView
     * @param primary
     */
    public static void CO_ToIcon(ImageView imageView, String primary) {
        if (primary == null || primary.equals("")) {
            primary = "获取失败";
        } else {
            float prim = Float.parseFloat(primary);
            if (prim >= 0 && prim <= 5) {
                imageView.setImageResource(R.drawable.block_1);
            } else if (prim > 6 && prim <= 10) {
                imageView.setImageResource(R.drawable.block_2);
            } else if (prim > 1 && prim <= 35) {
                imageView.setImageResource(R.drawable.block_3);
            } else if (prim > 36 && prim <= 60) {
                imageView.setImageResource(R.drawable.block_4);
            } else if (prim > 61 && prim <= 90) {
                imageView.setImageResource(R.drawable.block_5);
            } else if (prim > 91 && prim <= 150) {
                imageView.setImageResource(R.drawable.block_6);
            }
        }
    }

    /**
     * 根据O3含量转换背景图片颜色
     *
     * @param imageView
     * @param primary
     */
    public static void O3_ToIcon(ImageView imageView, String primary) {
        if (primary == null || primary.equals("")) {
            primary = "获取失败";
        } else {
            float prim = Float.parseFloat(primary);
            if (prim >= 0 && prim <= 160) {
                imageView.setImageResource(R.drawable.block_1);
            } else if (prim > 161 && prim <= 200) {
                imageView.setImageResource(R.drawable.block_2);
            } else if (prim > 201 && prim <= 300) {
                imageView.setImageResource(R.drawable.block_3);
            } else if (prim > 301 && prim <= 400) {
                imageView.setImageResource(R.drawable.block_4);
            } else if (prim > 401 && prim <= 800) {
                imageView.setImageResource(R.drawable.block_5);
            } else if (prim > 801 && prim <= 1200) {
                imageView.setImageResource(R.drawable.block_6);
            }
        }
    }

    /**
     * 根据category的提示转化空气质量圆环的背景
     *
     * @param apiInfo
     * @param textView
     */
    public static void categoryToBg(ImageView imageView, String category) {
        //优，良，轻度污染，中度污染，重度污染，严重污染
        switch (category) {
            case "优":
                imageView.setImageResource(R.drawable.bg_air_content_1);
                break;
            case "良":
                imageView.setImageResource(R.drawable.bg_air_content_2);
                break;
            case "轻度污染":
                imageView.setImageResource(R.drawable.bg_air_content_3);
                break;
            case "中度污染":
                imageView.setImageResource(R.drawable.bg_air_content_4);
                break;
            case "重度污染":
                imageView.setImageResource(R.drawable.bg_air_content_5);
                break;
            case "严重污染":
                imageView.setImageResource(R.drawable.bg_air_content_6);
                break;
            default:
                break;
        }
    }

    /**
     * 根据category的提示转化TextView的的背景
     *
     * @param textView
     */
    public static void categoryToBgIcon(TextView textView, String category) {
        //优，良，轻度污染，中度污染，重度污染，严重污染
        switch (category) {
            case "优":
                textView.setBackgroundResource(R.drawable.air_bg_1);
                break;
            case "良":
                textView.setBackgroundResource(R.drawable.air_bg_2);
                break;
            case "轻度污染":
                textView.setBackgroundResource(R.drawable.air_bg_3);
                break;
            case "中度污染":
                textView.setBackgroundResource(R.drawable.air_bg_4);
                break;
            case "重度污染":
                textView.setBackgroundResource(R.drawable.air_bg_5);
                break;
            case "严重污染":
                textView.setBackgroundResource(R.drawable.air_bg_6);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的category提示，先判断再截取
     *
     * @param category
     */
    public static String updateCategory(String category) {
        String result = null;
        switch (category) {
            case "优":
                result = category;
                break;
            case "良":
                result = category;
                break;
            case "轻度污染":
                result = category.substring(0, 2);
                break;
            case "中度污染":
                result = category.substring(0, 2);
                break;
            case "重度污染":
                result = category.substring(0, 2);
                break;
            case "严重污染":
                result = category.substring(0, 2);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 根据传入category切换圆环底部空气质量文案
     */
    public static String updateAirDescribe(String category) {
        String result = null;
        switch (category) {
            case "优":
                result = "空气质量优，基本无空气污染，各类人群可正常活动。";
                break;
            case "良":
                result = "空气质量良好，健康人群无需刻意防护，异常敏感人群减少外出。";
                break;
            case "轻度污染":
                result = "空气轻度污染，外出做好防护，易感人群减少外出。";
                break;
            case "中度污染":
                result = "今日空气中度污染，心脏病和肺病患者需避免接触，健康人群外出需做好防护。";
                break;
            case "重度污染":
                result = "今日空气重度污染，避免外出，所有人群需做好防护.";
                break;
            case "严重污染":
                result = "今日空气污染严重，所有人群应避免外出，做好防护，积极应对疾病产生。";
                break;
            default:
                result = "抬头望望天，今日适宜开心";
                break;
        }
        return result;
    }

    /**
     * 根据生活指数的Type返回相应icon
     *
     * @param imageView
     * @param Type
     */
    public static void setIcon(ImageView imageView, String Type) {
        switch (Type) {
            case "1":
                imageView.setImageResource(R.drawable.movement);
                break;
            case "9":
                imageView.setImageResource(R.drawable.catch_a_cold);
                break;
            case "7":
                imageView.setImageResource(R.drawable.allergy);
                break;
            case "10":
                imageView.setImageResource(R.drawable.air_pollution);
                break;
            default:
                break;
        }
    }

    /**
     * 根据category切换背景颜色
     *
     * @param catrgory
     */
    public static int setBgColor(String catrgory) {
        int result = 0;
        switch (catrgory) {
            case "优":
                result = R.color.air_bg_1;
                break;
            case "良":
                result = R.color.air_bg_2;
                break;
            case "轻度污染":
                result = R.color.air_bg_3;
                break;
            case "中度污染":
                result = R.color.air_bg_4;
                break;
            case "重度污染":
                result = R.color.air_bg_5;
                break;
            case "严重污染":
                result = R.color.air_bg_6;
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 白天图标代码变成夜晚图标代码（如果存在）
     * @param code
     */
    public static int iconCode2Night(int code) {
        switch (code) {
            case 100:
            case 103:
            case 104:
                return code+50;
            default:
                return code;
        }
    }

    public static boolean isSunrise(String time, Context context) {
        String rise = SPUtils.getString(Constant.SUNRISE, "", context);
        String set = SPUtils.getString(Constant.SUNSET, "", context);
        if (rise==null || set==null || rise.length()==0 || set.length()==0) return true;

        int hour = Integer.parseInt(time.substring(0, 2));
        int min = Integer.parseInt(time.substring(3, 5));
        if (hour > Integer.parseInt(rise.substring(0,2)) && hour < Integer.parseInt(set.substring(0,2))) {
            return true;
        }
        else if (hour == Integer.parseInt(rise.substring(0,2))) {
            if (min > Integer.parseInt(rise.substring(3, 5))) {
                return true;
            }
        }
        else if (hour == Integer.parseInt(set.substring(0,2))) {
            if (min < Integer.parseInt(set.substring(3,5))) {
                return true;
            }
        }
        return false;
    }
}
