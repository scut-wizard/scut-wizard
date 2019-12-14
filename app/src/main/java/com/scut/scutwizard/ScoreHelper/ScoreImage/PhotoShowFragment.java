package com.scut.scutwizard.ScoreHelper.ScoreImage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.scut.scutwizard.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author MinutesSneezer
 */

public class PhotoShowFragment extends Fragment {
    private final static int                       MAX_IMG_NUM = 6;
    private              ArrayList<Object>         images      = new ArrayList<>();
    private              Context                   mContext;
    private              NineGridImageView<Object> iv;

    public PhotoShowFragment() {
    }

    public void setImages(@NonNull String filenamesStr) {
        images.clear();
        final String[] filenames = filenamesStr.split(";");
        final LocalMediaDbUtil lmd = new LocalMediaDbUtil(getContext());
        final File dirPath = lmd.getDataDir();
        for (String fn : filenames) {
            File img = new File(dirPath, fn);
            images.add(img);
        }
        iv.setImagesData(images);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();

        iv = view.findViewById(R.id.detail_img_view);
        iv.setMaxSize(MAX_IMG_NUM);
        iv.setAdapter(new NineGridImageViewAdapter<Object>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, Object o) {
                Glide.with(context).load((File) o).into(imageView);
            }

            @Override
            protected void onItemImageClick(Context context,
                                            ImageView imageView,
                                            int index,
                                            List<Object> list) {
                new XPopup.Builder(context).asImageViewer(imageView,
                                                          index,
                                                          list,
                                                          true,
                                                          true,
                                                          -1,
                                                          -1,
                                                          -1,
                                                          false,
                                                          (popupView, position) -> {},
                                                          new XPopupImageLoader() {
                                                              @Override
                                                              public void loadImage(int position,
                                                                                    @NonNull
                                                                                            Object uri,
                                                                                    @NonNull
                                                                                            ImageView imageView) {
                                                                  Glide.with(imageView)
                                                                       .load((File) uri)
                                                                       .apply(new RequestOptions().placeholder(
                                                                               R.mipmap.ic_launcher_round)
                                                                                                  .override(
                                                                                                          Target.SIZE_ORIGINAL))
                                                                       .into(imageView);
                                                              }

                                                              @Override
                                                              public File getImageFile(
                                                                      @NonNull Context context,
                                                                      @NonNull Object uri) {
                                                                  return (File) uri;
                                                              }
                                                          }).show();
            }
        });
    }
}
