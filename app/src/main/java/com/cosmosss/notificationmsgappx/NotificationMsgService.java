package com.cosmosss.notificationmsgappx;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationMsgService extends FirebaseMessagingService {
    private String TAG = "NotificationMsgService";
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//
//        Log.e("NotificationMsg", "aaa");
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "001")
//                .setContentTitle("テスト")
//                .setContentText("テストテスト")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setStyle(new NotificationCompat.BigTextStyle())
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setAutoCancel(true);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());
//    }
@Override
public void onMessageReceived(RemoteMessage remoteMessage) {
    Log.e("FirebaseMsg", "message arrived");
    // ...

    // TODO(developer): Handle FCM messages here.
    // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
    Log.d(TAG, "From: " + remoteMessage.getFrom());

    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        if (/* Check if data needs to be processed by long running job */ true) {
            // For long-running tasks (10 seconds or more) use WorkManager.
            //scheduleJob();
        } else {
            // Handle message within 10 seconds
            //handleNow();
        }

    }

    // Check if message contains a notification payload.
    String messageBody = "";
    if (remoteMessage.getNotification() != null) {
        messageBody = remoteMessage.getNotification().getBody();
        Log.d(TAG, "Message Notification Body: " + messageBody);
    }

    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.

    sendNotification(messageBody);
}

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        final Intent nIntent = new Intent(this, MainActivity.class);
        nIntent.setAction(Intent.ACTION_MAIN);
        nIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        nIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "001";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Push1 App")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
