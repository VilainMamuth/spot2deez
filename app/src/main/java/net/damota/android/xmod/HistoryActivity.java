package net.damota.android.xmod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import net.damota.android.xmod.room.AppDatabase;
import net.damota.android.xmod.room.Entry;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private final static String TAG = HistoryActivity.class.getSimpleName();
    private static AppDatabase db;
    private RecyclerView rv;
    private EntryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        this.db = Room.databaseBuilder(this,AppDatabase.class,"appdb").build();

        this.rv = (RecyclerView)findViewById(R.id.historique);
        rv.setHasFixedSize(true);

        rv.setLayoutManager(new LinearLayoutManager(this));

        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        class GetDataTask extends AsyncTask<Void,Void,List<Entry>>{

            @Override
            protected List<Entry> doInBackground(Void... voids) {
                Log.d(TAG, "doInBackground: ");
                return HistoryActivity.db.entryDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Entry> entries) {
                Log.d(TAG, "onPostExecute: nb " + entries.size());
                adapter = new EntryAdapter(entries);
                rv.setAdapter(adapter);
                super.onPostExecute(entries);
            }
        }
        GetDataTask t = new GetDataTask();
        t.execute();
    }
}
