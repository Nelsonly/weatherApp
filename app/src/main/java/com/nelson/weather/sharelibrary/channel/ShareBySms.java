package com.nelson.weather.sharelibrary.channel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.nelson.weather.R;
import com.nelson.weather.sharelibrary.bean.ShareEntity;
import com.nelson.weather.sharelibrary.interfaces.OnShareListener;
import com.nelson.weather.sharelibrary.interfaces.ShareConstant;
import com.nelson.weather.sharelibrary.util.ShareUtil;
import com.nelson.weather.sharelibrary.util.ToastUtil;

/**
 * Created by zhanglifeng
 */
public class ShareBySms extends ShareBase {

    public ShareBySms(Context context) {
        super(context);
    }

    @Override
    public void share(ShareEntity data, OnShareListener listener) {
        if (data == null || TextUtils.isEmpty(data.getContent())) {
            ToastUtil.showToast(context, R.string.share_empty_tip, true);
            return;
        }
        String content;
        if (TextUtils.isEmpty(data.getContent())) {
            content = data.getTitle() + data.getUrl();
        } else {
            content = data.getContent() + data.getUrl();
        }

        Uri smsToUri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
        //sendIntent.putExtra("address", "");
        //短信内容
        sendIntent.putExtra("sms_body", content);
        sendIntent.setType("vnd.android-dir/mms-sms");
        if (ShareUtil.startActivity(context, sendIntent)) {
            if (null != listener) {
                listener.onShare(ShareConstant.SHARE_CHANNEL_SMS, ShareConstant.SHARE_STATUS_COMPLETE);
            }
        } else {
            if (null != listener) {
                listener.onShare(ShareConstant.SHARE_CHANNEL_SMS, ShareConstant.SHARE_STATUS_FAILED);
            }
        }
    }
}
