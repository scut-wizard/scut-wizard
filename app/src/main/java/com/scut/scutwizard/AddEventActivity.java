package com.scut.scutwizard;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.w3c.dom.Text;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Double.valueOf;

public class AddEventActivity extends AppCompatActivity {

    public EditText add_event_et_name;
    public EditText add_event_et_progress;
    public Button confirmAdd_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        confirmAdd_btn = (Button) findViewById(R.id.confirm_add_btn);
        add_event_et_name = (EditText) findViewById(R.id.add_event_et_name);
        add_event_et_progress = (EditText) findViewById(R.id.add_event_et_progress);
        confirmAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = add_event_et_name.getText().toString();
                double progress = valueOf(add_event_et_progress.getText().toString());

                noteDatabaseHelper dbHelper = new noteDatabaseHelper(AddEventActivity.this, "event_db", null, 1);
                SQLiteDatabase note_db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("progress", progress);
                note_db.insert("event_db", null, values);

                startActivity(new Intent(AddEventActivity.this, NoteActivity.class));
            }
        });

    }

}
