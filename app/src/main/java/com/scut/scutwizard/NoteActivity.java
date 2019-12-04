package com.scut.scutwizard;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class NoteActivity extends AppCompatActivity {

    private List<Event> eventList = new ArrayList<>();
    public Button add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        add_btn = (Button) findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteActivity.this, AddEventActivity.class));
            }
        });
        //从数据库中读取所有event，生成 event list
        noteDatabaseHelper dbHelper = new noteDatabaseHelper(NoteActivity.this, "event_db", null, 1);
        SQLiteDatabase note_db = dbHelper.getWritableDatabase();
        //创建游标对象
        Cursor cursor = note_db.query("event_table", null, null, null, null, null, null);
        //利用游标遍历所有数据对象
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            double progress = cursor.getDouble(cursor.getColumnIndex("progress"));
            Event event = new Event(name);
            event.progress = progress;
            eventList.add(event);
        }
        cursor.close();


        initEvent();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        EventAdapter adapter = new EventAdapter(eventList);
        recyclerView.setAdapter(adapter);
    }


    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

        private List<Event> mEventList;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView event_tv;
            ProgressBar event_progressbar;

            public ViewHolder(View view) {
                super(view);
                event_tv = (TextView) view.findViewById(R.id.event_name);
                event_progressbar = (ProgressBar) view.findViewById(R.id.event_progress);
            }
        }

        public EventAdapter(List<Event> eventList) {
            mEventList = eventList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            Event event = mEventList.get(position);
            holder.event_tv.setText(event.name);
            holder.event_progressbar.setProgress((int) (event.progress));
        }

        public int getItemCount() {
            return mEventList.size();
        }
    }

    public void initEvent() {
        Event event = new Event("Initial Event 1");
        eventList.add(event);
    }


}