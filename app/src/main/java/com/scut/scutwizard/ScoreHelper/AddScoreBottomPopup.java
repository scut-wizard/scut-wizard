package com.scut.scutwizard.ScoreHelper;


import android.content.Context;

import com.lxj.xpopup.core.BottomPopupView;
import com.scut.scutwizard.R;

import androidx.annotation.NonNull;

public class AddScoreBottomPopup extends BottomPopupView {

    public AddScoreBottomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.add_score_bottom_popup;
    }

    @Override
    protected void onShow() {
        super.onShow();
    }
}
