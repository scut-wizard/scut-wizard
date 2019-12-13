package com.scut.scutwizard.ScoreHelper;

import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.ocnyang.contourview.ContourView;
import com.rey.material.widget.Spinner;
import com.robinhood.ticker.TickerView;
import com.scut.scutwizard.R;
import com.scut.scutwizard.ScoreHelper.BroadcastReceivers.ScoreRemovedBroadcastReceiver;
import com.scut.scutwizard.ScoreHelper.Score.Category;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Callable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import static com.scut.scutwizard.ScoreHelper.Score.Category.values;

/**
 * @author MinutesSneezer
 * Date: 2019/12
 */

public class HelperActivity extends AppCompatActivity implements
        StatsFragment.OnFragmentInteractionListener {
    private static ArrayList<Integer>   subtableIds   = new ArrayList<>();
    private static ArrayList<String>    subtableNames = new ArrayList<>();
    final          Category[]           CATEGORIES    = values();
    private        TextView             stManageBtn;
    private        TabAdapter           mTabAdapter;
    private        TabLayout            mTabLayout;
    private        ViewPager            vp;
    private        ContourView          mContourView;
    private        Spinner              mSpinner;
    private        FloatingActionButton mFab;
    private        CoordinatorLayout    mCoordLayout;
    private        StatsFragment        deFrag, zhiFrag, tiFrag;
    private LocalBroadcastManager lbm;
    private ScoreDatabaseHelper   scoreDbHelper;
    private int                   selectedSubtableId = 0;
    private ArrayList<Score>      deData, zhiData, tiData;
    private TickerView deTicker, zhiTicker, tiTicker;

    private boolean       isPopupShown = false;
    private BasePopupView mPopup;

    public static String getSubtableNameById(int id_inDatabase) {
        final int pos = subtableIds.indexOf(id_inDatabase);
        return pos != -1 ? subtableNames.get(pos) : "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        initView();
    }

    private void initView() {

        /* Init */

        lbm = LocalBroadcastManager.getInstance(this);

        mCoordLayout = findViewById(R.id.helper_coord_layout);

        deData = new ArrayList<>();
        zhiData = new ArrayList<>();
        tiData = new ArrayList<>();

        deTicker = findViewById(R.id.helper_deyu_ticker);
        zhiTicker = findViewById(R.id.helper_zhiyu_ticker);
        tiTicker = findViewById(R.id.helper_wenti_ticker);

        deFrag = StatsFragment.newInstance(StatsFragment.CATEGORY_DE);
        zhiFrag = StatsFragment.newInstance(StatsFragment.CATEGORY_ZHI);
        tiFrag = StatsFragment.newInstance(StatsFragment.CATEGORY_TI);

        /* ContourView background */

        mContourView = findViewById(R.id.contour_view);
        final int[] STYLES = {ContourView.STYLE_SAND,
                              ContourView.STYLE_CLOUDS,
                              ContourView.STYLE_SHELL,
                              ContourView.STYLE_RIPPLES};
        Random random = new Random();
        mContourView.setStyle(STYLES[random.nextInt(4)]);

        /* Spinner */

        mSpinner = findViewById(R.id.subtable_spinner);
        mSpinner.setDropDownVerticalOffset(20);

        // query subtableNames
        scoreDbHelper = new ScoreDatabaseHelper(HelperActivity.this,
                                                ScoreDatabaseHelper.DB_FILENAME,
                                                null,
                                                1);
        final SQLiteDatabase score_db = scoreDbHelper.getWritableDatabase();
        Cursor cur = score_db.rawQuery("select * from Subtable", null);
        if (cur.moveToFirst()) {
            final int idCol = cur.getColumnIndex("id");
            final int nameCol = cur.getColumnIndex("name");
            do {
                subtableIds.add(cur.getInt(idCol));
                subtableNames.add(cur.getString(nameCol));
            } while (cur.moveToNext());
        }
        cur.close();

        // test
//        resetDatabase();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(HelperActivity.this,
                                                               R.layout.spinner_item_white,
                                                               subtableNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mSpinner.setAdapter(arrayAdapter);

        mSpinner.setOnItemSelectedListener((Spinner parent, View view, int position, long id) -> {
            selectedSubtableId = subtableIds.get(position);
            handleIdChange(score_db, selectedSubtableId, false);
        });

        /* Manage Button */
        stManageBtn = findViewById(R.id.helper_manage_subtable_btntv);
        stManageBtn.setOnClickListener(view -> new XPopup.Builder(HelperActivity.this).asConfirm(
                getString(R.string.regret),
                getString(R.string.regret_text),
                "",
                getString(R.string.ok_8),
                () -> {},
                () -> {},
                true).show());

        /* Tab & ViewPager */

        mTabLayout = findViewById(R.id.helper_tab_layout);
        vp = findViewById(R.id.helper_vp);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mTabAdapter.addFragment(deFrag, getString(R.string.deyu));
        mTabAdapter.addFragment(zhiFrag, getString(R.string.zhiyu));
        mTabAdapter.addFragment(tiFrag, getString(R.string.wenti));
        handleIdChange(score_db, selectedSubtableId, true);

        vp.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(vp);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                popupEmptyData(tab, getString(R.string.empty_tab_selected_text));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(@NonNull TabLayout.Tab tab) {
                popupEmptyData(tab, getString(R.string.empty_tab_reselected_text));
            }

            private void popupEmptyData(@NonNull TabLayout.Tab tab, @NonNull String fstr) {
                int dataNum;
                switch (tab.getPosition()) {
                    case 0:
                        dataNum = deData.size();
                        break;
                    case 1:
                        dataNum = zhiData.size();
                        break;
                    case 2:
                        dataNum = tiData.size();
                        break;
                    default:
                        return;
                }
                if (dataNum == 0)
                    Snackbar.make(mCoordLayout,
                                  String.format(Locale.getDefault(), fstr, tab.getText()),
                                  Snackbar.LENGTH_SHORT).show();
            }
        });

        /* Popup */
        createPopup();
        /* FAB */
        mFab = findViewById(R.id.fab_add);
        mFab.setOnClickListener(view -> {
            if (mPopup == null)
                createPopup();
            else if (!isPopupShown) {
                mFab.hide();
                mPopup.show();
                isPopupShown = true;
            }
        });

        /* Broadcast Receiver */
        lbm.registerReceiver(new ScoreRemovedBroadcastReceiver() {
            @Override
            public void refreshWith(int id, int category) {
                switchByCategory(category,
                                 () -> findAndRemoveById(deData, id),
                                 () -> findAndRemoveById(zhiData, id),
                                 () -> findAndRemoveById(tiData, id));
                applyData();
                Snackbar.make(mCoordLayout,
                              getString(R.string.delete_success),
                              Snackbar.LENGTH_SHORT).show();
            }
        }, new IntentFilter("com.scut.scutwizard.SCORE_REMOVED_BROADCAST"));
    }

    private Boolean findAndRemoveById(ArrayList arr, int id) {
        ListIterator lit = arr.listIterator();
        while (lit.hasNext())
            if (id == ((Score) lit.next()).getId()) {
                lit.remove();
                return true;
            }
        return false;
    }

    public void createPopup() {
        mPopup = new XPopup.Builder(HelperActivity.this).autoOpenSoftInput(true)
                                                        .moveUpToKeyboard(false)
                                                        .asCustom(new AddScoreBottomPopup(
                                                                HelperActivity.this) {
                                                            @Override
                                                            protected void onDismiss() {
                                                                super.onDismiss();
                                                                mFab.show();
                                                                isPopupShown = false;
                                                            }

                                                            @Override
                                                            public int fetchTabPos() {
                                                                return mTabLayout.getSelectedTabPosition();
                                                            }

                                                            @Override
                                                            public FragmentManager fetchFragManager() {
                                                                return HelperActivity.this.getSupportFragmentManager();
                                                            }

                                                            @Override
                                                            public int fetchSubtable() {
                                                                return HelperActivity.this.selectedSubtableId;
                                                            }

                                                            @Override
                                                            protected void insertScores(ArrayList<Score> scores) {
                                                                HelperActivity.this.insertScores(
                                                                        scores,
                                                                        true);
                                                            }

                                                            @Override
                                                            public void destroyPopup() {
                                                                super.destroyPopup();
                                                                HelperActivity.this.createPopup();
                                                            }
                                                        });
    }

    public <T> T switchByCategory(Score s, Callable<T> deyu, Callable<T> zhiyu, Callable<T> wenti) {
        return switchByCategory(s.getCategoryInt(), deyu, zhiyu, wenti);
    }

    public <T> T switchByCategory(int s, Callable<T> deyu, Callable<T> zhiyu, Callable<T> wenti) {
        try {
            switch (CATEGORIES[s]) {
                case DEYU:
                    return deyu.call();
                case ZHIYU:
                    return zhiyu.call();
                case WENTI:
                    return wenti.call();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void deleteScores(@NonNull final List<Score> score, final boolean alsoToDatabase) {
        int failure = 0;
        for (Score s : score) {
            if (alsoToDatabase)
                scoreDbHelper.getWritableDatabase()
                             .execSQL("delete from Score where id = ?",
                                      new String[]{Integer.toString(s.getId())});
            final boolean deleteSuccess = switchByCategory(s,
                                                           () -> deData.remove(s),
                                                           () -> zhiData.remove(s),
                                                           () -> tiData.remove(s));
            if (!deleteSuccess)
                failure++;
        }
        if (failure == 0)
            Snackbar.make(mCoordLayout, "删除成功~", Snackbar.LENGTH_SHORT);
        else if (failure < score.size())
            Snackbar.make(mCoordLayout,
                          String.format(Locale.getDefault(), "部分删除成功，有 %d 项没有找到!", failure),
                          Snackbar.LENGTH_SHORT);
        else
            Snackbar.make(mCoordLayout, "删除失败...", Snackbar.LENGTH_SHORT);
    }

    protected void insertScores(@NonNull List<Score> scores, final boolean alsoToDatabase) {
        for (Score s : scores) {
            if (alsoToDatabase) {
                scoreDbHelper.getWritableDatabase()
                             .execSQL("insert into Score (des, value, subtable, createDate, "
                                      + "modifyDate, eventDate, category, detail, ps, images) values "
                                      + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                      new String[]{s.getDescription(), s.getValueStr(),
                                                   Integer.toString(s.getSubtable()),
                                                   Long.toString(s.getCreateDate().getTime()),
                                                   Long.toString(s.getLastModifiedDate().getTime()),
                                                   Long.toString(s.getEventDate().getTime()),
                                                   Integer.toString(s.getCategoryInt()),
                                                   s.getSpecificCategory(),
                                                   s.getComment(),
                                                   s.getImagePaths()});
            }
            switchByCategory(s, () -> deData.add(s), () -> zhiData.add(s), () -> tiData.add(s));
        }
        this.applyData();
    }

    private String roundValue(double value) {
        return String.format(Locale.getDefault(), "%+.1f", value);
    }

    public void applyData() {
        Score.ScoreComparator scoreCmp = new Score.ScoreComparator();
        Collections.sort(deData, scoreCmp);
        Collections.sort(zhiData, scoreCmp);
        Collections.sort(tiData, scoreCmp);

        /* Calculate Sum */

        double deSum = 0d; // D2-|D3|
        for (Score s : deData)
            deSum += s.getValue();
        deTicker.setText(roundValue(deSum), true);

        double zhiSum = 0d;
        for (Score s : zhiData)
            zhiSum += s.getValue();
        zhiTicker.setText(roundValue(zhiSum), true);

        double tiSum = 0d;
        for (Score s : tiData)
            tiSum += s.getValue();
        tiTicker.setText(roundValue(tiSum), true);

        /* Update Fragment */

        deFrag.setData(deData);
        zhiFrag.setData(zhiData);
        tiFrag.setData(tiData);
    }

    private void handleIdChange(@NonNull SQLiteDatabase db, int id, boolean isInit) {
        deData.clear();
        zhiData.clear();
        tiData.clear();
        boolean hasData;
        // query
        Cursor cur = db.rawQuery("select * from Score where subtable = ? order by eventDate desc",
                                 new String[]{Integer.toString(id)});
        ArrayList<Score> data = new ArrayList<>();
        if (hasData = cur.moveToFirst()) {
            final int c_id = cur.getColumnIndex("id");
            final int c_des = cur.getColumnIndex("des");
            final int c_val = cur.getColumnIndex("value");
            final int c_st = cur.getColumnIndex("subtable");
            final int c_crD = cur.getColumnIndex("createDate");
            final int c_moD = cur.getColumnIndex("modifyDate");
            final int c_evD = cur.getColumnIndex("eventDate");
            final int c_cat = cur.getColumnIndex("category");
            final int c_dt = cur.getColumnIndex("detail");
            final int c_ps = cur.getColumnIndex("ps");
            final int c_img = cur.getColumnIndex("images");
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
                tmp.setComment(cur.getString(c_ps));
//                ArrayList<String> tmp_paths = new ArrayList<>();
//                Collections.addAll(tmp_paths, cur.getString(c_img).split(";"));
                tmp.setImagePaths(cur.getString(c_img));

                final Category tmp_cat = CATEGORIES[cur.getInt(c_cat)];
                tmp.setCategory(tmp_cat);
//                switch (tmp_cat) {
//                    case DEYU:
//                        deData.add(tmp);
//                        break;
//                    case ZHIYU:
//                        zhiData.add(tmp);
//                        break;
//                    case WENTI:
//                        tiData.add(tmp);
//                        break;
//                }
                data.add(tmp);
            } while (cur.moveToNext());
        }
        cur.close();
        insertScores(data, false);
        if (hasData) {
            if (!isInit)
                Snackbar.make(mCoordLayout,
                              getString(R.string.nonempty_subtable_text),
                              Snackbar.LENGTH_LONG).show();
        } else
            Snackbar.make(mCoordLayout,
                          getString(R.string.empty_subtable_text),
                          Snackbar.LENGTH_LONG).show();
    }

    private void resetDatabase() {
        if (scoreDbHelper != null) {
            SQLiteDatabase db = scoreDbHelper.getWritableDatabase();
            db.execSQL("drop table if exists Score");
            db.execSQL("drop table if exists Subtable");
            scoreDbHelper.onCreate(db);
            Toast.makeText(HelperActivity.this, "重置数据库成功~", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFragmentInteraction(@NonNull Uri uri) {
    }
}
