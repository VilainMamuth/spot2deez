package net.damota.android.xmod;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.damota.android.xmod.room.AppDatabase;
import net.damota.android.xmod.room.Entry;

import java.util.List;

class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {
    public static final String TAG = EntryAdapter.class.getSimpleName();
    private List<Entry> entries;



    public EntryAdapter(List<Entry> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_entry,parent,false);
        return new EntryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        Entry e = entries.get(position);
        //Log.d(TAG, "onBindViewHolder: " + position + " " + e.getTitle());
        holder.artist.setText(e.getArtist());
        holder.title.setText(e.getTitle());
    }

    @Override
    public int getItemCount() {

        return entries.size();
    }

    public static class EntryViewHolder extends RecyclerView.ViewHolder  {
        private TextView artist,title;

        public EntryViewHolder(@NonNull View itemView) {

            super(itemView);
            artist = (TextView)itemView.findViewById(R.id.artist);
            title = (TextView)itemView.findViewById(R.id.title);
        }
    }


}
