package com.scut.scutwizard.Note;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scut.scutwizard.MainActivity;
import com.scut.scutwizard.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class NoteActivity extends AppCompatActivity {

    private List<Event> eventList = new ArrayList<>();
    private String sortStr="rating desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        if(intent.hasExtra("extra_data")) sortStr = intent.getStringExtra("extra_data");

        //从数据库中读取所有event，生成 event list
        NoteDatabaseHelper dbHelper = new NoteDatabaseHelper(NoteActivity.this, "event_db", null, 1);
        SQLiteDatabase note_db = dbHelper.getWritableDatabase();
        //创建游标对象
        Cursor cursor = note_db.query("event_table", null, null, null, null, null, sortStr);
        //利用游标遍历所有数据对象
        if(cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double progress = cursor.getDouble(cursor.getColumnIndex("progress"));
                int daysLeft = cursor.getInt(cursor.getColumnIndex("daysLeft"));
                int finish = cursor.getInt(cursor.getColumnIndex("finish"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int step = cursor.getInt(cursor.getColumnIndex("step"));
                if (finish == 0) {
                    Event event = new Event(name);
                    event.setProgress(progress);
                    event.setDaysLeft(daysLeft);
                    event.setFinish(finish);
                    event.setId(id);
                    event.setStep(step);
                    eventList.add(event);

                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        if(eventList.size()==0){
            //Toast.makeText(NoteActivity.this,"暂无任务",Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_note_empty);

        }else if(eventList.size()>0){
            setContentView(R.layout.activity_note);
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            EventAdapter adapter = new EventAdapter(eventList);
            recyclerView.setAdapter(adapter);
        }


    }


    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

        private List<Event> mEventList;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView event_name_tv;
            ProgressBar event_progressbar;
            Button event_done_btn;
            Button event_giveup_btn;
            TextView event_daysLeft_tv;
            RatingBar event_ratingbar;


            public ViewHolder(View view) {
                super(view);
                event_name_tv =  view.findViewById(R.id.event_name_tv);
                event_progressbar =  view.findViewById(R.id.event_progressbar);
                event_done_btn = view.findViewById(R.id.event_done_btn);
                event_daysLeft_tv =  view.findViewById(R.id.event_daysLeft_tv);
                event_ratingbar = view.findViewById(R.id.event_ratingbar);
                event_giveup_btn = view.findViewById(R.id.event_giveup_btn);
            }
        }

        public EventAdapter(List<Event> eventList) {
            mEventList = eventList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);

            holder.event_done_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Event event = mEventList.get(position);
                    String event_id = String.valueOf(event.id);

                    double newProgress = (100/event.getStep())+event.getProgress();
                    if(newProgress>=100){
                        event.setProgress(100.0);
                        event.setFinish(1);
                        holder.event_progressbar.setProgress((int)newProgress);
                        Toast.makeText(NoteActivity.this,event.getName()+"任务完成！",Toast.LENGTH_SHORT).show();
                    }else{
                        event.setProgress(newProgress);
                        event.setFinish(0);
                        holder.event_progressbar.setProgress((int)newProgress);
                        Toast.makeText(NoteActivity.this,event.getName()+"打卡成功！再接再厉",Toast.LENGTH_SHORT).show();
                    }
                    //更新数据库
                    NoteDatabaseHelper dbHelper = new NoteDatabaseHelper(NoteActivity.this, "event_db", null, 1);
                    SQLiteDatabase note_db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put("progress",event.getProgress());
                    values.put("finish",event.getFinish());
                    values.put("rating",event.getRating());

                    note_db.update("event_table",values,"id=?",new String[]{event_id});

                    /*if(newProgress>=100){
                        //删除已完成的活动
                        for (int i = 0; i < mEventList.size(); i++) {
                            if(event_id.equals(eventList.get(i).getId()) )
                                eventList.remove(i);
                            //Toast.makeText(NoteActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        }
                    }*/
                }
            });
            holder.event_giveup_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(NoteActivity.this);
                    //normalDialog.setIcon(R.drawable.icon_dialog);
                    normalDialog.setTitle("");
                    normalDialog.setMessage("你真的要半途而废吗？");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = holder.getAdapterPosition();
                                    Event event = mEventList.get(position);
                                    String event_id = String.valueOf(event.id);
                                    //更新数据库
                                    NoteDatabaseHelper dbHelper = new NoteDatabaseHelper(NoteActivity.this, "event_db", null, 1);
                                    SQLiteDatabase note_db = dbHelper.getWritableDatabase();

                                    event.setFinish(1);

                                    ContentValues values = new ContentValues();

                                    values.put("finish",event.getFinish());
                                    values.put("rating",event.getRating());

                                    note_db.update("event_table",values,"id=?",new String[]{event_id});
                                    startActivity(new Intent(NoteActivity.this,NoteActivity.class));
                                }
                            });
                    normalDialog.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //...To-do
                                }
                            });
                    // 显示对话框
                    normalDialog.show();
                }
            });
            return holder;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            Event event = mEventList.get(position);
            String days_left_str = event.getDaysLeft()+" days";
            holder.event_name_tv.setText(event.getName());
            holder.event_progressbar.setProgress((int)event.getProgress());
            holder.event_daysLeft_tv.setText(days_left_str);
            holder.event_ratingbar.setRating((float)event.getRating());
        }

        public int getItemCount() {
            return mEventList.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_add:
                Intent intent_add = new Intent(NoteActivity.this, AddEventActivity.class);
                finish();
                startActivity(intent_add);
                break;
            case R.id.navigation_cnt:
                Intent intent_cnt = new Intent(NoteActivity.this, ShowEventActivity.class);
                finish();
                startActivity(intent_cnt);
                break;
            case R.id.navigation_sort:
                Intent intent_sort = new Intent(NoteActivity.this, NoteActivity.class);
               if(sortStr.equals("rating desc")){
                   intent_sort.putExtra("extra_data","daysLeft");
                   Toast.makeText(NoteActivity.this,"按剩余天数排序",Toast.LENGTH_SHORT).show();
               }
                else if(sortStr.equals("daysLeft")){
                   intent_sort.putExtra("extra_data","progress");
                   Toast.makeText(NoteActivity.this,"按剩余进度排序",Toast.LENGTH_SHORT).show();
               }
                else if(sortStr.equals("progress")){
                   intent_sort.putExtra("extra_data","rating desc");
                   Toast.makeText(NoteActivity.this,"按紧急度排序",Toast.LENGTH_SHORT).show();
               }
                finish();
                startActivity(intent_sort);
                break;
            case R.id.navigation_note_return:
                Intent intent_return = new Intent(NoteActivity.this, MainActivity.class);
                finish();
                startActivity(intent_return);
                break;
        }
        return true;
    }


}