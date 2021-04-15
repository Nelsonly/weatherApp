package com.nelson.weather.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

import com.irg.lvlmonetization.MonetizeManager;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class AlarmWatcher {


    public static final String TIMED_ADS_ALARM_BROADCAST = "com.irg.cn.Application.TA_ALARM_BROADCAST";
    public static final int INTERVAL_TIME_FOR_AD_FROM_SERVICE = 4 * 60 * 60 * 1000; // 每 4 小时检查一次
    public static final int INTERVAL_TIME_FOR_CHECKPOINT = 5 * 60 * 1000; // 每 5 分钟检查一次

    public static final int ALARM_PENDING_INTENT_REQUEST_CODE = 2;

    public static final long INTERVAL_TIME_UPDATE_DAILY_AD_NUM = AlarmManager.INTERVAL_DAY;
    public static final long INTERVAL_TIME_SHOW_AD_OUT_APP = AlarmManager.INTERVAL_HOUR * 2;

    private Context mContext;
    private IntentFilter mFilter;
    private OnAlarmAlertListener mListener;
    private InnerReceiver mReceiver;

    public AlarmWatcher(Context context) {
        mContext = context;
        mFilter = new IntentFilter(TIMED_ADS_ALARM_BROADCAST);
        mReceiver = new InnerReceiver();
    }

    public void setOnAlarmAlertListener(OnAlarmAlertListener listener) {
        mListener = listener;
    }

    public void startWatch() {
        if (mReceiver != null) {
            mContext.registerReceiver(mReceiver, mFilter);
        }

        // timing的interval
        long interval = MonetizeManager.getInstance().getLevelConfig().getStrategy().getTimer().getInterval();

        // set alarm manager to run a runnable in a certain time loop
        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        Intent timedTaskIntent = new Intent(TIMED_ADS_ALARM_BROADCAST);
        PendingIntent timedTaskPendingIntent = PendingIntent.getBroadcast(mContext, ALARM_PENDING_INTENT_REQUEST_CODE, timedTaskIntent, FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(timedTaskPendingIntent);
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + interval, interval, timedTaskPendingIntent);

    }

    public void stopWatch() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
    }

    class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent == null) {
                return;
            }

            mListener.onAlarmAlert(action);

        }
    }

    public interface OnAlarmAlertListener {
        void onAlarmAlert(String action);
    }

}
