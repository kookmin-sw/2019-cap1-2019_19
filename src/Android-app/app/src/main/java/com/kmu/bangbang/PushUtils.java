package com.kmu.bangbang;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

public class PushUtils {
    private static final String TAG = PushUtils.class.getSimpleName();

    private static PowerManager.WakeLock mWakeLock;

    public static void acquireWakeLock(Context context){
        Log.i(TAG, "acquireWakeLock : "+mWakeLock);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(
                //PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "myapp:wakeup"
        );

        mWakeLock.acquire();
        Log.i(TAG, "mWakeLock mWakeLock : "+mWakeLock);
    }

    public static void releaseWakeLock(){
        if(mWakeLock != null){
            mWakeLock.release();
            mWakeLock = null;
        }
        Log.i(TAG, "mWakeLock : "+mWakeLock);
    }
}
