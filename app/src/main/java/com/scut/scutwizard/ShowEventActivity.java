package com.scut.scutwizard;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scut.scutwizard.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShowEventActivity extends AppCompatActivity {

    public Button show_event_return_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);


        show_event_return_btn= (Button) findViewById(R.id.show_event_return_btn);
        show_event_return_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowEventActivity.this, NoteActivity.class));
            }
        });

        TextView cnt_events_tv,cnt_finish_events_tv,cnt_finish_per_tv,cnt_finish_aver_tv,cnt_daysleft_aver_tv;
        double cnt_events=0,cnt_finish_events=0,cnt_finish_per=0,cnt_finish_aver=0,cnt_daysleft_aver=0;
        cnt_events_tv = findViewById(R.id.cnt_events_tv);
        cnt_finish_events_tv = findViewById(R.id.cnt_finish_events_tv);
        cnt_finish_per_tv = findViewById(R.id.cnt_finish_per_tv);
        cnt_finish_aver_tv = findViewById(R.id.cnt_finish_aver_tv);
        cnt_daysleft_aver_tv = findViewById(R.id.cnt_daysleft_aver_tv);

        //从数据库中读取所有event，生成 event list
        noteDatabaseHelper dbHelper = new noteDatabaseHelper(ShowEventActivity.this, "event_db", null, 1);
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
                if (finish == 1) {
                    cnt_finish_events+=1;
                    cnt_finish_aver+=progress;
                    cnt_daysleft_aver+=daysLeft;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        if(cnt_events!=0) {
            cnt_finish_per=cnt_finish_events/cnt_events;
            if(cnt_finish_events!=0){
                cnt_finish_aver/=cnt_finish_events;
                cnt_daysleft_aver/=cnt_finish_events;
            }
        }

        cnt_events_tv.setText(String.valueOf(cnt_events)+" 件");
        cnt_finish_events_tv.setText(String.valueOf(cnt_finish_events)+ " 件");
        cnt_finish_per_tv.setText(String.valueOf(cnt_finish_per*100)+" %");
        cnt_finish_aver_tv.setText(String.valueOf(cnt_finish_aver*100)+" %");
        cnt_daysleft_aver_tv.setText(String.valueOf(cnt_daysleft_aver)+" 天");


    }



}
