package com.scut.scutwizard;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.google.android.material.navigation.NavigationView;
import com.lxj.xpopup.XPopup;
import com.scut.scutwizard.Helpers.DateHelper;
import com.scut.scutwizard.Note.EventItemFrag;
import com.scut.scutwizard.Note.NoteActivity;
import com.scut.scutwizard.ScoreHelper.HelperActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout  mDrawerLayout;
    private EventItemFrag main_note_frag;
    public LocationClient mLocationClient;
    private TextView positionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_note_frag = (EventItemFrag) getSupportFragmentManager().findFragmentById(R.id.main_note_frag);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        navView.setCheckedItem(R.id.navigation_home);
        navView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    break;
                case R.id.navigation_note:
                    Intent intent_note = new Intent(MainActivity.this, NoteActivity.class);
                    startActivity(intent_note);
                    break;
                case R.id.navigation_helper:
                    Intent intent_Helper = new Intent(MainActivity.this, HelperActivity.class);
                    startActivity(intent_Helper);
                    break;
                case R.id.navigation_homework:
                    new XPopup.Builder(MainActivity.this).asLoading("正在加载...")
                                                         .show()
                                                         .delayDismissWith(3500,
                                                                           () -> new XPopup.Builder(
                                                                                   MainActivity.this)
                                                                                   .asConfirm(
                                                                                           "其实并没有这个功能",
                                                                                           "如果你开发出来了，请务必告诉我们。",
                                                                                           "",
                                                                                           "OKK",
                                                                                           () -> {},
                                                                                           () -> {},
                                                                                           true)
                                                                                   .show());
                    break;
                case R.id.navigation_about:
                    new XPopup.Builder(MainActivity.this).asConfirm("关于",
                                                                    "两个主要功能「DDL 助手」和「综测助手」都是在生产实践中…不是，都是在学习生活中由经验教训获得的灵感，使用完多来反馈噢！\n\n华南理工大学 郭佳鑫&郑俊豪 2019.12",
                                                                    "",
                                                                    "关闭",
                                                                    () -> {},
                                                                    () -> {},
                                                                    true).show();
            }
            mDrawerLayout.closeDrawers();
            return true;
        });
        //更新数据库
        new DateHelper().updateDaysLeft(MainActivity.this);
        main_note_frag.refresh();

//以下代码用于测试contentProvider
//
//        private Button add_button_temp;
//
//        add_button_temp = findViewById(R.id.add_button_temp);
//        add_button_temp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri = Uri.parse("content://com.scut.scutwizard.provider/event_table");
//                ContentValues values = new ContentValues();
//                values.put("name","add by Provider");
//                values.put("DDL","2019-12-18");
//                values.put("daysLeft","10");
//                values.put("progress","30.0");
//                values.put("step","4");
//                values.put("rating","2");
//                values.put("finish","0");
//                getContentResolver().insert(uri,values);
//                Toast.makeText(MainActivity.this,"插入成功",Toast.LENGTH_SHORT).show();
//            }
//        });
        mLocationClient =new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        positionText = findViewById(R.id.positionText);
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }
        else{
            requestLocation();
        }

    }
    private void requestLocation(){
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int results:grantResults){
                        if(results!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(this,"发生位置错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default:
        }
    }

    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPosition = new StringBuilder();
                    currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
                    currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
                    currentPosition.append("定位方式：");
                    if(bdLocation.getLocType()==BDLocation.TypeGpsLocation){
                        currentPosition.append("GPS");
                    }else if(bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                        currentPosition.append("网络");
                    }
                    positionText.setText(currentPosition);
                }
            });
        }


    }
}
