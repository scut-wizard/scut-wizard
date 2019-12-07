package com.scut.scutwizard.Note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    public NoteDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static final String CREATE_EVENT = "create table event_table("
            + "id integer primary key autoincrement,"
            + " name varchar(20),"
            + " DDL varchar(20),"
            + " daysLeft int,"
            + " progress real,"
            + " step int,"
            + " rating double,"
            + " finish int)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句 并 执行

        db.execSQL(CREATE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}