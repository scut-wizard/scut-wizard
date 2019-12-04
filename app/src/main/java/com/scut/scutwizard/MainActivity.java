package com.scut.scutwizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_memo:
                Intent intent_Note = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent_Note);
                break;
            case R.id.navigation_helper:
                Intent intent_Helper = new Intent(MainActivity.this, HelperActivity.class);
                startActivity(intent_Helper);
                break;
            case R.id.navigation_tool:
                Intent intent_Map = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent_Map);
                break;
        }
        return true;
    }


}
