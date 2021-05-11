package com.nelson.weather.sharelibrary.channel;

import android.content.Context;

import com.nelson.weather.sharelibrary.interfaces.IShareBase;


/**
 * Created by zhanglifeng
 */
public abstract class ShareBase implements IShareBase {

    Context context;

    public ShareBase(Context context) {
        this.context = context;
    }

}
