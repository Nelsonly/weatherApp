package com.huantansheng.easyphotos.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.huantansheng.easyphotos.R;
import com.huantansheng.easyphotos.constant.Code;
import com.huantansheng.easyphotos.models.album.AlbumModel;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.setting.Setting;
import com.huantansheng.easyphotos.ui.adapter.AlbumItemsAdapter;
import com.huantansheng.easyphotos.ui.adapter.PuzzleSelectorAdapter;
import com.huantansheng.easyphotos.ui.adapter.PuzzleSelectorPreviewAdapter;
import com.huantansheng.easyphotos.ui.widget.PressedTextView;
import com.huantansheng.easyphotos.utils.Color.ColorUtils;
import com.huantansheng.easyphotos.utils.permission.PermissionUtil;
import com.huantansheng.easyphotos.utils.system.SystemUtils;

import java.util.ArrayList;

public class PuzzleSelectorActivitySecond extends AppCompatActivity implements View.OnClickListener, AlbumItemsAdapter.OnClickListener, PuzzleSelectorAdapter.OnClickListener, PuzzleSelectorPreviewAdapter.OnClickListener {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, PuzzleSelectorActivitySecond.class);
        activity.startActivity(intent);
        listener = (OnSelectPhotosForPuzzleDoneListener) activity;
    }

    private static OnSelectPhotosForPuzzleDoneListener listener;

    private AlbumModel albumModel;

    private AnimatorSet setShow;
    private AnimatorSet setHide;

    private RelativeLayout rootViewAlbumItems, rootSelectorView;
    private RecyclerView rvAlbumItems;
    private AlbumItemsAdapter albumItemsAdapter;
    private PressedTextView tvAlbumItems;

    private ArrayList<Photo> photoList = new ArrayList<>();
    private PuzzleSelectorAdapter photosAdapter;
    private RecyclerView rvPhotos, rvPreview;
    private PuzzleSelectorPreviewAdapter previewAdapter;
    private ArrayList<Photo> selectedPhotos = new ArrayList<>();

    private PressedTextView tvDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_selector_easy_photos);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int statusColor = getWindow().getStatusBarColor();
            if (statusColor == Color.TRANSPARENT) {
                statusColor = ContextCompat.getColor(this, R.color.easy_photos_status_bar);
            }
            if (ColorUtils.isWhiteColor(statusColor)) {
                SystemUtils.getInstance().setStatusDark(this, true);
            }
        }

        if (PermissionUtil.checkAndRequestPermissionsInActivity(this, getNeedPermissions())) {
            hasPermissions();
        }

    }

    protected String[] getNeedPermissions() {
        if (Setting.isShowCamera) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                return new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
            }
            return new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
            }
            return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionUtil.onPermissionResult(this, permissions, grantResults,
                new PermissionUtil.PermissionCallBack() {
                    @Override
                    public void onSuccess() {
                        hasPermissions();
                    }

                    @Override
                    public void onShouldShow() {
//                        tvPermission.setText(R.string.permissions_again_easy_photos);
//                        permissionView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (PermissionUtil.checkAndRequestPermissionsInActivity(EasyPhotosActivity.this, getNeedPermissions())) {
//                                    hasPermissions();
//                                }
//                            }
//                        });

                    }

                    @Override
                    public void onFailed() {
//                        tvPermission.setText(R.string.permissions_die_easy_photos);
//                        permissionView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                SettingsUtils.startMyApplicationDetailsForResult(EasyPhotosActivity.this,
//                                        getPackageName());
//                            }
//                        });

                    }
                });
    }

    private void hasPermissions() {

        albumModel = AlbumModel.getInstance();
        albumModel.query(getApplicationContext(), new AlbumModel.CallBack() {
            @Override
            public void onAlbumWorkedCallBack() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                    }
                });
            }
        });
    }

    private void initView() {

        if (albumModel.getAlbumItems().isEmpty()) {
            Toast.makeText(this, R.string.no_photos_easy_photos, Toast.LENGTH_LONG).show();
            return;
        }

        setClick(R.id.iv_back);
        tvAlbumItems = (PressedTextView) findViewById(R.id.tv_album_items);
        tvAlbumItems.setText(albumModel.getAlbumItems().get(0).name);
        rootSelectorView = (RelativeLayout) findViewById(R.id.m_selector_root);
        tvDone = (PressedTextView) findViewById(R.id.tv_done);
        tvDone.setOnClickListener(this);
        tvAlbumItems.setOnClickListener(this);
        initAlbumItems();
        initPhotos();
        initPreview();
    }

    private void initPreview() {
        rvPreview = (RecyclerView) findViewById(R.id.rv_preview_selected_photos);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        previewAdapter = new PuzzleSelectorPreviewAdapter(this, selectedPhotos, this);
        rvPreview.setLayoutManager(lm);
        rvPreview.setAdapter(previewAdapter);
    }

    private void initPhotos() {
        rvPhotos = (RecyclerView) findViewById(R.id.rv_photos);
        ((SimpleItemAnimator) rvPhotos.getItemAnimator()).setSupportsChangeAnimations(false);//去除item更新的闪光

        photoList.addAll(albumModel.getCurrAlbumItemPhotos(0));
        photosAdapter = new PuzzleSelectorAdapter(this, photoList, this);

        int columns = getResources().getInteger(R.integer.photos_columns_easy_photos);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columns);
        rvPhotos.setLayoutManager(gridLayoutManager);
        rvPhotos.setAdapter(photosAdapter);
    }

    private void initAlbumItems() {
        rootViewAlbumItems = (RelativeLayout) findViewById(R.id.root_view_album_items);
        rootViewAlbumItems.setOnClickListener(this);
        setClick(R.id.iv_album_items);
        rvAlbumItems = (RecyclerView) findViewById(R.id.rv_album_items);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        ArrayList<Object> list = new ArrayList<Object>(albumModel.getAlbumItems());
        albumItemsAdapter = new AlbumItemsAdapter(this, list, 0, this);
        rvAlbumItems.setLayoutManager(lm);
        rvAlbumItems.setAdapter(albumItemsAdapter);
    }

    private void setClick(@IdRes int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.iv_back == id) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (R.id.tv_album_items == id || R.id.iv_album_items == id) {
            showAlbumItems(View.GONE == rootViewAlbumItems.getVisibility());
        } else if (R.id.root_view_album_items == id) {
            showAlbumItems(false);
        } else if (R.id.tv_done == id) {
            listener.onSelectPhotosForPuzzleDoneListener(selectedPhotos);
            finish();
//            PuzzleActivity.startWithPhotos(this, selectedPhotos, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name), "IMG", Code.REQUEST_PUZZLE, false, Setting.imageEngine);
        }
    }

    private void showAlbumItems(boolean isShow) {
        if (null == setShow) {
            newAnimators();
        }
        if (isShow) {
            rootViewAlbumItems.setVisibility(View.VISIBLE);
            setShow.start();
        } else {
            setHide.start();
        }
    }

    private void newAnimators() {
        newHideAnim();
        newShowAnim();
    }

    private void newShowAnim() {
        ObjectAnimator translationShow = ObjectAnimator.ofFloat(rvAlbumItems, "translationY", rootSelectorView.getTop(), 0);
        ObjectAnimator alphaShow = ObjectAnimator.ofFloat(rootViewAlbumItems, "alpha", 0.0f, 1.0f);
        translationShow.setDuration(300);
        setShow = new AnimatorSet();
        setShow.setInterpolator(new AccelerateDecelerateInterpolator());
        setShow.play(translationShow).with(alphaShow);
    }

    private void newHideAnim() {
        ObjectAnimator translationHide = ObjectAnimator.ofFloat(rvAlbumItems, "translationY", 0, rootSelectorView.getTop());
        ObjectAnimator alphaHide = ObjectAnimator.ofFloat(rootViewAlbumItems, "alpha", 1.0f, 0.0f);
        translationHide.setDuration(200);
        setHide = new AnimatorSet();
        setHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rootViewAlbumItems.setVisibility(View.GONE);
            }
        });
        setHide.setInterpolator(new AccelerateInterpolator());
        setHide.play(translationHide).with(alphaHide);
    }

    @Override
    public void onAlbumItemClick(int position, int realPosition) {
        updatePhotos(realPosition);
        showAlbumItems(false);
        tvAlbumItems.setText(albumModel.getAlbumItems().get(realPosition).name);
    }

    private void updatePhotos(int currAlbumItemIndex) {
        photoList.clear();
        photoList.addAll(albumModel.getCurrAlbumItemPhotos(currAlbumItemIndex));

        photosAdapter.notifyDataSetChanged();
        rvPhotos.scrollToPosition(0);
    }


    @Override
    public void onBackPressed() {

        if (null != rootViewAlbumItems && rootViewAlbumItems.getVisibility() == View.VISIBLE) {
            showAlbumItems(false);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onPhotoClick(int position) {
        if (selectedPhotos.size() > 8) {
            Toast.makeText(this, getString(R.string.selector_reach_max_image_hint_easy_photos, 9), Toast.LENGTH_SHORT).show();
            return;
        }

        selectedPhotos.add(photoList.get(position));
        previewAdapter.notifyDataSetChanged();
        rvPreview.smoothScrollToPosition(selectedPhotos.size() - 1);

        tvDone.setText(getString(R.string.selector_action_done_easy_photos, selectedPhotos.size(), 9));
        if (selectedPhotos.size() > 1) {
            tvDone.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteClick(int position) {
        selectedPhotos.remove(position);
        previewAdapter.notifyDataSetChanged();
        tvDone.setText(getString(R.string.selector_action_done_easy_photos, selectedPhotos.size(), 9));
        if (selectedPhotos.size() < 2) {
            tvDone.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Code.REQUEST_PUZZLE) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

    public interface OnSelectPhotosForPuzzleDoneListener{
        void onSelectPhotosForPuzzleDoneListener(ArrayList<Photo> selectedPhotos);
    }

}
