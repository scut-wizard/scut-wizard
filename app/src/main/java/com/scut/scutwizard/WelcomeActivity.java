package com.scut.scutwizard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.rey.material.widget.Button;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity {

    private VideoView videoView;
    private Timer     skipTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        videoView = findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("android.resource://"
                                        + getPackageName()
                                        + "/"
                                        + R.raw.welcome));
        videoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        videoView.setOnPreparedListener(mediaPlayer -> {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

            // hide ime
            InputMethodManager imm = (InputMethodManager) WelcomeActivity.this.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(videoView.getWindowToken(), 0);
        });

        final Intent skipIntent = new Intent(WelcomeActivity.this, MainActivity.class);

        skipTimer = new Timer();
        final TimerTask skipTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(skipIntent);
            }
        };
        skipTimer.schedule(skipTask, 5 * 1000);

        // skip button
        Button skipBtn = findViewById(R.id.skip_btn);
        skipBtn.setOnClickListener(view -> {
            skipTimer.cancel();
            startActivity(skipIntent);
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }
}
