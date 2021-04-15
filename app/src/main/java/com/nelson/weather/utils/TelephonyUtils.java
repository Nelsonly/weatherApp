package com.nelson.weather.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.Context.TELEPHONY_SERVICE;


public class TelephonyUtils {

    public static boolean hasSimCard(Context context) {
        boolean result = true;
        switch (((TelephonyManager) context.getSystemService(TELEPHONY_SERVICE)).getSimState()) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
            default:
                break;
        }
        return result;
    }

    @SuppressLint("MissingPermission")
    @Nullable
    public static String getDeviceId(@NonNull Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readSIMCard(Context context) {
        StringBuilder result = new StringBuilder();

        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);//取得相关系统服务
        switch (tm.getSimState()) { //getSimState()取得sim的状态  有下面6中状态
            case TelephonyManager.SIM_STATE_ABSENT:
                result.append("无卡");
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result.append("未知状态");
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                result.append("需要NetworkPIN解锁");
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                result.append("需要PIN解锁");
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                result.append("需要PUK解锁");
                break;
            case TelephonyManager.SIM_STATE_READY:
                result.append("良好");
                break;
        }

        String serial = "";
        try {
            serial = tm.getSimSerialNumber();
        } catch (SecurityException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(serial)) {
            result.append("@无法取得SIM卡号");
        } else {
            result.append("@").append(serial);
        }

        if (tm.getSimOperator().equals("")) {
            result.append("@无法取得供货商代码");
        } else {
            result.append("@").append(tm.getSimOperator());
        }

        if (tm.getSimOperatorName().equals("")) {
            result.append("@无法取得供货商");
        } else {
            result.append("@").append(tm.getSimOperatorName());
        }

        if (tm.getSimCountryIso().equals("")) {
            result.append("@无法取得国籍");
        } else {
            result.append("@").append(tm.getSimCountryIso());
        }

        if (tm.getNetworkOperator().equals("")) {
            result.append("@无法取得网络运营商");
        } else {
            result.append("@").append(tm.getNetworkOperator());
        }
        if (tm.getNetworkOperatorName().equals("")) {
            result.append("@无法取得网络运营商名称");
        } else {
            result.append("@").append(tm.getNetworkOperatorName());
        }
        if (tm.getNetworkType() == 0) {
            result.append("@无法取得网络类型");
        } else {
            result.append("@").append(tm.getNetworkType());
        }
        return result.toString();
    }
}
