package com.scut.scutwizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.navigation.NavigationView;
import com.scut.scutwizard.Note.DateHelper;
import com.scut.scutwizard.Note.EventItemFrag;
import com.scut.scutwizard.Note.NoteActivity;
import com.scut.scutwizard.ScoreHelper.HelperActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private EventItemFrag main_note_frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_note_frag = (EventItemFrag)getSupportFragmentManager().findFragmentById(R.id.main_note_frag);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        navView.setCheckedItem(R.id.navigation_home);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_note:
                        Intent intent_note = new Intent(MainActivity.this, NoteActivity.class);
                        startActivity(intent_note);
                        break;
                    case R.id.navigation_helper:
                        Intent intent_Helper = new Intent(MainActivity.this, HelperActivity.class);
                        startActivity(intent_Helper);
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        //更新数据库
        new DateHelper().updateDaysLeft(MainActivity.this);
        main_note_frag.refresh();
    }
}
