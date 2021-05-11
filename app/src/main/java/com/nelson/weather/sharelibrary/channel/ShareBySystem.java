package com.nelson.weather.sharelibrary.channel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.nelson.weather.R;
import com.nelson.weather.sharelibrary.bean.ShareEntity;
import com.nelson.weather.sharelibrary.interfaces.OnShareListener;
import com.nelson.weather.sharelibrary.interfaces.ShareConstant;
import com.nelson.weather.sharelibrary.util.ShareUtil;
import com.nelson.weather.sharelibrary.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by zhanglifeng
 */
public class ShareBySystem extends ShareBase {

    public ShareBySystem(Context context) {
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

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        if(data.getImgUrl()!=null){
            Uri uri;
            File  file = new File(data.getImgUrl());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                try {
                    Log.d("zhangyuhong", "share: 8282828282882828282828282828282882");
                    uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(), file.getName(), null));
                } catch (FileNotFoundException e) {
                    uri = Uri.fromFile(file);
                    e.printStackTrace();
                }
            }else{
                uri = Uri.fromFile(file);

            }
            //uri 是图片的地址
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");
        }else{
            shareIntent.putExtra(Intent.EXTRA_TEXT, content);
            shareIntent.setType("text/plain");
        }

        if(ShareUtil.startActivity(context, Intent.createChooser(
                shareIntent, context.getString(R.string.share_to)))) {
            if (null != listener) {
                listener.onShare(ShareConstant.SHARE_CHANNEL_SYSTEM, ShareConstant.SHARE_STATUS_COMPLETE);
            }
        } else {
            if (null != listener) {
                listener.onShare(ShareConstant.SHARE_CHANNEL_SYSTEM, ShareConstant.SHARE_STATUS_FAILED);
            }
        }
    }
}
