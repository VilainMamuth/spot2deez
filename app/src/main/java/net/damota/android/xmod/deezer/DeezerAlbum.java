package net.damota.android.xmod.deezer;

import com.squareup.moshi.Json;

import net.damota.android.xmod.Album;

public class DeezerAlbum implements Album {

    String cover;
    @Json(name = "title") String name;


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
        return null;
    }

    @Override
    public String getGenresNames() {
        return "";
    }
}
