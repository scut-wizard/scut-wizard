package com.scut.scutwizard.ScoreHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.scut.scutwizard.R;

import androidx.annotation.Nullable;

public class ScoreDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_SCORE = "create table Score (\n" +
            "    id integer primary key autoincrement,\n" +
            "    des text not null,\n" +
            "    value real not null,\n" +
            "    subtable integer not null,\n" +
            "    createDate integer default CURRENT_TIMESTAMP,\n" +
            "    modifyDate integer default CURRENT_TIMESTAMP,\n" +
            "    eventDate integer default CURRENT_TIMESTAMP,\n" +
            "    category integer not null,\n" +
            "    detail text,\n" +
            "    images text)";
    public static final String CREATE_SUBTABLE = "create table Subtable (\n" +
            "    id integer primary key autoincrement not null,\n" +
            "    name text not null)";
    public static final String INIT_SUBTABLE = "insert into Subtable (id, name) values(?, ?)";
    static final String DB_FILENAME = "score.db";
    private Context mContext;

    public ScoreDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SCORE);
        sqLiteDatabase.execSQL(CREATE_SUBTABLE);
        sqLiteDatabase.execSQL(INIT_SUBTABLE, new String[]{"0", mContext.getString(R.string.default_subtable_name)});
        Log.d("TAG", "Init");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
