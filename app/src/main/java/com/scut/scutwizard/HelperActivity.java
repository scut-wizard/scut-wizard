package com.scut.scutwizard;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

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
        adapter.addFragment(new StatsFragment(), getString(R.string.deyu));
        adapter.addFragment(new StatsFragment(), getString(R.string.zhiyu));
        adapter.addFragment(new StatsFragment(), getString(R.string.wenti));

        vp.setAdapter(adapter);
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO: complete
    }
}
