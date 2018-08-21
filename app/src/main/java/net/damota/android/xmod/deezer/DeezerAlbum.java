package net.damota.android.xmod.deezer;

import android.util.Log;

import com.squareup.moshi.Json;

import net.damota.android.xmod.Album;

public class DeezerAlbum implements Album {

    String cover;
    @Json(name = "title") String name;

    DeezerArtist artist;

    String release_date;

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getCoverUrl() {
        if (cover == null) {
            return "";
        }
        return cover;
    }

    @Override
    public String getArtistsNames() {
        return artist.getName();
    }

    @Override
    public String getGenresNames() {
        return "";
    }

    @Override
    public String getReleaseYear() {
        return release_date.substring(0,4);
    }


}
