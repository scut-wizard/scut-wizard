package com.scut.scutwizard.ScoreHelper.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class SnackbarBroadcastReceiver extends BroadcastReceiver {
    private CoordinatorLayout mCoordLayout;

    public SnackbarBroadcastReceiver(@NonNull CoordinatorLayout coordLayout) {
        this.mCoordLayout = coordLayout;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("content"))
            Snackbar.make(this.mCoordLayout,
                          intent.getStringExtra("content"),
                          intent.getIntExtra("duration", Snackbar.LENGTH_SHORT));

    }
}
