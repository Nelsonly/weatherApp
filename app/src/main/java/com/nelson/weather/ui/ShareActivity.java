package com.nelson.weather.ui;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {

}
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import androidx.appcompat.widget.Toolbar;
//
//import com.bumptech.glide.Glide;
//import com.huantansheng.easyphotos.EasyPhotos;
//import com.huantansheng.easyphotos.utils.bitmap.SaveBitmapCallBack;
//import com.irigel.album.AdConstants;
//import com.irigel.album.R;
//import com.irigel.album.manager.PreloadAdManager;
//import com.irigel.album.present.IPresent;
//import com.irigel.album.sharelibrary.bean.ShareEntity;
//import com.irigel.album.sharelibrary.interfaces.ShareConstant;
//import com.irigel.album.sharelibrary.util.ShareUtil;
//import com.irigel.album.utils.TranslateBitmap;
//import com.irigel.album.utils.WaterMaskUtil;
//import com.irigel.album.wxapi.ShareCallBack;
//import com.irigel.common.utils.BitmapUtils;
//import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//
//import net.appcloudbox.AcbAds;
//import net.appcloudbox.ads.expressad.AcbExpressAdView;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
//import static com.irigel.album.utils.AppInfoUtils.logEvent;
//import static com.irigel.album.utils.AppInfoUtils.logMonetizeEventsWithDeviceInfo;
//import static com.irigel.common.utils.AppFileUtils.getSavingPath;
//
///**
// * @author zyh
// * @date 03/01/21
// * 分享界面
// */
//public class ShareActivity extends BaseActivity<IPresent> {
//
//    public static final String TAG = "ShareActivity";
//    public static final String KEY_FILE = "file";
//    public static final String SAVE_PRE_IMG = "IMG";
//    @BindView(R.id.toolbar_share)
//    Toolbar toolbarShare;
//    @BindView(R.id.btn_share_share)
//    Button btnShare;
//    @BindView(R.id.iv_share)
//    ImageView ivSaveImg;
//    @BindView(R.id.iv_back)
//    ImageView iv_back;
//    @BindView(R.id.layout_ad)
//    LinearLayout layoutAd;
//    private ShareEntity testBean;
//    private File savePhotoFile;
//    private AutoSwitchTimerTask autoSwitchTask;
//    private final int WHAT_CODE_AUTO_SWITCH = 1;
//    private AcbExpressAdView acbExpressAdView;
//    private Timer timer;
//    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
//    private IWXAPI api;
//    private static final int THUMB_SIZE = 150;
//    private Bitmap bgBitmapWithWater;
//    private Bitmap bgWithoutWater;
//    private String shareClick;
//    private String shareSuccess;
//    private String umengKey;
//    private String umengValue;
//    @Override
//    public int getLayoutId() {
//        return R.layout.app_activity_share;
//    }
//
//    @Override
//    public void initData(Bundle savedInstanceState) {
//        if (getIntent().getStringExtra("type")!=null){
//            switch (getIntent().getStringExtra("type")){
//                case "Split":
//                    shareClick = "Splitjoint_Share_Click";
//                    shareSuccess = "Splitjoint_Share_Success";
//                    break;
//                default:
//                    break;
//            }
//        }
//        if(getIntent().getStringExtra("umengkey")!=null && getIntent().getStringExtra("umengvalue")!=null) {
//            umengKey = getIntent().getStringExtra("umengkey");
//            umengValue = getIntent().getStringExtra("umengvalue");
//            Log.d(TAG, "initData: ------->"+"---"+shareClick+"---"+shareSuccess+"---" +umengKey+"----"+umengValue);
//        }
//        testBean = new ShareEntity("我是标题", "我是内容，描述内容。");
//        bgWithoutWater = TranslateBitmap.getInstance().getBitmap();
//        bgBitmapWithWater = WaterMaskUtil.createWaterMaskRightBottom(this,bgWithoutWater, BitmapUtils.zoomImg(BitmapFactory.decodeResource(getResources(),R.drawable.shuiyin),188,54),0,0);
//        EasyPhotos.saveBitmapToDir((Activity) this,
//                getSavingPath(this.getApplicationContext(),getString(R.string.app_name)), SAVE_PRE_IMG, bgBitmapWithWater, true, new
////                getSavingPath(this.getApplicationContext(),getString(R.string.app_name)), SAVE_PRE_IMG, bgWithoutWater, true, new
//
//                        SaveBitmapCallBack() {
//                            @Override
//                            public void onSuccess(File file) {
//                                testBean.setImgUrl(file.toString());
//                                Toast.makeText(com.irigel.album.activity.ShareActivity.this,"保存成功",Toast.LENGTH_LONG).show();
//
//                            }
//
//                            @Override
//                            public void onIOFailed(IOException exception) {
//                            }
//
//                            @Override
//                            public void onCreateDirFailed() {
//                            }
//                        });
//
//        testBean.setShareBigImg(true);
//    }
//
//    @Override
//    public void bindUI(View rootView) {
//        super.bindUI(rootView);
//        if (bgBitmapWithWater != null ) {
//            Glide.with(this).load(bgBitmapWithWater).into(ivSaveImg);
//            TranslateBitmap.getInstance().setBitmap(bgBitmapWithWater);
//        }
//        showExpressAd();
//    }
//
//    @OnClick({R.id.btn_share_share, R.id.tv_done, R.id.iv_back})
//    public void onShareClick(View view) {
//        int id = view.getId();
//        switch (id) {
//            case R.id.btn_share_share:
//                if(umengKey==null||umengValue==null){
//                    logMonetizeEventsWithDeviceInfo(com.irigel.album.activity.ShareActivity.this,shareClick);
//                }else {
//                    logEvent(com.irigel.album.activity.ShareActivity.this,shareClick,umengKey,umengValue);
//                }
//                ShareUtil.showShareDialog(com.irigel.album.activity.ShareActivity.this, testBean, ShareConstant.REQUEST_CODE);
//                break;
//            case R.id.tv_done:
//                finish();
//                break;
//            case R.id.iv_back:
//                finish();
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        /**
//         * 分享回调处理
//         */
//        if (requestCode == ShareConstant.REQUEST_CODE) {
//            if (data != null) {
//                int channel = data.getIntExtra(ShareConstant.EXTRA_SHARE_CHANNEL, -1);
//                int status = data.getIntExtra(ShareConstant.EXTRA_SHARE_STATUS, -1);
//                logEvent(com.irigel.album.activity.ShareActivity.this,shareSuccess,umengKey,umengValue);
////                onShareCallback(channel, status);
//            }
//        }
//    }
//
//    /**
//     * 展示express广告
//     */
//    public void showExpressAd() {
//        AcbAds.getInstance().setForegroundActivity(this);
//        acbExpressAdView = PreloadAdManager.getInstance().fetchPreloadedExpressAd(AdConstants.AdPlacement.EXPRESS_SHEEPE);
//        if (acbExpressAdView == null) {
//            Log.i(TAG, "acbExpressAdView == null");
//            acbExpressAdView = new AcbExpressAdView(getApplicationContext(), AdConstants.AdPlacement.EXPRESS_SHEEPE, AdConstants.AdPlacement.EXPRESS_SHEEPE);
//        }
//        acbExpressAdView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
//        acbExpressAdView.setExpressAdViewListener(new AcbExpressAdView.AcbExpressAdViewListener() {
//            @Override
//            public void onAdShown(AcbExpressAdView acbExpressAdView) {
//                Log.i(TAG, "onAdShown");
//            }
//
//            @Override
//            public void onAdClicked(AcbExpressAdView acbExpressAdView) {
//
//            }
//
//            @Override
//            public void onAdDislike(AcbExpressAdView acbExpressAdView) {
//                if(acbExpressAdView != null){
//                    acbExpressAdView.destroy();
//                }
//                if(timer != null){
//                    timer.cancel();
//                }
//                layoutAd.removeAllViews();
//
//            }
//        });
//
//        layoutAd.removeAllViews();
//        ViewGroup parent = (LinearLayout) acbExpressAdView.getParent();
//        if (parent != null) {
//            parent.removeAllViews();
//        }
//        layoutAd.addView(acbExpressAdView);
//        try {
//            timer = new Timer();
//            autoSwitchTask = new AutoSwitchTimerTask();
//            timer.schedule(autoSwitchTask, 0, 10000);
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (timer != null) {
//                timer.cancel();
//            }
//        }
//    }
//
//
//    /**
//     * 分享回调处理
//     *
//     * @param channel
//     * @param status
//     */
//    private void onShareCallback(int channel, int status) {
//        new ShareCallBack().onShareCallback(channel, status);
//    }
//
//    /**
//     * 分享大图，大图分享支持，微信，微信朋友圈，微博，QQ，其他渠道不支持
//     * <p>
//     * 分享大图注意点
//     * 1、setShareBigImg为ture
//     * 2、QQ分享大图，只能是本地图片
//     */
//    public void shareBigImg() {
//        ShareEntity testBean = new ShareEntity("", "");
//        testBean.setShareBigImg(true);
////        testBean.setImgUrl("https://www.baidu.com/img/bd_logo1.png"); // 网络地址
//        testBean.setImgUrl("/storage/sdcard0/Android/data/com.xyzlf.share/files/com.xyzlf.share_share_pic.png"); //本地地址
//
//        /** 如果你要分享的图片是Bitmap，你可以如下使用 **/
////        Bitmap bitmap = null;
////        String filePath = ShareUtil.saveBitmapToSDCard(this, bitmap);
////        testBean.setImgUrl(filePath);
//
//        int channel = ShareConstant.SHARE_CHANNEL_WEIXIN_FRIEND | ShareConstant.SHARE_CHANNEL_WEIXIN_CIRCLE | ShareConstant.SHARE_CHANNEL_SINA_WEIBO | ShareConstant.SHARE_CHANNEL_QQ;
//        ShareUtil.showShareDialog(this, channel, testBean, ShareConstant.REQUEST_CODE);
//    }
//
//    private Handler handler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case WHAT_CODE_AUTO_SWITCH:
//                    if (acbExpressAdView != null) {
//                        acbExpressAdView.switchAd();
//                    }
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
//
//    class AutoSwitchTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            Message msg = Message.obtain();
//            msg.what = WHAT_CODE_AUTO_SWITCH;
//            handler.sendMessage(msg);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(acbExpressAdView != null){
//            acbExpressAdView.destroy();
//        }
//        if(timer != null){
//            timer.cancel();
//        }
//    }
//
//
//
//}
//
