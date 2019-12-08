package com.scut.scutwizard.ScoreHelper.ScoreImage;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemNotBothDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.scut.scutwizard.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {
    private final static String           TAG         = PhotoFragment.class.getSimpleName();
    private              Context          mContext;
    private              RecyclerView     mRv;
    private              int              MAX_IMG_NUM = 6;
    private              List<LocalMedia> selectList  = new ArrayList<>();

    private GridImageAdapter adapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras;
            if (BroadcastAction.ACTION_DELETE_PREVIEW_POSITION.equals(action)) {// 外部预览删除按钮回调
                extras = intent.getExtras();
                int position = Objects.requireNonNull(extras)
                                      .getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                ToastUtils.s(context, "delete image index:" + position);
                if (position < adapter.getItemCount()) {
                    selectList.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }
        }
    };

    @NonNull
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = () -> {
        PictureSelector.create(PhotoFragment.this)
                       .openGallery(PictureMimeType.ofImage())
                       .loadImageEngine(GlideEngine.createGlideEngine())
                       .maxSelectNum(MAX_IMG_NUM)// 最大图片选择数量
                       .minSelectNum(0)// 最小选择数量
                       .imageSpanCount(4)
                       .selectionMode(PictureConfig.MULTIPLE)
                       .previewImage(true)
                       .enablePreviewAudio(false) // 是否可播放音频
                       .isCamera(true)// 是否显示拍照按钮
                       .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                       .compress(true)// 是否压缩
                       .compressQuality(80)// 图片压缩后输出质量 0~ 100
                       .synOrAsy(true)//同步false或异步true 压缩 默认同步
                       //.compressSavePath(getPath())//压缩图片保存地址
                       .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                       .isGif(false)// 是否显示gif图片
                       .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                       .circleDimmedLayer(false)// 是否圆形裁剪
                       .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                       .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                       .openClickSound(false)// 是否开启点击声音
                       .selectionMedia(selectList)// 是否传入已选图片
                       .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                       .cutOutQuality(90)// 裁剪输出质量 默认100
                       .minimumCompressSize(100)// 小于100kb的图片不压缩
                       .rotateEnabled(true) // 裁剪是否可旋转图片
                       .scaleEnabled(true)// 裁剪是否可放大缩小图片
                       .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    };


    public PhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mRv = view.findViewById(R.id.popup_img_rv);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(),
                                                                    MAX_IMG_NUM / 2,
                                                                    GridLayoutManager.VERTICAL,
                                                                    false);
        mRv.setLayoutManager(manager);
        mRv.addItemDecoration(new GridSpacingItemNotBothDecoration(MAX_IMG_NUM / 2,
                                                                   ScreenUtils.dip2px(mContext, 8),
                                                                   true,
                                                                   false));
        adapter = new GridImageAdapter(mContext, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(MAX_IMG_NUM);
        adapter.setOnItemClickListener((position, v) -> {
            if (selectList.size() > 0) {
//                LocalMedia media = selectList.get(position);
//                String mimeType = media.getMimeType();
//                int mediaType = PictureMimeType.getMimeType(mimeType);
                // 预览图片 可自定长按保存路径
//                        PictureWindowAnimationStyle animationStyle = new PictureWindowAnimationStyle();
//                        animationStyle.activityPreviewEnterAnimation = R.anim.picture_anim_up_in;
//                        animationStyle.activityPreviewExitAnimation = R.anim.picture_anim_down_out;
                PictureSelector.create(PhotoFragment.this)
                               .themeStyle(com.luck.picture.lib.R.style.picture_default_style) // xml设置主题
                               //.setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                               //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                               .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                               .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                               .openExternalPreview(position, selectList);

            }
        });
        mRv.setAdapter(adapter);

        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        if (PermissionChecker.checkSelfPermission(mContext,
                                                  Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
            PictureFileUtils.deleteAllCacheDirFile(mContext);
        } else {
            PermissionChecker.requestPermissions(getActivity(),
                                                 new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                 PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
        }

        // 注册外部预览图片删除按钮回调
        BroadcastManager.getInstance(Objects.requireNonNull(getActivity()))
                        .registerReceiver(broadcastReceiver,
                                          BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
                selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回五种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                // 5.media.getAndroidQToPath();为Android Q版本特有返回的字段，此字段有值就用来做上传使用
                // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
                for (LocalMedia media : selectList) {
                    Log.i(TAG, "压缩::" + media.getCompressPath());
                    Log.i(TAG, "原图::" + media.getPath());
                    Log.i(TAG, "裁剪::" + media.getCutPath());
                    Log.i(TAG, "是否开启原图::" + media.isOriginal());
                    Log.i(TAG, "原图路径::" + media.getOriginalPath());
                    Log.i(TAG, "Android Q 特有Path::" + media.getAndroidQToPath());
                }
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE) {// 存储权限
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    PictureFileUtils.deleteCacheDirFile(Objects.requireNonNull(getActivity()),
                                                        PictureMimeType.ofImage());
                } else {
                    Toast.makeText(getActivity(),
                                   getString(R.string.picture_jurisdiction),
                                   Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
