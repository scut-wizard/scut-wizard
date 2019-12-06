package com.scut.scutwizard;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.w3c.dom.Text;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Double.parseDouble;
import static java.lang.Double.valueOf;
import static java.lang.Integer.parseInt;

public class AddEventActivity extends AppCompatActivity {

    public EditText add_event_et_name;
    public EditText add_event_et_progress;
    public Button confirmAdd_btn;
    public Button add_event_return_btn;
    public EditText add_event_et_daysLeft;
    public EditText add_event_et_step;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        confirmAdd_btn = (Button) findViewById(R.id.confirm_add_btn);
        add_event_return_btn= (Button) findViewById(R.id.add_event_return_btn);
        add_event_et_name = (EditText) findViewById(R.id.add_event_et_name);
        add_event_et_progress = (EditText) findViewById(R.id.add_event_et_progress);
        add_event_et_daysLeft = (EditText) findViewById(R.id.add_event_et_daysLeft);
        add_event_et_step = (EditText) findViewById(R.id.add_event_et_step);

        confirmAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //判断输入合理性
                if(TextUtils.isEmpty(add_event_et_name.getText().toString())||TextUtils.isEmpty(add_event_et_progress.getText())||TextUtils.isEmpty(add_event_et_step.getText())||TextUtils.isEmpty(add_event_et_daysLeft.getText())){
                    Toast.makeText(AddEventActivity.this,"输入均不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    String name = add_event_et_name.getText().toString();
                    double progress = parseDouble(add_event_et_progress.getText().toString());
                    int step = parseInt(add_event_et_step.getText().toString());
                    int daysLeft = parseInt(add_event_et_daysLeft.getText().toString());
                    double rating;

                    if(progress<0||progress>99){
                        Toast.makeText(AddEventActivity.this,"完成进度输入0-99的数字",Toast.LENGTH_SHORT).show();
                    }else if(step<1||step>10){
                        Toast.makeText(AddEventActivity.this,"步数输入1-10的整数",Toast.LENGTH_SHORT).show();
                    }else if(daysLeft<1||daysLeft>365){
                        Toast.makeText(AddEventActivity.this,"完成进度输入1-365的整数",Toast.LENGTH_SHORT).show();
                    }else{
                        noteDatabaseHelper dbHelper = new noteDatabaseHelper(AddEventActivity.this, "event_db", null, 1);
                        SQLiteDatabase note_db = dbHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put("name", name);
                        values.put("daysLeft", daysLeft);
                        values.put("progress", progress);
                        values.put("step",step);
                        if(daysLeft>30) rating=0.5;
                        else{
                            double temp = 30/daysLeft*(100-progress)/100;
                            if(temp>5) rating=5;
                            else rating = temp;
                        }
                        values.put("rating",rating);
                        values.put("finish",0);
                        note_db.insert("event_table", null, values);

                        startActivity(new Intent(AddEventActivity.this, NoteActivity.class));
                    }
                }
            }
        });
        add_event_return_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddEventActivity.this, NoteActivity.class));
            }
        });

    }

}
