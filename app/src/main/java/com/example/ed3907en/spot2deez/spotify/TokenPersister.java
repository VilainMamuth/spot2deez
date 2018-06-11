package com.example.ed3907en.spot2deez.spotify;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class TokenPersister {

    private static final String TAG = TokenPersister.class.getSimpleName();

    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_AT = "expires_at";

    public static void setToken(Context c , Token token){
        Context app = c.getApplicationContext();

        long now = System.currentTimeMillis();
        long expiresAt = now + TimeUnit.SECONDS.toMillis(token.getExpries_in());

        SharedPreferences prefs = app.getSharedPreferences("prefs",Context.MODE_PRIVATE);

        prefs.edit()
                .putString(ACCESS_TOKEN, token.getAccess_token())
                .putLong(EXPIRES_AT, expiresAt)
        .apply();

        Log.d(TAG, "Set token to " + token);

    }

    public static Token getToken(Context c){
        Context app = c.getApplicationContext();
        SharedPreferences sharedPref = app.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        Token token = null;
        String tokenString = sharedPref.getString(ACCESS_TOKEN, null);
        long expiresAt = sharedPref.getLong(EXPIRES_AT, 0L);

        Log.d(TAG, "Inside prefs " + tokenString + " " + expiresAt + " : " + System.currentTimeMillis());

        if (tokenString == null || expiresAt < System.currentTimeMillis()) {
            Log.d(TAG, "Token null ou expiré ");

            return null;
        }else{
            token = new Token();
            token.setAccess_token(tokenString);
            // a priori seul l'access token est utile , pas besoin de mettre le reste
        }


        return token;
    }

}
