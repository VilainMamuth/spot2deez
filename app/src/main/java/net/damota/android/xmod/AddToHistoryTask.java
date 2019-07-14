package net.damota.android.xmod;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import net.damota.android.xmod.room.AppDatabase;
import net.damota.android.xmod.room.Entry;

public class AddToHistoryTask extends AsyncTask<Void, Void, Void> {
    public static final String TAG = AddToHistoryTask.class.getSimpleName();
    private Context c;
    private Entry e;

    public AddToHistoryTask(Context c, Entry e) {
        this.c = c;
        this.e = e;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase db = Room.databaseBuilder(c,AppDatabase.class,"appdb").build();
        e.setTs(System.currentTimeMillis());
        db.entryDao().insert(e);
        Log.d(TAG, "doInBackground: Added to database");
        return null;
    }
}
