package com.scut.scutwizard.ScoreHelper;

import android.content.Context;
import android.content.Intent;

import com.lxj.xpopup.core.CenterPopupView;
import com.scut.scutwizard.R;
import com.shitij.goyal.slidebutton.SwipeButton;

import androidx.annotation.NonNull;

/**
 * @author MinutesSneezer
 */

public class DeleteScoreConfirmPopup extends CenterPopupView {
    private int id, category;
    private Context mContext;
//    private ScoreDeleteService.MyBinder deleteBinder;
//
//    private ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            deleteBinder = (ScoreDeleteService.MyBinder) iBinder;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//        }
//    };

    public DeleteScoreConfirmPopup(@NonNull Context context, int scoreId, int scoreCat) {
        super(context);
        this.mContext = context;
        this.id = scoreId;
        this.category = scoreCat;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.score_delete_confirm;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        SwipeButton mSwipe = findViewById(R.id.delete_confirm_swipe);
        mSwipe.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {
            }

            @Override
            public void onSwipeCancel() {
            }

            @Override
            public void onSwipeConfirm() {
                Intent deleteIntent = new Intent(mContext, ScoreDeleteService.class);
                deleteIntent.putExtra("id", id);
                deleteIntent.putExtra("cat", category);
                DeleteScoreConfirmPopup.this.delayDismissWith(500,
                                                              () -> mContext.startService(
                                                                      deleteIntent));
            }
        });

        findViewById(R.id.delete_cancel_btn).setOnClickListener(view -> DeleteScoreConfirmPopup.this
                .dismiss());
    }
}
