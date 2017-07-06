package com.aboelyan.sha.mshaweerak.FirbaseNotfication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 04/07/2017.
 */

public class SharedprefManager {

    private static final String SHARED_PREF_NAME="fcmshared";
    private static final String KEY_ACCESS_TOKEN="token";

    private static Context mCTX;
    private static SharedprefManager mIstance;

    public SharedprefManager(Context context) {
        mCTX=context;

    }public static synchronized SharedprefManager getIstance(Context context){
        if (mIstance==null)
            mIstance=new SharedprefManager(context);


        return mIstance;
    }
    public boolean storToken(String token){

        SharedPreferences sharedPreferences=mCTX.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN,token);
        editor.apply();
        return true;
    }
    public String getToken(){
        SharedPreferences sharedPreferences=mCTX.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,null);

    }
}
