package com.example.ed3907en.spot2deez;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class TokenPersister {

    private static final String TAG = TokenPersister.class.getSimpleName();

    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_AT = "expires_at";

    public static void setToken(Context c , String token, long expiresIn, TimeUnit unit ){
        Context app = c.getApplicationContext();

        long now = System.currentTimeMillis();
        long expiresAt = now + unit.toMillis(expiresIn);

        SharedPreferences prefs = app.getSharedPreferences("prefs",Context.MODE_PRIVATE);

        prefs.edit()
                .putString(ACCESS_TOKEN,token)
                .putLong(EXPIRES_AT, expiresAt)
        .apply();

        Log.d(TAG, "Set token to " + token);

    }

    public static String getToken(Context c){
        Context app = c.getApplicationContext();
        SharedPreferences sharedPref = app.getSharedPreferences("prefs",Context.MODE_PRIVATE);

        String token = sharedPref.getString(ACCESS_TOKEN, null);
        long expiresAt = sharedPref.getLong(EXPIRES_AT, 0L);

        Log.d(TAG, "Inside prefs " + token + " " + expiresAt + " : " + System.currentTimeMillis());

        if (token == null || expiresAt < System.currentTimeMillis()) {
            Log.d(TAG, "Token null ou expiré ");

            return null;
        }

        return token;
    }

}
