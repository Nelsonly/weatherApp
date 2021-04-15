package com.nelson.weather.utils;

import android.graphics.Point;

/**
 * 统一管理缓存中对应的KEY
 *
 * @author llw
 */
public class Constant {

    /**
     * 和风天气KEY
     */
    public static final String API_KEY = "d4a619bfe3244190bfa84bb468c14316";
//    public static final String API_KEY = "94d5a3a2eeb746cd990246a8cb9598a8";
    /**
     * 更新APP的用户Id
     */
    public static final String UPDATE_USER_ID = "5e8c37e90d81cc0db2645c1c";
    /**
     * 更新APP的用户Token
     */
    public static final String UPDATE_API_TOKEN = "468e4653ca9e1d34e7a73b8f2d7191da";
    /**
     * 返回成功码
     */
    public static final String SUCCESS_CODE = "200";
    /**
     * 市
     */
    public static final String CITY = "city";
    /**
     * 区/县
     */
    public static final String DISTRICT = "district";
    /**
     * 通过搜索接口得到的城市id,在V7中所有数据通过id来查询
     */
    public static final String LOCATION_ID = "locationId";
    /**
     * 每日图片开关
     */
    public static final String EVERYDAY_IMG = "everyday_img";
    /**
     * 图片列表开关
     */
    public static final String IMG_LIST = "img_list";
    /**
     * 手动定义开关
     */
    public static final String CUSTOM_IMG = "custom_img";
    /**
     * 选中的本地背景图片
     */
    public static final String IMG_POSITION = "img_position";
    /**
     * 启动相册标识
     */
    public final static int SELECT_PHOTO = 2;
    /**
     * 手动上传图片地址
     */
    public static final String CUSTOM_IMG_PATH = "custom_img_path";
    /**
     * 跳转页面的标识
     */
    public static final String FLAG_OTHER_RETURN = "flag_other_return";

    public static final String LOCATION = "location";
    /**
     * App首次启动
     */
    public static final String APP_FIRST_START = "appFirstStart";
    /**
     * 今日启动APP的时间
     */
    public static final String START_UP_APP_TIME = "startAppTime";

    /**
     * 成功
     */
    public static final String SUCCESS = "success";

    /**
     * 壁纸地址
     */
    public static final String WALLPAPER_URL = "wallpaperUrl";
    /**
     * 壁纸类型  1  壁纸列表  2  每日一图  3  手动上传  4  默认壁纸
     */
    public static final String WALLPAPER_TYPE = "wallpaperType";

    public static final String BiYing_Date = "BiYingDate";

    public static final String SUNRISE = "sunrise";

    public static final String SUNSET = "sunset";

    public static final String refreshTime = "RefreshTime";

    public static Point point;//获取屏幕的大小

    public static String isindexfragment = "isindexfragment";

    public static String allLocation = "AllLocation";

    public static final String UMENG_APP_KEY = "5f991f8b45b2b751a91e2895";

