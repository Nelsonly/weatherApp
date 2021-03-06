package com.nelson.weather.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

/**
 * @author tianyu.meng
 */
public class HomeWatcher {

    static final String TAG = "HomeWatcher";
    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener;
    private InnerReceiver mReceiver;
    private long latestHomeClick = 0;

    public HomeWatcher(Context context) {
        mContext = context;
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mListener = listener;
        mReceiver = new InnerReceiver();
    }

    public void startWatch() {
        if (mReceiver != null) {
            mContext.registerReceiver(mReceiver, mFilter);
        }
    }

    public void stopWatch() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
    }

    class InnerReceiver extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (mListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            mListener.onHomePressed();
                        } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
//                            String a = Build.MODEL;
//                            String b = Build.MANUFACTURER;
//                            String c = Build.VERSION.SDK_INT + "";
//                            Log.i("12333",a+"|"+b+"|"+c);
                            if ("HUAWEI".equalsIgnoreCase(Build.MANUFACTURER)
                                    && Build.VERSION.SDK_INT == 29) {
                                long tempts = System.currentTimeMillis();
                                if (latestHomeClick == 0 || tempts - latestHomeClick > 800) {
                                    latestHomeClick = tempts;
                                    mListener.onHomePressed();
                                }
                            } else {
                                mListener.onRecentMenuPressed();
                            }
                        }
                    }
                }
            }
        }
    }

    public interface OnHomePressedListener {
        /**
         * ???home????????????
         */
        void onHomePressed();

        /**
         * ?????????????????????
         */
        void onRecentMenuPressed();
    }

}
