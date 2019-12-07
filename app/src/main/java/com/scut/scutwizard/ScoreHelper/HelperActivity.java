package com.scut.scutwizard.ScoreHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;
import com.ocnyang.contourview.ContourView;
import com.rey.material.widget.Spinner;
import com.scut.scutwizard.R;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

public class HelperActivity extends AppCompatActivity implements StatsFragment.OnFragmentInteractionListener {
    private TabAdapter mTabAdapter;
    private TabLayout mTabLayout;
    private ViewPager vp;
    private ContourView mContourView;
    private Spinner mSpinner;
    private FloatingActionButton mFab;
    private CoordinatorLayout mCoordLayout;

    private ScoreDatabaseHelper scoreDbHelper;
    private ArrayList<Integer> subtableIds;
    private ArrayList<String> subtableNames;

    private StatsFragment deFrag, zhiFrag, tiFrag;
    private ArrayList<Score> deData, zhiData, tiData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        initView();
    }

    private void initView() {

        mCoordLayout = findViewById(R.id.helper_coord_layout);

        /* Init ArrayLists */

        deData = new ArrayList<>();
        zhiData = new ArrayList<>();
        tiData = new ArrayList<>();

        /* Fragments */

        deFrag = StatsFragment.newInstance(StatsFragment.CATEGORY_DE);
        zhiFrag = StatsFragment.newInstance(StatsFragment.CATEGORY_ZHI);
        tiFrag = StatsFragment.newInstance(StatsFragment.CATEGORY_TI);

        /* ContourView background */

        mContourView = findViewById(R.id.contour_view);
        final int[] STYLES = {ContourView.STYLE_SAND, ContourView.STYLE_CLOUDS, ContourView.STYLE_SHELL, ContourView.STYLE_RIPPLES};
        Random random = new Random();
        mContourView.setStyle(STYLES[random.nextInt(4)]);

        /* Spinner */

        mSpinner = findViewById(R.id.subtable_spinner);
        mSpinner.setDropDownVerticalOffset(20);

        // query subtableNames
        scoreDbHelper = new ScoreDatabaseHelper(HelperActivity.this, ScoreDatabaseHelper.DB_FILENAME, null, 1);
        final SQLiteDatabase score_db = scoreDbHelper.getWritableDatabase();
        Cursor cur = score_db.rawQuery("select * from Subtable", null);
        subtableNames = new ArrayList<>();
        subtableIds = new ArrayList<>();
        if (cur.moveToFirst()) {
            final int idCol = cur.getColumnIndex("id");
            final int nameCol = cur.getColumnIndex("name");
            do {
                subtableIds.add(cur.getInt(idCol));
                subtableNames.add(cur.getString(nameCol));
            }
            while (cur.moveToNext());
        }
        cur.close();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(HelperActivity.this, R.layout.spinner_item_white, subtableNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mSpinner.setAdapter(arrayAdapter);

        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                final int selectedId = subtableIds.get(position);
                handleIdChange(score_db, selectedId);
            }
        });

        /* Tab & ViewPager */

        mTabLayout = findViewById(R.id.helper_tab_layout);
        vp = findViewById(R.id.helper_vp);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mTabAdapter.addFragment(deFrag, getString(R.string.deyu));
        mTabAdapter.addFragment(zhiFrag, getString(R.string.zhiyu));
        mTabAdapter.addFragment(tiFrag, getString(R.string.wenti));
        handleIdChange(score_db, 0);


        vp.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(vp);

        /* FAB */

        mFab = findViewById(R.id.fab_add);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new XPopup.Builder(HelperActivity.this)
                        .autoOpenSoftInput(true)
                        .asCustom(new AddScoreBottomPopup(HelperActivity.this))
                        .show();
            }
        });
    }

    private void handleIdChange(SQLiteDatabase db, int id) {
        deData.clear();
        zhiData.clear();
        tiData.clear();
        // query
        Cursor cur = db.rawQuery("select * from Score where subtable = ? order by eventDate desc", new String[]{Integer.toString(id)});
        if (cur.moveToFirst()) {
            final int c_id = cur.getColumnIndex("id");
            final int c_des = cur.getColumnIndex("text");
            final int c_val = cur.getColumnIndex("value");
            final int c_st = cur.getColumnIndex("subtable");
            final int c_crD = cur.getColumnIndex("createDate");
            final int c_moD = cur.getColumnIndex("modifyDate");
            final int c_evD = cur.getColumnIndex("eventDate");
            final int c_cat = cur.getColumnIndex("category");
            final int c_dt = cur.getColumnIndex("detail");
            final int c_img = cur.getColumnIndex("images");
            final Score.Category[] cats = Score.Category.values();
            do {
                Score tmp = new Score();
                tmp.setId(cur.getInt(c_id));
                tmp.setDescription(cur.getString(c_des));
                tmp.setValue(cur.getDouble(c_val));
                tmp.setCreateDate(new Date(cur.getLong(c_crD)));
                tmp.setLastModifiedDate(new Date(cur.getLong(c_moD)));
                tmp.setEventDate(new Date(cur.getLong(c_evD)));
                tmp.setSpecificCategory(cur.getString(c_dt));
                tmp.setSubtable(cur.getInt(c_st));

                ArrayList<String> tmp_paths = new ArrayList<>();
                Collections.addAll(tmp_paths, cur.getString(c_img).split(";"));
                tmp.setImagePaths(tmp_paths);

                final Score.Category tmp_cat = cats[cur.getInt(c_cat)];
                tmp.setCategory(tmp_cat);
                switch (tmp_cat) {
                    case DEYU:
                        deData.add(tmp);
                        break;
                    case ZHIYU:
                        zhiData.add(tmp);
                        break;
                    case WENTI:
                        tiData.add(tmp);
                        break;
                }
            } while (cur.moveToNext());
        } else {
            Snackbar.make(mCoordLayout, "😀暂无数据，快点击按钮添加吧！", Snackbar.LENGTH_LONG).show();
        }
        cur.close();
        // update fragments
        deFrag.setData(deData);
        zhiFrag.setData(zhiData);
        tiFrag.setData(tiData);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(HelperActivity.this, uri.toString(), Toast.LENGTH_SHORT);
    }
}
