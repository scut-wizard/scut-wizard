package com.scut.scutwizard.ScoreHelper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.scut.scutwizard.ScoreHelper.ScoreImage.LocalMediaDbUtil;

import java.io.File;
import java.util.ArrayList;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import kotlin.NotImplementedError;

/**
 * @author MinutesSneezer
 */

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
                Intent lbIntent = new Intent("com.scut.scutwizard.SCORE_REMOVED_BROADCAST");
                lbIntent.putExtra("id", id);
                lbIntent.putExtra("cat", category);
                lbm.sendBroadcast(lbIntent);
                final String imgPaths = intent.getStringExtra("img");
                for (String p : imgPaths.split(";")) {
                    final File img = new File(new LocalMediaDbUtil(this).getDataDir(), p);
                    System.gc();
                    final boolean imgDeleted = img.delete();
                    Log.d("sneezer",
                          String.format("onStartCommand: Image %sdeleted! @ ScoreDeleteService",
                                        imgDeleted ? "" : "not "));
                }
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
