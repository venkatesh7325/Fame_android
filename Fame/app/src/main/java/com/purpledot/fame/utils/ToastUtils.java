package com.purpledot.fame.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mahiti on 2/6/16.
 */
public class ToastUtils {

    private static final long LONG_DELAY = 3500; // 3.5 seconds
    private static final int SHORT_DELAY = 1000; // 1 seconds
    public static void displayToast(String message, Context context) {
        if (context != null && message != null && !message.isEmpty()) {
            final Toast t1 = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) t1.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(18);
            t1.setGravity(Gravity.CENTER, 0, 0);
            t1.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    t1.cancel();
                }
            }, LONG_DELAY);
        }
    }
}
