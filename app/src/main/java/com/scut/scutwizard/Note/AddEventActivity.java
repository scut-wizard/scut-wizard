package com.scut.scutwizard.Note;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.scut.scutwizard.Helpers.DateHelper;
import com.scut.scutwizard.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class AddEventActivity extends AppCompatActivity {

    public EditText add_event_et_name;
    public EditText add_event_et_progress;
    public Button confirmAdd_btn;
    public Button add_event_return_btn;
    public DatePicker add_event_et_daysLeft;
    public EditText add_event_et_step;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        confirmAdd_btn =  findViewById(R.id.confirm_add_btn);
        add_event_return_btn= findViewById(R.id.add_event_return_btn);
        add_event_et_name =  findViewById(R.id.add_event_et_name);
        add_event_et_progress =  findViewById(R.id.add_event_et_progress);
        add_event_et_daysLeft =  findViewById(R.id.add_event_et_daysLeft);
        add_event_et_step =  findViewById(R.id.add_event_et_step);

        toolbar = findViewById(R.id.note_add_toolbar);
        setSupportActionBar(toolbar);

        confirmAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //判断输入合理性
                if(TextUtils.isEmpty(add_event_et_name.getText().toString())||TextUtils.isEmpty(add_event_et_progress.getText())||TextUtils.isEmpty(add_event_et_step.getText())){
                    Toast.makeText(AddEventActivity.this,"输入均不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    String name = add_event_et_name.getText().toString();
                    double progress = parseDouble(add_event_et_progress.getText().toString());
                    int step = parseInt(add_event_et_step.getText().toString());
                    double daysLeft;
                    double rating;

                    //计算DDL与当前日期距离天数
                    String ddlDate_str = add_event_et_daysLeft.getYear()+"-"+(add_event_et_daysLeft.getMonth()+1)+"-"+add_event_et_daysLeft.getDayOfMonth();

                    daysLeft = new DateHelper().calcDateDiffFromToday(new DateHelper().strToDate(
                            ddlDate_str));
                    //Toast.makeText(AddEventActivity.this,daysLeft+"",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(AddEventActivity.this,add_event_et_daysLeft.getDayOfMonth()+"",Toast.LENGTH_SHORT).show();
                    if(progress<0||progress>99){
                        Toast.makeText(AddEventActivity.this,"完成进度输入0-99的数字",Toast.LENGTH_SHORT).show();
                    }else if(step<1||step>10){
                        Toast.makeText(AddEventActivity.this,"步数输入1-10的整数",Toast.LENGTH_SHORT).show();
                    }else if(daysLeft<=0){
                        Toast.makeText(AddEventActivity.this,"输入日期要是将来哟",Toast.LENGTH_SHORT).show();
                    }else{
                        NoteDatabaseHelper dbHelper = new NoteDatabaseHelper(AddEventActivity.this, "event_db", null, 1);
                        SQLiteDatabase note_db = dbHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put("name", name);
                        values.put("daysLeft", (int)daysLeft);
                        values.put("progress", progress);
                        values.put("step",step);
                        values.put("DDL",ddlDate_str);
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
                finish();
                startActivity(new Intent(AddEventActivity.this, NoteActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_return_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_note_return:
                Intent intent_return = new Intent(AddEventActivity.this, NoteActivity.class);
                finish();
                startActivity(intent_return);
                break;
        }
        return true;
    }

}
