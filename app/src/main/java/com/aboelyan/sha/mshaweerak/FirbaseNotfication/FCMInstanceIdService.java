package com.aboelyan.sha.mshaweerak.FirbaseNotfication;


import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 04/07/2017.
 */

public class FCMInstanceIdService extends FirebaseInstanceIdService {

    public static final String TOKEN_BROADCAST="myfcmtokenbroadcast";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

       // TODO: Implement this method to send any registration to your app's servers.
        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        storToken(refreshedToken);
    }

    private void storToken(String token) {
        SharedprefManager.getIstance(getApplicationContext()).storToken(token);
    }


}
