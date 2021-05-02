package com.nelson.weather.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.utils.bitmap.SaveBitmapCallBack;
import com.nelson.weather.R;
import com.nelson.weather.utils.Constant;
import com.nelson.weather.utils.SPUtils;
import com.nelson.weather.utils.StatusBarUtil;
import com.nelson.weather.utils.ToastUtils;
import com.nelson.mvplibrary.base.BaseActivity;
import com.nelson.mvplibrary.bean.WallPaper;
import com.google.android.material.button.MaterialButton;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 查看图片
 *
 * @author llw
 */
public class ImageActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_setting_wallpaper)
    MaterialButton btnSettingWallpaper;
    @BindView(R.id.btn_download)
    MaterialButton btnDownload;
    @BindView(R.id.vp)
    ViewPager2 vp;

    List<WallPaper> mList = new ArrayList<>();
    WallPaperAdapter mAdapter;
    String wallpaperUrl = null;

    private int position;
    private Bitmap bitmap;

    @Override
    public void initData(Bundle savedInstanceState) {
        showLoadingDialog();
        //透明状态栏
        StatusBarUtil.transparencyBar(context);
        //获取位置
        position = getIntent().getIntExtra("position", 0);
        //获取数据
        mList = LitePal.findAll(WallPaper.class);
        Log.d("list-->", "" + mList.size());
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                if ("".equals(mList.get(i).getImgUrl())) {
                    mList.remove(i);
                }
            }
        }

        mAdapter = new WallPaperAdapter(R.layout.item_image_list, mList);
        //ViewPager2实现方式
        vp.setAdapter(mAdapter);

        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Log.d("position-->", "" + position);
                wallpaperUrl = mList.get(position).getImgUrl();
                bitmap = getBitMap(wallpaperUrl);
            }
        });

        mAdapter.notifyDataSetChanged();
        vp.setCurrentItem(position, false);

        dismissLoadingDialog();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }


    @OnClick({R.id.iv_back, R.id.btn_setting_wallpaper, R.id.btn_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //设置壁纸
            case R.id.btn_setting_wallpaper:
                //放入缓存
                SPUtils.putString(Constant.WALLPAPER_URL, wallpaperUrl, context);
                //壁纸列表
                SPUtils.putInt(Constant.WALLPAPER_TYPE, 1, context);
                ToastUtils.showShortToast(context, "已设置");
                break;
            //下载壁纸
            case R.id.btn_download:
                showLoadingDialog();
                saveImageToGallery(context, bitmap);
                break;
            default:
                break;
        }
    }

    /**
     * 壁纸适配器
     */
    public class WallPaperAdapter extends BaseQuickAdapter<WallPaper, BaseViewHolder> {

        public WallPaperAdapter(int layoutResId, @Nullable List<WallPaper> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, WallPaper item) {
            ImageView imageView = helper.getView(R.id.wallpaper);
            Glide.with(mContext).load(item.getImgUrl()).into(imageView);
        }
    }

    /**
     * 保存图片到本地相册
     *
     * @param context 上下文
     * @param bitmap  bitmap
     * @return
     */
    public boolean saveImageToGallery(Context context, Bitmap bitmap) {

        // 首先保存图片
        String filePath = Environment.getExternalStorageDirectory().getAbsoluteFile()
                + "/" + Environment.DIRECTORY_PICTURES
                + "/" + getString(R.string.app_name);
        EasyPhotos.saveBitmapToDir((Activity) this,
               filePath, "IMG", bitmap, true, new
                        SaveBitmapCallBack() {
                            @Override
                            public void onSuccess(File file) {
                                ToastUtils.showShortToast(context, "图片保存成功");
                                dismissLoadingDialog();
                            }

                            @Override
                            public void onIOFailed(IOException exception) {
                                ToastUtils.showShortToast(context, "图片保存失败");
                                dismissLoadingDialog();
                            }

                            @Override
                            public void onCreateDirFailed() {
                                ToastUtils.showShortToast(context, "图片保存失败");
                                dismissLoadingDialog();
                            }
                        });
        return false;
    }

    /**
     * Url转Bitmap
     *
     * @param url
     * @return
     */
    public Bitmap getBitMap(final String url) {
        //新启一个线程进行转换
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return bitmap;

    }

}
