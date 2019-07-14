package net.damota.android.xmod.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "entry")
public class Entry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "provider")
    public String provider;

    @ColumnInfo(name = "artist")
    public String artist;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "ts")
    public long ts;

    @ColumnInfo(name = "cover_url")
    public String coverUrl;

    @ColumnInfo(name = "release_year")
    public String releaseYear;

    @ColumnInfo(name = "duration")
    public String duration;


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getReleaseYear() {
        return releaseYear;
    }
}
