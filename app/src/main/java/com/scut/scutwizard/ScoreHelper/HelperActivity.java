package com.scut.scutwizard.ScoreHelper;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.scut.scutwizard.R;
import com.scut.scutwizard.TabAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class HelperActivity extends AppCompatActivity implements StatsFragment.OnFragmentInteractionListener {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        setTitle(R.string.helper);

        tabLayout = findViewById(R.id.helper_tab_layout);
        vp = findViewById(R.id.helper_vp);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(StatsFragment.newInstance("德"), getString(R.string.deyu));
        adapter.addFragment(StatsFragment.newInstance("智"), getString(R.string.zhiyu));
        adapter.addFragment(StatsFragment.newInstance("体"), getString(R.string.wenti));

        vp.setAdapter(adapter);
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(HelperActivity.this, uri.toString(), Toast.LENGTH_SHORT);
    }
}
