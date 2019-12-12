package com.scut.scutwizard.ScoreHelper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import kotlin.NotImplementedError;

public class ScoreDeleteService extends Service {
    ArrayList<Integer> histories = new ArrayList<>();
    //    private MyBinder              mBinder = new MyBinder();
    private LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);

    public ScoreDeleteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
//        return mBinder;
        throw new NotImplementedError();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int id = intent.getIntExtra("id", -1);
        if (!histories.contains(id)) {
            histories.add(id);
            final int category = intent.getIntExtra("cat", -1);
            final Context mContext = getBaseContext();
            try {
                new ScoreDatabaseHelper(mContext,
                                        ScoreDatabaseHelper.DB_FILENAME,
                                        null,
                                        1).getWritableDatabase()
                                          .execSQL("delete from Score where id = ?",
                                                   new String[]{Integer.toString(id)});
                Intent mIntent = new Intent("com.scut.scutwizard.SCORE_REMOVED_BROADCAST");
                mIntent.putExtra("id", id);
                mIntent.putExtra("cat", category);
                lbm.sendBroadcast(mIntent);
                stopSelf();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    class MyBinder extends Binder {
//        public void showSuccessSnackbar(View v) {
////            Snackbar.make(findViewById(R.id.helper_coord_layout), "删除成功~", Snackbar.LENGTH_SHORT);
//        }
//    }
}
