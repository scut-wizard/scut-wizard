package com.scut.scutwizard.Note;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class EventDataProvider extends ContentProvider {

    public static final int EVENT_DIR = 0;
    public static final int EVENT_ITEM = 1;
    public static final String AUTHORITY = "com.scut.scutwizard.provider";
    private NoteDatabaseHelper dbHelper;
    private static UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"event_table",EVENT_DIR);
        uriMatcher.addURI(AUTHORITY,"event_table/#",EVENT_ITEM);
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case EVENT_DIR:
                return "vnd.android.cursor.dir/vnd.com.scut.scutwizard.provider.event_table";
            case EVENT_ITEM:
                return "vnd.android.cursor.item/vnd.com.scut.scutwizard.provider.event_table";
        }
        return null;
    }
    public EventDataProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)){
            case EVENT_DIR:
                deleteRows = db.delete("event_table",selection,selectionArgs);
                break;
            case EVENT_ITEM:
                String eventName=uri.getPathSegments().get(1);
                deleteRows = db.delete("event_table","name=?",new String[]{eventName});
                break;
        }
        return deleteRows;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Uri uriReturn=null;
        switch (uriMatcher.match(uri)){
            case EVENT_DIR:
            case EVENT_ITEM:
                long newEventId = db.insert("event_table",null,values);
                uriReturn = Uri.parse("content://"+AUTHORITY+"/event_table/"+newEventId);
                break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new NoteDatabaseHelper(getContext(),"event_db",null,1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case EVENT_DIR:
                cursor = db.query("event_table",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case EVENT_ITEM:
                String eventName=uri.getPathSegments().get(1);
                cursor = db.query("event_table",projection,"name=?",new String[]{eventName},null,null,sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int upDateRows = 0;
        switch (uriMatcher.match(uri)){
            case EVENT_DIR:
                upDateRows = db.update("event_table",values,selection,selectionArgs);
                break;
            case EVENT_ITEM:
                String eventName=uri.getPathSegments().get(1);
                upDateRows = db.update("event_table",values,"name=?",new String[]{eventName});
                break;
        }
        return upDateRows;
    }
}
