package net.damota.android.xmod.deezer;

import com.squareup.moshi.Json;

import net.damota.android.xmod.Album;
import net.damota.android.xmod.Artist;
import net.damota.android.xmod.Track;

import java.util.ArrayList;
import java.util.List;

public class DeezerTrack implements Track {

    DeezerArtist artist;
    DeezerAlbum album;

    @Json(name = "title") String name;

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public Album getAlbum() {
        return album;
    }

    @Override
    public String getArtistsNames() {
        return artist.getName();
    }


    public List<Artist> getArtists() {
        List<Artist> liste = new ArrayList<Artist>();
        liste.add(artist);
        return liste;
    }
}
