package com.nelson.weather.ui;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.utils.bitmap.SaveBitmapCallBack;
import com.nelson.mvplibrary.base.BaseActivity;
import com.nelson.weather.R;

import com.nelson.weather.sharelibrary.bean.ShareEntity;
import com.nelson.weather.sharelibrary.interfaces.ShareConstant;
import com.nelson.weather.sharelibrary.util.ShareUtil;

import com.nelson.weather.utils.TransportBitmap;
import com.nelson.weather.wxapi.ShareCallBack;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zyh
 * @date 03/01/21
 * 分享界面
 */
public class ShareActivity extends BaseActivity {

    public static final String TAG = "ShareActivity";
    public static final String KEY_FILE = "file";
    public static final String SAVE_PRE_IMG = "IMG";
    @BindView(R.id.toolbar_share)
    Toolbar toolbarShare;
    @BindView(R.id.btn_share_share)
    Button btnShare;
    @BindView(R.id.iv_share)
    ImageView ivSaveImg;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    private ShareEntity testBean;
    private File savePhotoFile;
    private final int WHAT_CODE_AUTO_SWITCH = 1;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private IWXAPI api;
    private static final int THUMB_SIZE = 150;
    private Bitmap bgWithoutWater;
    private String shareClick;
    private String shareSuccess;
    private String umengKey;
    private String umengValue;
    @Override
    public int getLayoutId() {
        return R.layout.app_activity_share;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (getIntent().getStringExtra("type")!=null){
            switch (getIntent().getStringExtra("type")){
                case "Split":
                    shareClick = "Splitjoint_Share_Click";
                    shareSuccess = "Splitjoint_Share_Success";
                    break;
                default:
                    break;
            }
        }
        if(getIntent().getStringExtra("umengkey")!=null && getIntent().getStringExtra("umengvalue")!=null) {
            umengKey = getIntent().getStringExtra("umengkey");
            umengValue = getIntent().getStringExtra("umengvalue");
            Log.d(TAG, "initData: ------->"+"---"+shareClick+"---"+shareSuccess+"---" +umengKey+"----"+umengValue);
        }
        testBean = new ShareEntity("我是标题", "我是内容，描述内容。");
        bgWithoutWater = TransportBitmap.getInstance().getBitmap();
        EasyPhotos.saveBitmapToDir((Activity) this,
                Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + Environment.DIRECTORY_PICTURES + "/" + getString(R.string.app_name),
                    SAVE_PRE_IMG, bgWithoutWater, true, new
//                getSavingPath(this.getApplicationContext(),getString(R.string.app_name)), SAVE_PRE_IMG, bgWithoutWater, true, new

                        SaveBitmapCallBack() {
                            @Override
                            public void onSuccess(File file) {
                                testBean.setImgUrl(file.toString());
                                Toast.makeText(ShareActivity.this,"保存成功",Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onIOFailed(IOException exception) {
                            }

                            @Override
                            public void onCreateDirFailed() {
                            }
                        });

        testBean.setShareBigImg(true);
        if (bgWithoutWater != null ) {
            Glide.with(this).load(bgWithoutWater).into(ivSaveImg);
        }
    }


    @OnClick({R.id.btn_share_share, R.id.tv_done, R.id.iv_back})
    public void onShareClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_share_share:
                ShareUtil.showShareDialog(ShareActivity.this, testBean, ShareConstant.REQUEST_CODE);
                break;
            case R.id.tv_done:
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 分享回调处理
     *
     * @param channel
     * @param status
     */
    private void onShareCallback(int channel, int status) {
        new ShareCallBack().onShareCallback(channel, status);
    }

    /**
     * 分享大图，大图分享支持，微信，微信朋友圈，微博，QQ，其他渠道不支持
     * <p>
     * 分享大图注意点
     * 1、setShareBigImg为ture
     * 2、QQ分享大图，只能是本地图片
     */
    public void shareBigImg() {
        ShareEntity testBean = new ShareEntity("", "");
        testBean.setShareBigImg(true);
//        testBean.setImgUrl("https://www.baidu.com/img/bd_logo1.png"); // 网络地址
        testBean.setImgUrl("/storage/sdcard0/Android/data/com.xyzlf.share/files/com.xyzlf.share_share_pic.png"); //本地地址

        /** 如果你要分享的图片是Bitmap，你可以如下使用 **/
//        Bitmap bitmap = null;
//        String filePath = ShareUtil.saveBitmapToSDCard(this, bitmap);
//        testBean.setImgUrl(filePath);

        int channel = ShareConstant.SHARE_CHANNEL_WEIXIN_FRIEND | ShareConstant.SHARE_CHANNEL_WEIXIN_CIRCLE | ShareConstant.SHARE_CHANNEL_SINA_WEIBO | ShareConstant.SHARE_CHANNEL_QQ;
        ShareUtil.showShareDialog(this, channel, testBean, ShareConstant.REQUEST_CODE);
    }


}

