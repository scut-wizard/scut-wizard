package com.scut.scutwizard.ScoreHelper;

import android.content.Intent;
import android.os.Bundle;

import com.scut.scutwizard.R;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreDetailActivity extends AppCompatActivity {

    private Score mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        Intent intent = getIntent();
        mScore = (Score) intent.getSerializableExtra("score");
    }
}
