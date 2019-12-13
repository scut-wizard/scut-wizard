package com.scut.scutwizard.ScoreHelper.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author MinutesSneezer
 */

public abstract class ScoreRemovedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("id") && intent.hasExtra("cat")) {
            final int id = intent.getIntExtra("id", -1), cat = intent.getIntExtra("cat", -1);
            refreshWith(id, cat);
        }
    }

    public abstract void refreshWith(int id, int category);
}
