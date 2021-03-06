package com.nelson.weather.sharelibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.nelson.weather.sharelibrary.interfaces.ShareConstant;


/**
 * @author from github , modify by zyh
 */
public abstract class ShareBaseActivity extends FragmentActivity {

    protected int channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == getIntent()) {
            finish();
            return;
        }
        initChannel();
    }

    protected void initChannel() {
        try {
            //Fuzz问题处理
            channel = getIntent().getIntExtra(ShareConstant.EXTRA_SHARE_CHANNEL, ShareConstant.SHARE_CHANNEL_ALL);
        } catch (Exception e) {}
    }

    /**
     * @param channel {@link ShareConstant#SHARE_CHANNEL_ALL}
     * @param status {@link ShareConstant#SHARE_STATUS_CANCEL}
     */
    public void finishWithResult(final int channel, final int status) {
        Intent intent = new Intent();
        intent.putExtra(ShareConstant.EXTRA_SHARE_CHANNEL, channel);
        intent.putExtra(ShareConstant.EXTRA_SHARE_STATUS, status);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
