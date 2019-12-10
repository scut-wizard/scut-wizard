package com.scut.scutwizard.ScoreHelper;

import android.content.Context;

import com.lxj.xpopup.core.CenterPopupView;
import com.scut.scutwizard.R;
import com.shitij.goyal.slidebutton.SwipeButton;

import androidx.annotation.NonNull;

public class DeleteScoreConfirmPopup extends CenterPopupView {
    private int id, category;
    private Context mContext;

    public DeleteScoreConfirmPopup(@NonNull Context context,
                                   @NonNull int scoreId,
                                   @NonNull int scoreCat) {
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
//                DeleteScoreConfirmPopup.this.delayDismissWith(1000, () -> {
//                    new ScoreDatabaseHelper(mContext,
//                                            ScoreDatabaseHelper.DB_FILENAME,
//                                            null,
//                                            1).getWritableDatabase()
//                                              .execSQL("delete from Score where id = ?",
//                                                       new String[]{Integer.toString(id)});
//                    Snackbar.make(findViewById(R.id.helper_coord_layout),
//                                  "删除成功~",
//                                  Snackbar.LENGTH_SHORT);
//                });
            }
        });

        findViewById(R.id.delete_cancel_btn).setOnClickListener(view -> DeleteScoreConfirmPopup.this
                .dismiss());
    }
}
