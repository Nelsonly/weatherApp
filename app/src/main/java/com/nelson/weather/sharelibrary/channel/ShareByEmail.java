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
public class ShareByEmail extends ShareBase {

    public ShareByEmail(Context context) {
        super(context);
    }

    @Override
    public void share(ShareEntity data, OnShareListener listener) {
        if (data == null || TextUtils.isEmpty(data.getContent())) {
            ToastUtil.showToast(context, R.string.share_empty_tip, true);
            return;
        }
        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:"));
        //邮件主题
        if (!TextUtils.isEmpty(data.getTitle())) {
            email.putExtra(Intent.EXTRA_SUBJECT, data.getTitle());
        }
        //邮件内容
        String contentt = data.getContent() + data.getUrl();
        email.putExtra(Intent.EXTRA_TEXT, contentt);
        if (ShareUtil.startActivity(context, email)) {
            if (null != listener) {
                listener.onShare(ShareConstant.SHARE_CHANNEL_EMAIL, ShareConstant.SHARE_STATUS_COMPLETE);
            }
        } else {
            if (null != listener) {
                listener.onShare(ShareConstant.SHARE_CHANNEL_EMAIL, ShareConstant.SHARE_STATUS_FAILED);
            }
        }
    }
}
