package com.scut.scutwizard.ScoreHelper.DetailView;

import android.content.Intent;
import android.os.Bundle;

import com.scut.scutwizard.R;
import com.scut.scutwizard.ScoreHelper.Score;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author MinutesSneezer
 */

public class ScoreDetailActivity extends AppCompatActivity {

    private RecyclerView  rv;
    private Score         mScore;
    private DetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        Intent intent = getIntent();
        if (intent.hasExtra("score")) {
            mScore = (Score) intent.getSerializableExtra("score");
            setTitle(String.format(Locale.getDefault(), "ID: %d", mScore.getId()));

            mAdapter = new DetailAdapter(ScoreDetailActivity.this, mScore);
            mAdapter.setFragManager(getSupportFragmentManager());

            rv = findViewById(R.id.score_dt_rv);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            rv.setAdapter(mAdapter);
        } else
            finish();
    }
}
