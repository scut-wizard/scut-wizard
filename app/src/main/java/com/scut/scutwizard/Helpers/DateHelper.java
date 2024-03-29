package com.scut.scutwizard.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.scut.scutwizard.Note.NoteDatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

public class DateHelper {
    public static String dateToStr(@NonNull Date d, boolean withTime) {
        return new SimpleDateFormat(withTime ? "yyyy-MM-dd hh:mm:ss" : "yyyy-MM-dd",
                                    Locale.getDefault()).format(d);
    }

    public double calcDateDiffFromToday(@NonNull Date date2) {
        Date date1 = new Date();
        return ((double) date2.getTime() - date1.getTime()) / (24 * 3600 * 1000);
    }

    public Date strToDate(@NonNull String date_str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return sdf.parse(date_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    //传入活动的context
    public void updateDaysLeft(Context context) {
        //从数据库中读取所有event，生成 event list
        NoteDatabaseHelper dbHelper = new NoteDatabaseHelper(context, "event_db", null, 1);
        SQLiteDatabase note_db = dbHelper.getWritableDatabase();
        //创建游标对象
        Cursor cursor = note_db.query("event_table", null, null, null, null, null, null);
        //利用游标遍历所有数据对象
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int finish = cursor.getInt(cursor.getColumnIndex("finish"));
                int daysLeft = cursor.getInt(cursor.getColumnIndex("daysLeft"));
                @NonNull String ddl_str = cursor.getString(cursor.getColumnIndex("DDL"));
                double progress = cursor.getDouble(cursor.getColumnIndex("progress"));
                int newDaysLeft;
                //只更新未完成事件
                if (progress < 100) {
                    //计算DDL与当前日期距离天数
                    newDaysLeft = (int) (new DateHelper().calcDateDiffFromToday(strToDate(ddl_str)));
                    //Toast.makeText(AddEventActivity.this,daysLeft+"",Toast.LENGTH_SHORT).show();
                    if (newDaysLeft != daysLeft) {
                        //ddl已过但是未完成任务
                        if (newDaysLeft < 0) {
                            finish = 1;
                            newDaysLeft = 0;
                            Toast.makeText(context, "ddl已过！" + name + "任务未完成", Toast.LENGTH_SHORT)
                                 .show();
                        }
                        ContentValues values = new ContentValues();
                        values.put("daysLeft", newDaysLeft);
                        values.put("finish", finish);
                        note_db.update("event_table",
                                       values,
                                       "id=?",
                                       new String[]{String.valueOf(id)});
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
