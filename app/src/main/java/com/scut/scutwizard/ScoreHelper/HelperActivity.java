package com.scut.scutwizard.ScoreHelper;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
        initView();
    }

    private void initView() {
        tabLayout = findViewById(R.id.helper_tab_layout);
        vp = findViewById(R.id.helper_vp);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(StatsFragment.newInstance(StatsFragment.CATEGORY_DE), getString(R.string.deyu));
        adapter.addFragment(StatsFragment.newInstance(StatsFragment.CATEGORY_ZHI), getString(R.string.zhiyu));
        adapter.addFragment(StatsFragment.newInstance(StatsFragment.CATEGORY_TI), getString(R.string.wenti));

        vp.setAdapter(adapter);
        tabLayout.setupWithViewPager(vp);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TAG", "" + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(HelperActivity.this, uri.toString(), Toast.LENGTH_SHORT);
    }
}
