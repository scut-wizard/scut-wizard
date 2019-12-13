package com.scut.scutwizard.ScoreHelper.ScoreImage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luck.picture.lib.entity.LocalMedia;
import com.scut.scutwizard.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author MinutesSneezer
 */

public class PhotoShowFragment extends Fragment {
    private final static int MAX_IMG_NUM = 6;

    private String[]              filenames;
    private ArrayList<LocalMedia> images;
    private Context               mContext;

    public PhotoShowFragment() {
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
    }
}
