package com.nelson.weather.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.TypedValue;

import com.nelson.weather.BuildConfig;
import com.nelson.weather.R;

import com.irg.lvlmonetization.MonetizeManager;
import com.irg.lvlmonetization.utils.http.model.Level;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhixiangxiao on 2018/1/24.
 */

public class AppInfoUtils {

    public static final String TAG = "logMonetizeEvents";

    private static final String PREF_FILE_NAME = "optimizer_app_info_utils";

    private static final String PREF_KEY_UUID = "PREF_KEY_UUID";

    public static final String PREF_KEY_IS_FIRST_ENTER = "PREF_KEY_IS_FIRST_ENTER";
    public static final String LEVEL_3 = "3";
    public static final String LEVEL_2 = "2";
    public static final String LEVEL_1 = "1";
    public static final String LEVEL_DEFAULT = "0";


    /**
     * 更改activity当前所在回退栈应用icon和名称
     */
    public static void changeTaskDescription(Activity activity) {
        if (activity == null) {
            return;
        }
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = activity.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.recent_app_icon);
        ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(activity.getString(R.string.recent_app_name), bm, color);

        activity.setTaskDescription(td);
        bm.recycle();
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    /**
     * 手机系统版本
     */
    public static String getSdkVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 存储第一次安装时间（精确到天 格式20201228）
     */
    public static void setFirstOpenTime(Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year + String.format("%02d", month) + String.format("%02d", day);
    }
    

    /**
     * 获取当前用户已开启最高level（1 2 3）缺省值：0
     */
    public static String getHighestMonetizeLevel() {
        Level level = MonetizeManager.getInstance().getLevelConfig();
        if (level.getLevel3()) {
            return LEVEL_3;
        } else if (level.getLevel2()) {
            return LEVEL_2;
        } else if (level.getLevel1()) {
            return LEVEL_1;
        } else {
            return LEVEL_DEFAULT;
        }
    }


    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getMobileModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getMobileBrand() {
        String brand = android.os.Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            return brand.toUpperCase();
        }
        else{
            return "null";
        }
    }
}