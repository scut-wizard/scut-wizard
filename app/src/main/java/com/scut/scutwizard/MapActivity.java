package com.scut.scutwizard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.scut.scutwizard.Note.NoteActivity;
import com.scut.scutwizard.Note.ShowEventActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MapActivity extends AppCompatActivity {
    public LocationClient mLocationClient;
    private MapView bmapview;
    private BaiduMap baiduMap;
    private MapStatusUpdate update;
    private boolean isfirstLocate;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient =new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        toolbar = findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);

        bmapview=   (MapView) findViewById(R.id.bmapview);
        baiduMap = bmapview.getMap();
        baiduMap.setMyLocationEnabled(true);

        isfirstLocate=true;

        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MapActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MapActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MapActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapActivity.this,permissions,1);
        }
        else{
            requestLocation();
        }

    }
    private void requestLocation(){
        mLocationClient.start();
    }
    private void navigateTo(BDLocation location){
        if(isfirstLocate){
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);

            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);

            isfirstLocate=false;
        }
        //Toast.makeText(MapActivity.this,"当前位置 ("+location.getLatitude()+","+location.getLongitude()+")",Toast.LENGTH_SHORT).show();
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData myLocationData=locationBuilder.build();
        baiduMap.setMyLocationData(myLocationData);
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
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation||bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                navigateTo(bdLocation);
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        bmapview.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        bmapview.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bmapview.onDestroy();
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
                Intent intent_return = new Intent(MapActivity.this, MainActivity.class);
                finish();
                startActivity(intent_return);
                break;
        }
        return true;
    }
}
