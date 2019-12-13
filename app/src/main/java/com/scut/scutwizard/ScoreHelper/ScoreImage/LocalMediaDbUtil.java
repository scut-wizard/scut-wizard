package com.scut.scutwizard.ScoreHelper.ScoreImage;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.Objects;

/**
 * Utility for {@link com.luck.picture.lib.entity.LocalMedia}
 * to {@link android.database.sqlite.SQLiteDatabase}.
 *
 * Date: 2019/12/8
 */

public class LocalMediaDbUtil {
    public static final String  DIR_NAME = "CertificationImages";
    private             Context mContext;

    public LocalMediaDbUtil(Context context) {
        this.mContext = context;
    }

    public File getDataDir() {
        File cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
            || !Environment.isExternalStorageRemovable()) {
            cachePath = Objects.requireNonNull(mContext.getExternalFilesDir(DIR_NAME));
        } else {
            cachePath = new File(mContext.getFilesDir(), DIR_NAME);
        }
        return cachePath;
    }
}
