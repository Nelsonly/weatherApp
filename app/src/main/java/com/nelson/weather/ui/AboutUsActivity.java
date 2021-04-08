package com.nelson.weather.ui;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.nelson.weather.R;
import com.nelson.weather.utils.APKVersionInfoUtils;
import com.nelson.weather.utils.StatusBarUtil;
import com.nelson.weather.utils.ToastUtils;
import com.nelson.mvplibrary.base.BaseActivity;
import com.nelson.mvplibrary.bean.AppVersion;
import com.nelson.mvplibrary.utils.SizeUtils;
import com.nelson.mvplibrary.view.dialog.AlertDialog;

import org.litepal.LitePal;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于 Good Weather
 *
 * @author llw
 */
public class AboutUsActivity extends BaseActivity {

    private static final int INSTALL_APK_CODE = 3472;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lay_app_version)
    LinearLayout layAppVersion;
    @BindView(R.id.tv_version)
    TextView tvVersion;//版本
    @BindView(R.id.tv_blog)
    TextView tvBlog;//博客
    @BindView(R.id.tv_code)
    TextView tvCode;//源码
    @BindView(R.id.tv_copy_email)
    TextView tvCopyEmail;//复制邮箱
    @BindView(R.id.tv_author)
    TextView tvAuthor;//作者
    @BindView(R.id.v_red)
    View vRed;//红点
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private String updateUrl = null;
    private String updateLog = null;
    private boolean is_update = false;
    private AppVersion appVersion;
    /**
     * 博客地址
     */
    private String CSDN_BLOG_URL = "https://blog.csdn.net/qq_38436214/category_9880722.html";
    /**
     * 源码地址
     */
    private String GITHUB_URL = "https://github.com/lilongweidev/GoodWeather";


    @Override
    public void initData(Bundle savedInstanceState) {
        Back(toolbar);
        //蓝色状态栏
        StatusBarUtil.setStatusBarColor(context, R.color.about_bg_color);
        //设置文字下划线
        tvCopyEmail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //抗锯齿
        tvCopyEmail.getPaint().setAntiAlias(true);

        tvVersion.setText(APKVersionInfoUtils.getVerName(context));

        appVersion = LitePal.find(AppVersion.class, 1);
        updateLog = appVersion.getChangelog();

        //提示更新
        if (!appVersion.getVersionShort().equals(APKVersionInfoUtils.getVerName(context))) {
            is_update = true;
            //显示红点
            vRed.setVisibility(View.VISIBLE);
            updateUrl = appVersion.getInstall_url();
            updateLog = appVersion.getChangelog();
        } else {
            //隐藏红点
            vRed.setVisibility(View.GONE);
            is_update = false;
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }


    @OnClick({R.id.lay_app_version, R.id.tv_blog, R.id.tv_code, R.id.tv_copy_email, R.id.tv_author})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //版本更新
            case R.id.lay_app_version:
                if (is_update) {
                    showUpdateAppDialog(updateUrl, updateLog);
                } else {
                    ToastUtils.showShortToast(context, "当前已是最新版本");
                }
                break;
            //博客地址
            case R.id.tv_blog:
                jumpUrl(CSDN_BLOG_URL);
                break;
            //源码地址
            case R.id.tv_code:
                jumpUrl(GITHUB_URL);
                break;
            //复制邮箱
            case R.id.tv_copy_email:
                myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", "lonelyholiday@qq.com");
                myClipboard.setPrimaryClip(myClip);
                ToastUtils.showShortToast(context, "邮箱已复制");
                break;
            case R.id.tv_author:
                ToastUtils.showShortToast(context, "你为啥要点我呢？");
                break;
            default:
                ToastUtils.showShortToast(context, "点你咋的！");
                break;
        }
    }

    /**
     * 跳转URL
     *
     * @param url 地址
     */
    private void jumpUrl(String url) {
        if (url != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } else {
            ToastUtils.showShortToast(context, "未找到相关地址");
        }
    }

    AlertDialog updateAppDialog = null;

    /**
     * 更新弹窗
     *
     * @param downloadUrl 下载地址
     * @param updateLog   更新内容
     */
    private void showUpdateAppDialog(String downloadUrl, String updateLog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .addDefaultAnimation()//默认弹窗动画
                .setCancelable(true)
                .setText(R.id.tv_update_info, updateLog)
                .setContentView(R.layout.dialog_update_app_tip)//载入布局文件
                .setWidthAndHeight(SizeUtils.dp2px(context, 270), ViewGroup.LayoutParams.WRAP_CONTENT)//设置弹窗宽高
                .setOnClickListener(R.id.tv_cancel, v -> {//取消
                    updateAppDialog.dismiss();
                }).setOnClickListener(R.id.tv_fast_update, v -> {//立即更新
                    ToastUtils.showShortToast(context, "正在后台下载，下载后会自动安装");
                    downloadApk(downloadUrl);
                    updateAppDialog.dismiss();
                });
        updateAppDialog = builder.create();
        updateAppDialog.show();
    }

    /**
     * 清除APK
     *
     * @param apkName
     * @return
     */
    public static File clearApk(String apkName) {
        File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkName);
        if (apkFile.exists()) {
            apkFile.delete();
        }
        return apkFile;
    }

    /**
     * 下载APK
     *
     * @param downloadUrl
     */
    private void downloadApk(String downloadUrl) {
        clearApk("GoodWeather.apk");
        //下载管理器 获取系统下载服务
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Request request = new Request(Uri.parse(downloadUrl));
        //设置运行使用的网络类型，移动网络或者Wifi都可以
        request.setAllowedNetworkTypes(request.NETWORK_MOBILE | request.NETWORK_WIFI);
        //设置是否允许漫游
        request.setAllowedOverRoaming(true);
        //设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadUrl));
        request.setMimeType(mimeString);
        //设置下载时或者下载完成时，通知栏是否显示
        request.setNotificationVisibility(request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("下载新版本");
        request.setVisibleInDownloadsUi(true);//下载UI
        //sdcard目录下的download文件夹
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "GoodWeather.apk");
        //将下载请求放入队列
        downloadManager.enqueue(request);
    }

}