    public static final String PRIVACY_ARGREE = "privacy_argree";
    public static final String PRIVACY_REFUSE = "privacy_refuse";
    public static final String APP_START = "app_start";
    public static final String APP_QUIT = "app_quit";
    public static final String APP_HOMETAB_CLICK = "app_hometab_click";
    public static final String APP_7DAYSTAB_CLICK = "app_7daystab_click";
    public static final String APP_AIRCONDTIONTAB_CLICK = "app_aircondtiontab_click";
    public static final String APP_GOBACK = "app_goback";
    public static final String HOME_LOCATIONMAP = "home_locationmap";
    public static final String HOME_MENU = "home_menu";
    public static final String HOME_AIRQUALITY = "home_airquality";
    public static final String HOME_24HOURS_SLIP = "home_24hours_slip";
    public static final String HOME_24HOURS_CLICK = "home_24hours_click";
    public static final String HOME_15DAYS_SLIP = "home_15days_slip";
    public static final String HOME_15DAYS_CLICK = "home_15days_click";
    public static final String HOME_LIVING_SLIP = "home_living_slip";
    public static final String HOME_LIVINGSUNPROOF_CLICK = "home_livingsunproof_click";
    public static final String HOME_LIVINGCLOTHES_CLICK = "home_livingclothes_click";
    public static final String HOME_LIVINGCOLD_CLICK = "home_livingcold_click";
    public static final String HOME_LIVINGCLEANCAR_CLICK = "home_livingcleancar_click";
    public static final String HOME_LIVINGCOMFORT_CLICK = "home_livingcomfort_click";
    public static final String HOME_LIVINGMAKEUP_CLICK = "home_livingmakeup_click";
    public static final String HOME_LIVINGTRAVEL_CLICK = "home_livingtravel_click";
    public static final String HOME_LIVINGFISHING_CLICK = "home_livingfishing_click";
    public static final String HOME_LIVINGAIRCON_CLICK = "home_livingaircon_click";
    public static final String HOME_LIVINGSUNGLASS_CLICK = "home_livingsunglass_click";
    public static final String HOME_LIVINGAIRING_CLICK = "home_livingairing_click";
    public static final String HOME_LIVINGAIRPOLLUTION_CLICK = "home_livingAirpollution_click";
    public static final String HOME_LIVINGALLERGY_CLICK = "home_livingallergy_click";
    public static final String HOME_LIVINGRAYS_CLICK = "home_livingrays_click";
    public static final String HOME_LIVINGSPORT_CLICK = "home_livingsport_click";
    public static final String HOME_LIVINGTRAFFIC_CLICK = "home_livingtraffic_click";
    public static final String DAYS_15DAYS_SLIP = "15days_15days_slip";
    public static final String DAYS_AIRQUALITY_CLICK = "15days_airquality_click";
    public static final String AIRQ_SUGGESTALLERGY_CLICK = "airq_suggestallergy_click";
    public static final String AIRQ_SUGGESTAIRPOLLUTION_CLICK = "airq_suggestAirpollution_click";
    public static final String AIRQ_SUGGESPORT_CLICK = "airq_suggesport_click";
    public static final String AIRQ_SUGGESTCOLD_CLICK = "airq_suggestcold_click";

