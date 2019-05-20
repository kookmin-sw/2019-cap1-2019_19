package com.kmu.bangbang;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireBaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = "MyFirebaseMsgService";

    /*
    @Override
    public void onCreate() {
        super.onCreate();
        PushUtils.acquireWakeLock(this);
    }
    */

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        String messageTitle = remoteMessage.getData().get("title");
        String messageBody = remoteMessage.getData().get("body");

        sendNotification(messageTitle, messageBody);
    }

    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        SharedPreferences pref = getSharedPreferences("alarm", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("alarm", "on");
        editor.commit();

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher) //알림 아이콘
                        .setContentTitle(messageTitle) //타이틀
                        .setContentText(messageBody)    //알림 설명문구
                        .setAutoCancel(true)    //알림 터치시 사라짐
                        .setSound(defaultSoundUri)  //알림 수신음
                       .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

        //PushUtils.releaseWakeLock();
    }

}