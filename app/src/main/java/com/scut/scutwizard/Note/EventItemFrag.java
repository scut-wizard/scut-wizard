package com.scut.scutwizard.Note;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.scut.scutwizard.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class EventItemFrag extends Fragment {
    private View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_item, container, false);
        return view;
    }
    public void refresh(){
        View visibilityLayout = view.findViewById(R.id.event_item_RelativeLayout_frag);

        RatingBar event_ratingbar = view.findViewById(R.id.event_ratingbar_frag);
        TextView event_name_tv = view.findViewById(R.id.event_name_tv_frag);
        TextView event_daysLeft_tv = view.findViewById(R.id.event_daysLeft_tv_frag);
        ProgressBar event_progressbar = view.findViewById(R.id.event_progressbar_frag);

        NoteDatabaseHelper dbHelper = new NoteDatabaseHelper(getActivity(), "event_db", null, 1);
        SQLiteDatabase note_db = dbHelper.getWritableDatabase();
        //创建游标对象
        Cursor cursor = note_db.query("event_table", null, "progress<=?", new String[]{"100"}, null, null, "rating desc");
        //利用游标遍历所有数据对象
        if(cursor.moveToFirst()){
            String name= cursor.getString(cursor.getColumnIndex("name"));
            int daysLeft = cursor.getInt(cursor.getColumnIndex("daysLeft"));
            double progress = cursor.getDouble(cursor.getColumnIndex("progress"));
            double rating = cursor.getDouble(cursor.getColumnIndex("rating"));

            event_ratingbar.setRating((float)rating);
            event_name_tv.setText(name);
            event_daysLeft_tv.setText("距离ddl还有"+daysLeft+"天");
            event_progressbar.setProgress((int)progress);

            visibilityLayout.setVisibility((View.VISIBLE));

        }else{
            visibilityLayout.setVisibility((View.INVISIBLE));
        }
        cursor.close();

    }

}