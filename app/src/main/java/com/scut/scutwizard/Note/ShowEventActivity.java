package com.scut.scutwizard.Note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scut.scutwizard.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ShowEventActivity extends AppCompatActivity {

    public Button show_event_return_btn;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        toolbar = findViewById(R.id.note_show_toolbar);
        setSupportActionBar(toolbar);

        show_event_return_btn=  findViewById(R.id.show_event_return_btn);
        show_event_return_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowEventActivity.this, NoteActivity.class));
            }
        });

        TextView cnt_events_tv,cnt_complete_events_tv,cnt_complete_per_tv,cnt_finish_progress_tv,cnt_finish_daysleft_tv;
        double cnt_events=0,cnt_complete_events=0,cnt_complete_per=0,cnt_finish_progress=0,cnt_finish_daysleft=0,cnt_finish_events=0;//cnt是已结束任务总数
        cnt_events_tv = findViewById(R.id.cnt_events_tv);
        cnt_complete_events_tv = findViewById(R.id.cnt_complete_events_tv);
        cnt_complete_per_tv = findViewById(R.id.cnt_complete_per_tv);
        cnt_finish_progress_tv = findViewById(R.id.cnt_finish_progress_tv);
        cnt_finish_daysleft_tv = findViewById(R.id.cnt_finish_daysleft_tv);

        //从数据库中读取所有event，生成 event list
        NoteDatabaseHelper dbHelper = new NoteDatabaseHelper(ShowEventActivity.this, "event_db", null, 1);
        SQLiteDatabase note_db = dbHelper.getWritableDatabase();
        //创建游标对象
        Cursor cursor = note_db.query("event_table", null, null, null, null, null, null);
        //利用游标遍历所有数据对象
        if(cursor.moveToFirst()){
            do {
                double progress = cursor.getDouble(cursor.getColumnIndex("progress"));
                int daysLeft = cursor.getInt(cursor.getColumnIndex("daysLeft"));
                int finish = cursor.getInt(cursor.getColumnIndex("finish"));

                cnt_events+=1;
                //已完成任务
                if (progress>=100) {
                    cnt_complete_events+=1;
                }
                //已结束任务
                if(finish==1){
                    cnt_finish_events+=1;
                    cnt_finish_progress+=progress;
                    cnt_finish_daysleft+=daysLeft;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        if(cnt_events!=0) {
            cnt_complete_per=cnt_complete_events/cnt_events;
            if(cnt_finish_events!=0){
                cnt_finish_progress/=cnt_finish_events;
                cnt_finish_daysleft/=cnt_finish_events;
            }
        }

        cnt_events_tv.setText((int)cnt_events + " 件");
        cnt_complete_events_tv.setText((int)cnt_complete_events + " 件");
        cnt_complete_per_tv.setText((int)cnt_complete_per * 100 + " %");
        cnt_finish_progress_tv.setText((int)cnt_finish_progress + " %");
        cnt_finish_daysleft_tv.setText((int)cnt_finish_daysleft + " 天");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_return_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_note_return:
                Intent intent_return = new Intent(ShowEventActivity.this, NoteActivity.class);
                finish();
                startActivity(intent_return);
                break;
        }
        return true;
    }



}