    public static final String SPLASHAD_WILLSHOW = "splashAd_willshow";
    public static final String SPLASHAD_DIDSHOW = "splashAd_didshow";
    public static final String SPLASHAD_CLICK = "splashAd_click";
    public static final String SPLASHAD_SKIP = "splashAd_skip";
    public static final String LOGIN_POPAD_WILLSHOW = "login_popAd_willshow";
    public static final String LOGIN_POPAD_DIDSHOW = "login_popAd_didshow";
    public static final String LOGIN_POPAD_CLOSE = "login_popAd_close";
    public static final String LOGIN_POPAD_CLICK = "login_popAdd_click";
    public static final String HOME_LBANNERAD_WILLSHOW = "home_LbannerAd_willshow";
    public static final String HOME_LBANNERAD_DIDSHOW = "home_LbannerAd_didshow";
    public static final String HOME_LBANNERAD_CLOSE = "home_LbannerAd_close";
    public static final String HOME_LBANNERAD_CLICK = "home_LbannerAd_click";
    public static final String HOME_INFOFLOW1AD_WILLSHOW = "home_infoflow1Ad_willshow";
    public static final String HOME_INFOFLOW1AD_DIDSHOW = "home_infoflow1Ad_didshow";
    public static final String HOME_INFOFLOW1AD_CLOSE = "home_infoflow1Ad_close";
    public static final String HOME_INFOFLOW1AD_CLICK = "home_infoflow1Ad_click";
    public static final String HOME_15DAYSAD_WILLSHOW = "home_15daysAd_willshow";
    public static final String HOME_15DAYSAD_DIDSHOW = "home_15daysAd_didshow";
    public static final String HOME_15DAYSAD_CLOSE = "home_15daysAd_close";
    public static final String HOME_15DAYSAD_CLICK = "home_15daysAd_click";
    public static final String HOME_LIVINGRTOPAD_WILLSHOW = "home_livingRtopAd_willshow";
    public static final String HOME_LIVINGRTOPAD_DIDSHOW = "home_livingRtopAd_didshow";
    public static final String HOME_LIVINGRTOPAD_CLOSE = "home_livingRtopAd_close";
    public static final String HOME_LIVINGRTOPAD_CLICK = "home_livingRtopAd_click";
    public static final String HOME_INFOFLOW2AD_WILLSHOW = "home_infoflow2Ad_willshow";
    public static final String HOME_INFOFLOW2AD_DIDSHOW = "home_infoflow2Ad_didshow";
    public static final String HOME_INFOFLOW2AD_CLOSE = "home_infoflow2Ad_close";
    public static final String HOME_INFOFLOW2AD_CLICK = "home_infoflow2Ad_click";
    public static final String DAYS_LBANNERAD_WILLSHOW = "15days_LbannerAd_willshow";
    public static final String DAYS_LBANNERAD_DIDSHOW = "15days_LbannerAd_didshow";
    public static final String DAYS_LBANNERAD_CLOSE = "15days_LbannerAd_close";
    public static final String DAYS_LBANNERAD_CLICK = "15days_LbannerAd_click";
    public static final String AIRQ_LBANNERAD_WILLSHOW = "airq_LbannerAd_willshow";
    public static final String AIRQ_LBANNERAD_DIDSHOW = "airq_LbannerAd_didshow";
    public static final String AIRQ_LBANNERAD_CLOSE = "airq_LbannerAd_close";
    public static final String AIRQ_LBANNERAD_CLICK = "airq_LbannerAd_click";
    public static final String DAYS_BOARDAD_WILLSHOW = "15days_boardAd_willshow";
    public static final String DAYS_BOARDAD_DIDSHOW = "15days_boardAd_didshow";
    public static final String DAYS_BOARDAD_CLOSE = "15days_boardAd_close";
    public static final String DAYS_BOARDAD_CLICK = "15days_boardAd_click";
    public static final String DAYS_INFOFLOWAD_WILLSHOW = "15days_infoflowAd_willshow";
    public static final String DAYS_INFOFLOWAD_DIDSHOW = "15days_infoflowAd_didshow";
    public static final String DAYS_INFOFLOWAD_CLOSE = "15days_infoflowAd_close";
    public static final String DAYS_INFOFLOWAD_CLICK = "15days_infoflowAd_click";
    public static final String AIRQ_INFOFLOW1AD_WILLSHOW = "airq_infoflow1Ad_willshow";
    public static final String AIRQ_INFOFLOW1AD_DIDSHOW = "airq_infoflow1Ad_didshow";
    public static final String AIRQ_INFOFLOW1AD_CLOSE = "airq_infoflow1Ad_close";
    public static final String AIRQ_INFOFLOW1AD_CLICK = "airq_infoflow1Ad_click";
    public static final String AIRQ_INFOFLOW2AD_WILLSHOW = "airq_infoflow2Ad_willshow";
    public static final String AIRQ_INFOFLOW2AD_DIDSHOW = "airq_infoflow2Ad_didshow";
    public static final String AIRQ_INFOFLOW2AD_CLOSE = "airq_infoflow2Ad_close";
    public static final String AIRQ_INFOFLOW2AD_CLICK = "airq_infoflow2Ad_click";
    public static final String AIRQ_5DAYSAD_WILLSHOW = "airq_5daysAd_willshow";
    public static final String AIRQ_5DAYSAD_DIDSHOW = "airq_5daysAd_didshow";
    public static final String AIRQ_5DAYSAD_CLOSE = "airq_5daysAd_close";
    public static final String AIRQ_5DAYSAD_CLICK = "airq_5daysAd_click";
    public static final String APP_QUITAD_WILLSHOW = "app_quitAd_willshow";
    public static final String APP_QUITAD_DIDSHOW = "app_quitAd_didshow";
    public static final String APP_QUITAD_CLOSE = "app_quitAd_close";
    public static final String APP_QUITAD_CLICK = "app_quitAd_click";

}
