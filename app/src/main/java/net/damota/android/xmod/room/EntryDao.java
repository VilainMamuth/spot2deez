package net.damota.android.xmod.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EntryDao {
    @Query("SELECT * FROM entry ORDER BY ts DESC")
    List<Entry> getAll();


    @Insert
    void insert(Entry e);
}
