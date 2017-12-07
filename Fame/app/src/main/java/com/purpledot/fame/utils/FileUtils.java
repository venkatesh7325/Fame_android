package com.purpledot.fame.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.purpledot.fame.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by venkatesh on 12/5/2017.
 */

public class FileUtils {
    public static boolean saveImageIntoSdcard(Context context, Bitmap bitmap, String nameOfFile) {
        boolean isFileSaved = false;
        try {
            File myDir = new File(Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name));
            Logger.logD("FileUtils--", "--myDir--" + myDir + "--bitmap Name--" + bitmap + "-File name--" + nameOfFile);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            File file = new File(myDir, nameOfFile);
            if (file.exists()) file.delete();
            try {
                //new AndroidBmpUtil().save(bitmap, file);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                isFileSaved = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isFileSaved;
    }

    public static Bitmap getImageFromSDCard(Context context, String fileName) {
        Bitmap bitmap = null;
        try {
            Logger.logD("File Util", " GET Image -" + Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name) + "/" + fileName);
            File myDir = new File(Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name) + "/" + fileName);
            Logger.logD("FILE UTIL", " Image PATH---" + myDir.getAbsoluteFile());
            bitmap = BitmapFactory.decodeFile(myDir.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
