package com.nelson.weather.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.constant.Key;
import com.huantansheng.easyphotos.engine.ImageEngine;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.models.puzzle.NumberSeekBar;
import com.huantansheng.easyphotos.models.puzzle.PuzzlePiece;
import com.huantansheng.easyphotos.models.puzzle.PuzzleUtils;
import com.huantansheng.easyphotos.models.puzzle.PuzzleView;
import com.huantansheng.easyphotos.models.sticker.StickerModel;
import com.huantansheng.easyphotos.setting.Setting;
import com.huantansheng.easyphotos.ui.adapter.PuzzleAdapter;
import com.huantansheng.easyphotos.ui.adapter.PuzzleEditBtnAdapter;
import com.nelson.mvplibrary.base.BaseActivity;
import com.nelson.weather.R;
import com.nelson.weather.utils.TransportBitmap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zyh
 * 通过自定义startActivity启动，传入照片集合。
 *
 */
public class PuzzleImgActivity extends BaseActivity implements View.OnClickListener,
        PuzzleAdapter.OnItemClickListener,PuzzleEditBtnAdapter.OnBtnItemClickListener{

    private static final String TAG = "zhangyuhong";
    private static WeakReference<Class<? extends Activity>> toClass;

    /**
     *
     * @param act 活动
     * @param photos    选择的照片的url
     * @param puzzleSaveDirPath 存储图片的位置
     * @param puzzleSaveNamePrefix  存储图片的名字
     * @param requestCode   Activity Result code
     * @param replaceCustom 是否替换Custom（一般为false）
     * @param imageEngine   图片加载引擎
     */
    public static void startWithPhotos(Activity act, ArrayList<Photo> photos,
                                       String puzzleSaveDirPath, String puzzleSaveNamePrefix,
                                       int requestCode, boolean replaceCustom,
                                       @NonNull ImageEngine imageEngine) {
        if (null != toClass) {
            toClass.clear();
            toClass = null;
        }
        Setting.fileProviderAuthority = act.getPackageName()+".savephoto.fileprovider";
        if (Setting.imageEngine != imageEngine) {
            Setting.imageEngine = imageEngine;
        }
        Log.d(TAG, "startWithPhotos: "+photos.size());
        Intent intent = new Intent(act, PuzzleImgActivity.class);
        intent.putExtra(Key.PUZZLE_FILE_IS_PHOTO, true);
        intent.putParcelableArrayListExtra(Key.PUZZLE_FILES, photos);
        intent.putExtra(Key.PUZZLE_SAVE_DIR, puzzleSaveDirPath);
        intent.putExtra(Key.PUZZLE_SAVE_NAME_PREFIX, puzzleSaveNamePrefix);
        if (replaceCustom) {
            toClass = new WeakReference<Class<? extends Activity>>(act.getClass());
        }
        act.startActivityForResult(intent, requestCode);
    }
    ArrayList<Photo> photos = null;

    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    String saveDirPath, saveNamePrefix;

    private PuzzleView puzzleView;
    private RecyclerView rvPuzzleTemplet;
    private PuzzleAdapter puzzleAdapter;
    private PuzzleEditBtnAdapter puzzlebtnAdapter;
    private NumberSeekBar numberSeekBar;
    private int fileCount = 0;

    private LinearLayout llMenu;
    private ArrayList<ImageView> ivMenus = new ArrayList<>();

    private ArrayList<Integer> degrees = new ArrayList<>();
    private int degreeIndex = -1;
    private int controlFlag;
    private static final int FLAG_CONTROL_PADDING = 0;
    private static final int FLAG_CONTROL_CORNER = 1;
    private static final int FLAG_CONTROL_ROTATE = 2;

    private int deviceWidth = 0;
    private int deviceHeight = 0;

    private TextView tvTemplate, tvTextSticker;
    private RelativeLayout mRootView, mBottomLayout;
    private ImageView ivReplace;
    private ImageView ivRotate;
    private ImageView ivMirror;
    private ImageView ivFlip;
    private ImageView ivCorner;
    private ImageView ivPadding;
    private StickerModel stickerModel;

    private List<String> btnTexts = new ArrayList<>();
    private List<Integer> btnIds = new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.app_activity_puzzle;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        stickerModel = new StickerModel();
        deviceWidth = getResources().getDisplayMetrics().widthPixels;
        deviceHeight = getResources().getDisplayMetrics().heightPixels;
        Intent intent = getIntent();
        saveDirPath = intent.getStringExtra(Key.PUZZLE_SAVE_DIR);
        saveNamePrefix = intent.getStringExtra(Key.PUZZLE_SAVE_NAME_PREFIX);

        photos = intent.getParcelableArrayListExtra(Key.PUZZLE_FILES);
        fileCount = photos.size() > 9 ? 9 : photos.size();
        /**
         * 载入图片
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < fileCount; i++) {
                    Bitmap bitmap = getScaleBitmap(photos.get(i).path, photos.get(i).uri);
                    bitmaps.add(bitmap);
                    degrees.add(0);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        puzzleView.post(new Runnable() {
                            @Override
                            public void run() {
                                loadPhoto();
                            }
                        });
                    }
                });

            }
        }).start();


        initView();
    }


    private void initView() {
        initIvMenu();
        initPuzzleView();
        initRecyclerView();
        setClick(R.id.tv_back, R.id.tv_done);
    }
    private void initIvMenu() {
        mRootView = (RelativeLayout) findViewById(R.id.m_root_view);
        mBottomLayout = (RelativeLayout) findViewById(R.id.m_bottom_layout);

        llMenu = (LinearLayout) findViewById(R.id.ll_menu);
        ivRotate = (ImageView) findViewById(R.id.iv_rotate);
        ivCorner = (ImageView) findViewById(R.id.iv_corner);
        ivPadding = (ImageView) findViewById(R.id.iv_padding);
        setClick(R.id.iv_replace, R.id.iv_mirror, R.id.iv_flip);
        setClick(ivRotate, ivCorner, ivPadding);

        ivMenus.add(ivRotate);
        ivMenus.add(ivCorner);
        ivMenus.add(ivPadding);
        /**
         * SeekBar的监听
         * 通过controlFlag判断为调整边距or圆角or旋转
         */
        numberSeekBar  = (NumberSeekBar) findViewById(R.id.my_number_seek_bar);
        numberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (controlFlag) {
                    case FLAG_CONTROL_PADDING:
                        puzzleView.setPiecePadding(progress);
                        break;
                    case FLAG_CONTROL_CORNER:
                        if (progress < 0) {
                            progress = 0;
                        }
                        puzzleView.setPieceRadian(progress);
                        break;
                    case FLAG_CONTROL_ROTATE:
                        progress = progress - 180;
                        puzzleView.rotate(progress - degrees.get(degreeIndex));
                        degrees.remove(degreeIndex);
                        degrees.add(degreeIndex, progress);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void initRecyclerView() {
        rvPuzzleTemplet = (RecyclerView) findViewById(R.id.rv_puzzle_template);
        puzzleAdapter = new PuzzleAdapter();
        puzzleAdapter.setOnItemClickListener(this);
        rvPuzzleTemplet.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        rvPuzzleTemplet.setAdapter(puzzleAdapter);

        puzzleAdapter.refreshData(PuzzleUtils.getPuzzleLayouts(fileCount),
                getResources().obtainTypedArray(getResources().obtainTypedArray(R.array.puzzle_models_unsel).getResourceId(fileCount-2,1)),
                getResources().obtainTypedArray(getResources().obtainTypedArray(R.array.puzzle_models_sel).getResourceId(fileCount-2,1)));

        initBtnTextAndId();
        puzzlebtnAdapter  = new PuzzleEditBtnAdapter();

    }

    private void initBtnTextAndId(){
        btnTexts.add("滤镜");
        btnIds.add(R.mipmap.puzzle_btn_filter);
        btnTexts.add("微调");
        btnIds.add(R.mipmap.puzzle_btn_finetuning);
        btnTexts.add("贴纸");
        btnIds.add(R.mipmap.puzzle_btn_stickers);
        btnTexts.add("马赛克");
        btnIds.add(R.mipmap.puzzle_btn_mosaic);
        btnTexts.add("文本");
        btnIds.add(R.mipmap.puzzle_btn_add_text);
        btnTexts.add("编辑");
        btnIds.add(R.mipmap.puzzle_btn_edit);
    }
    private void initPuzzleView() {
        int themeType = fileCount > 3 ? 1 : 0;
        puzzleView = (PuzzleView) findViewById(R.id.puzzle_view);
        puzzleView.setPuzzleLayout(PuzzleUtils.getPuzzleLayout(themeType, fileCount, 0));
        puzzleView.setOnPieceSelectedListener(new PuzzleView.OnPieceSelectedListener() {
            @Override
            public void onPieceSelected(PuzzlePiece piece, int position) {
                if(View.GONE == mBottomLayout.getVisibility()){
                    mBottomLayout.setVisibility(View.VISIBLE);
                }
                if (null == piece) {
                    replayAllImageView(0);
                    llMenu.setVisibility(View.GONE);
                    numberSeekBar.setVisibility(View.GONE);
                    degreeIndex = -1;
                    controlFlag = -1;
                    return;
                }

                if (degreeIndex != position) {
                    controlFlag = -1;
                    replayAllImageView(0);
                    numberSeekBar.setVisibility(View.GONE);
                }
                llMenu.setVisibility(View.VISIBLE);
                degreeIndex = position;
            }
        });
    }

    /**
     * 往puzzleView中添加碎片
     */
    private void loadPhoto() {
        puzzleView.addPieces(bitmaps);
    }

    private Bitmap getScaleBitmap(String path, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = Setting.imageEngine.getCacheBitmap(this, uri, deviceWidth / 2,
                    deviceHeight / 2);
            if (bitmap == null) {
                bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path),
                        deviceWidth / 2, deviceHeight / 2, true);
            }
        } catch (Exception e) {
            bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path), deviceWidth / 2,
                    deviceHeight / 2, true);
        }
        return bitmap;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        replayAllImageView(id);
        if (R.id.tv_back == id) {
            finish();
        } else if (R.id.tv_done == id) {
            savePhoto();
            numberSeekBar.setVisibility(View.GONE);
        } else if (R.id.iv_replace == id) {
            controlFlag = -1;
            numberSeekBar.setVisibility(View.GONE);
            if (null == toClass) {
                EasyPhotos.createAlbum(this, true, Setting.imageEngine).setCount(1).start(91);
            } else {
                Intent intent = new Intent(this, toClass.get());
                startActivityForResult(intent, 91);
            }
        } else if (R.id.iv_rotate == id) {
           /*
             * 点击（为非90整数度时）转为0度。
             * 点击（为90度整数时）旋转90度
             */
            if (controlFlag == FLAG_CONTROL_ROTATE) {

                if (degrees.get(degreeIndex) % 90 != 0) {
                    puzzleView.rotate(-degrees.get(degreeIndex));
                    degrees.remove(degreeIndex);
                    degrees.add(degreeIndex, 0);
                    numberSeekBar.setProgress(-numberSeekBar.getMinDegree());
                    return;
                }
                puzzleView.rotate(90);
                int degree = degrees.get(degreeIndex) +90+180;
                if (degree == 450 || degree == 0) {
                    degree = 90;
                }
                degrees.remove(degreeIndex);
                degrees.add(degreeIndex, degree);
                numberSeekBar.setProgress(degrees.get(degreeIndex));
                return;
            }
            numberSeekBar.setType(NumberSeekBar.ONEPOINT);
            handleSeekBar(FLAG_CONTROL_ROTATE, -180, 180, degrees.get(degreeIndex));
        } else if (R.id.iv_mirror == id) {
            numberSeekBar.setVisibility(View.GONE);
            controlFlag = -1;
            puzzleView.flipHorizontally();
        } else if (R.id.iv_flip == id) {
            controlFlag = -1;
            numberSeekBar.setVisibility(View.GONE);
            puzzleView.flipVertically();
        } else if (R.id.iv_corner == id) {
            numberSeekBar.setType(NumberSeekBar.NOPOINT);
            handleSeekBar(FLAG_CONTROL_CORNER, 0, 180, puzzleView.getPieceRadian());
        } else if (R.id.iv_padding == id) {
            replayAllImageView(id);
            numberSeekBar.setType(NumberSeekBar.NOPOINT);
            handleSeekBar(FLAG_CONTROL_PADDING, 0, 20, puzzleView.getPiecePadding());
        }
    }

    /***
     * 刷新 功能的icon
     * @param id 当前点击的icon id。
     */

    private void replayAllImageView(@IdRes int id) {
        if (id == R.id.iv_rotate) {
            ivRotate.setImageResource(R.mipmap.puzzle_btn_choose_rotating_sel);
        } else {
            ivRotate.setImageResource(R.mipmap.puzzle_btn_choose_rotating_unsel);
        }
        if (id == R.id.iv_corner) {
            ivCorner.setImageResource(R.mipmap.puzzle_btn_choose_rounded_sel);
        } else {
            ivCorner.setImageResource(R.mipmap.puzzle_btn_choose_rounded_unsel);
        }
        if (id == R.id.iv_padding){
            ivPadding.setImageResource(R.mipmap.puzzle_btn_choose_spacing_sel);
        }else {
            ivPadding.setImageResource(R.mipmap.puzzle_btn_choose_spacing_unsel);
        }
    }

    /**
     * 刷新设置seekBar
     * @param controlFlag 功能标志（边距、圆角……）
     * @param rangeStart 起始素质
     * @param rangeEnd 终止数值
     * @param degrees   当前/默认 数值
     */
    private void handleSeekBar(int controlFlag, int rangeStart, int rangeEnd, float degrees) {
        this.controlFlag = controlFlag;
        numberSeekBar.setVisibility(View.VISIBLE);
        numberSeekBar.setDegree(rangeEnd,rangeStart);
        numberSeekBar.setProgress((int) degrees - rangeStart);
    }

    /**
     * 保存图片
     */
    private void savePhoto() {
        mBottomLayout.setVisibility(View.GONE);
        findViewById(R.id.tv_done).setVisibility(View.INVISIBLE);
        puzzleView.clearHandling();
        puzzleView.invalidate();
        TransportBitmap.getInstance().setBitmap(stickerModel.save(mRootView, puzzleView, puzzleView.getWidth(), puzzleView.getHeight()));
        /**
         * 跳转到分享界面
         * */

        Intent shareIntent = new Intent();
        shareIntent.setClass(PuzzleImgActivity.this,ShareActivity.class);
        startActivity(shareIntent);
        finish();
    }

    public void showToast(String str) {
        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }




    @Override
    public void onItemClick(int themeType, int themeId,int position) {
        puzzleView.setPuzzleLayout(PuzzleUtils.getPuzzleLayout(themeType, fileCount, themeId));
        loadPhoto();
        resetDegrees();
    }

    /**
     * 重置度数
     */
    private void resetDegrees() {
        llMenu.setVisibility(View.GONE);
        numberSeekBar.setVisibility(View.GONE);
        degreeIndex = -1;

        for (int i = 0; i < degrees.size(); i++) {
            degrees.remove(i);
            degrees.add(i, 0);
        }
    }



    @Override
    protected void onDestroy() {
        if (null != toClass) {
            toClass.clear();
            toClass = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (degreeIndex != -1) {
                    degrees.remove(degreeIndex);
                    degrees.add(degreeIndex, 0);
                }

                String tempPath = "";
                Uri tempUri = null;

                ArrayList<Photo> photos =
                        data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                Photo photo = photos.get(0);
                tempPath = photo.path;
                tempUri = photo.uri;

                final String path = tempPath;
                final Uri uri = tempUri;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = getScaleBitmap(path, uri);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                puzzleView.replace(bitmap);
                                bitmaps.set(degreeIndex,bitmap);
                            }
                        });
                    }
                }).start();

                break;
            case RESULT_CANCELED:
                break;
            default:
                break;
        }
    }

    protected String[] getNeedPermissions() {
        return new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setClick(@IdRes int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    private void setClick(View... views) {
        for (View v : views) {
            v.setOnClickListener(this);
        }
    }

    @Override
    public void onBtnItemClick(int themeId) {
//        switch (themeId){
//            case R.id.
//        }
    }
}