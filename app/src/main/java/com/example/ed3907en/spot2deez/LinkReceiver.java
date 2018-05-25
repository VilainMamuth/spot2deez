package com.example.ed3907en.spot2deez;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LinkReceiver extends BroadcastReceiver {

    public static final String TAG = LinkReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.d(TAG, "onReceive: ");

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
