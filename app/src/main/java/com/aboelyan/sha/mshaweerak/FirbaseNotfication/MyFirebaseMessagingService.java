package com.aboelyan.sha.mshaweerak.FirbaseNotfication;

import android.content.Intent;
import android.util.Log;

import com.aboelyan.sha.mshaweerak.Opening;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Administrator on 05/07/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG="FcmExampleMessage";
    private MyNotficationManager myNotficationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        notfyUser(remoteMessage.getFrom(),remoteMessage.getNotification().getBody());
    }
    public void notfyUser(String from,String notfication){
         myNotficationManager=new MyNotficationManager(getApplicationContext());
        myNotficationManager.showNotfication(from,notfication,new Intent(getApplicationContext(),Opening.class));

    }
}
