package com.aboelyan.sha.mshaweerak.FirbaseNotfication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.aboelyan.sha.mshaweerak.R;

/**
 * Created by Administrator on 05/07/2017.
 */

public class MyNotficationManager {

    private Context ctx;
    public static final int NOTFICATIN_ID=234;

    public MyNotficationManager(Context ctx) {
        this.ctx = ctx;
    }

    public void showNotfication(String from, String notfication, Intent intent){

        PendingIntent pendingIntent=PendingIntent.getActivity(

                ctx,
                NOTFICATIN_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder=new NotificationCompat.Builder(ctx);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification mNotification=builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(from)
                .setSound(defaultSoundUri)
                .setContentText(notfication)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),R.mipmap.ic_launcher))
                .build();
        mNotification.flags |=Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager=(NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTFICATIN_ID,mNotification);


    }
}
