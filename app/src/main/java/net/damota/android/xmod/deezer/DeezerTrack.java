package net.damota.android.xmod.deezer;

import com.squareup.moshi.Json;

import net.damota.android.xmod.Album;
import net.damota.android.xmod.Artist;
import net.damota.android.xmod.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeezerTrack implements Track {

    DeezerArtist artist;
    DeezerAlbum album;

    @Json(name = "title") String name;

    public long duration;

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

    @Override
    public long getDurationMillis() {
        return 0;
    }

    @Override
    public String getDuration() {
        if (duration < 3600) {
            return new SimpleDateFormat("mm:ss").format(new Date(duration * 1000));
        } else {
            return new SimpleDateFormat("H:mm:ss").format(new Date(duration * 1000));
        }
    }


    public List<Artist> getArtists() {
        List<Artist> liste = new ArrayList<Artist>();
        liste.add(artist);
        return liste;
    }
}
