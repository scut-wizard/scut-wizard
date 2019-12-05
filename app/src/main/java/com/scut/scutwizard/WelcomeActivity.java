package com.scut.scutwizard;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.rey.material.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity {

    private VideoView videoView;
    private Timer skipTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        videoView = findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.welcome));
        videoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
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

        // 跳过键
        Button skipBtn = findViewById(R.id.skip_btn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipTimer.cancel();
                startActivity(skipIntent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }
}
