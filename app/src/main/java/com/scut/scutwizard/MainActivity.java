package com.scut.scutwizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.lxj.xpopup.XPopup;
import com.scut.scutwizard.Helpers.DateHelper;
import com.scut.scutwizard.Helpers.StringHelper;
import com.scut.scutwizard.Note.EventItemFrag;
import com.scut.scutwizard.Note.NoteActivity;
import com.scut.scutwizard.ScoreHelper.HelperActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout  mDrawerLayout;
    private EventItemFrag main_note_frag;
    private TextView slogan_tv;
    private TextView coffee_text_tv;
    private ImageView coffee_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_note_frag = (EventItemFrag) getSupportFragmentManager().findFragmentById(R.id.main_note_frag);
        slogan_tv = findViewById(R.id.slogan_tv);
        coffee_text_tv = findViewById(R.id.coffee_text_tv);
        coffee_iv = findViewById(R.id.coffee_iv);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        navView.setCheckedItem(R.id.navigation_home);
        navView.setNavigationItemSelectedListener(menuItem -> {
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
                case R.id.navigation_map:
                    Intent intent_map = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intent_map);
                    break;
                case R.id.navigation_homework:
                    new XPopup.Builder(MainActivity.this).asLoading("正在加载...")
                                                         .show()
                                                         .delayDismissWith(3500,
                                                                           () -> new XPopup.Builder(
                                                                                   MainActivity.this)
                                                                                   .asConfirm(
                                                                                           "其实并没有这个功能",
                                                                                           "如果你开发出来了，请务必告诉我们。",
                                                                                           "",
                                                                                           "OKK",
                                                                                           () -> {},
                                                                                           () -> {},
                                                                                           true)
                                                                                   .show());
                    break;
                case R.id.navigation_about:
                    new XPopup.Builder(MainActivity.this).asConfirm("关于",
                                                                    "两个主要功能「DDL 助手」和「综测助手」都是在生产实践中…不是，都是在学习生活中由经验教训获得的灵感，使用完多来反馈噢！\n\n华南理工大学 郭佳鑫&郑俊豪 2019.12",
                                                                    "",
                                                                    "关闭",
                                                                    () -> {},
                                                                    () -> {},
                                                                    true).show();
            }
            mDrawerLayout.closeDrawers();
            return true;
        });
        //更新数据库
        new DateHelper().updateDaysLeft(MainActivity.this);
        boolean isshow = main_note_frag.refresh();
        if(isshow){
            coffee_iv.setVisibility(View.GONE);
            coffee_text_tv.setVisibility(View.GONE);
        }else{
            coffee_iv.setVisibility(View.VISIBLE);
            coffee_text_tv.setVisibility(View.VISIBLE);
        }
        //更新标语
        String slogan;
        slogan = new StringHelper().newSlogan(MainActivity.this);
        slogan_tv.setText(slogan);

//以下代码用于测试contentProvider
//
//        private Button add_button_temp;
//
//        add_button_temp = findViewById(R.id.add_button_temp);
//        add_button_temp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri = Uri.parse("content://com.scut.scutwizard.provider/event_table");
//                ContentValues values = new ContentValues();
//                values.put("name","add by Provider");
//                values.put("DDL","2019-12-18");
//                values.put("daysLeft","10");
//                values.put("progress","30.0");
//                values.put("step","4");
//                values.put("rating","2");
//                values.put("finish","0");
//                getContentResolver().insert(uri,values);
//                Toast.makeText(MainActivity.this,"插入成功",Toast.LENGTH_SHORT).show();
//            }
//        });


    }

}
