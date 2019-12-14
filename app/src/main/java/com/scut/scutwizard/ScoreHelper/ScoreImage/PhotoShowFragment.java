package com.scut.scutwizard.ScoreHelper.ScoreImage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
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
    private final static int                     MAX_IMG_NUM = 6;
    private              ArrayList<File>         images      = new ArrayList<>();
    private              Context                 mContext;
    private              NineGridImageView<File> iv;

    public PhotoShowFragment() {
    }

    public void setImages(@NonNull String filenamesStr) {
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
        iv.setAdapter(new NineGridImageViewAdapter<File>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, File o) {
                Glide.with(context).load(o).into(imageView);
            }

            @Override
            protected void onItemImageClick(Context context,
                                            ImageView imageView,
                                            int index,
                                            List<File> list) {
                Toast.makeText(context, "" + index, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
