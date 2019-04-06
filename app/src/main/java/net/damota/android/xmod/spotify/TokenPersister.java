package net.damota.android.xmod.spotify;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenPersister {

    private static final String TAG = TokenPersister.class.getSimpleName();

    private static final String PREFS = "spotifyprefs";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_AT = "expires_at";

    public static void setToken(Context c , Token token){
        Context app = c.getApplicationContext();

        SharedPreferences prefs = app.getSharedPreferences(PREFS,Context.MODE_PRIVATE);

        prefs.edit()
                .putString(ACCESS_TOKEN, token.getAccess_token())
                .putLong(EXPIRES_AT, token.getExpiresAt())
        .apply();

        Log.d(TAG, "Set token to " + token);

    }

    public static Token getToken(Context c){
        Context app = c.getApplicationContext();
        SharedPreferences sharedPref = app.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        Token token = null;
        String tokenString = sharedPref.getString(ACCESS_TOKEN, null);
        long expiresAt = sharedPref.getLong(EXPIRES_AT, 0);


        Log.d(TAG, "getToken: Inside prefs " + tokenString + " expAt" + expiresAt + " : now " + System.currentTimeMillis());

        if (tokenString == null || expiresAt < System.currentTimeMillis()) {
            Log.d(TAG, "getToken: Token null ou expiré ");

            return null;
        }else{
            Log.d(TAG, "getToken: Token Valide");
            token = new Token();
            token.setAccess_token(tokenString);
            token.setExpiresAt(expiresAt);
            // a priori seul l'access token est utile , pas besoin de mettre le reste
        }


        return token;
    }

}
