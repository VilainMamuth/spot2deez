package net.damota.android.xmod.spotify;

import com.squareup.moshi.Json;

import net.damota.android.xmod.Album;
import net.damota.android.xmod.Episode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SpotifyEpisode implements Episode {

    @Json(name = "duration_ms")
    long durationMs;

    /**
     * The name of the track.
     */
    public String name;

    /**
     * The cover art for the episode in various sizes, widest first.
     */
    public List<SpotifyImage> images;

    @Override
    public String getTitle() {
        return name;
    }

/*
    @Override
    public Album getAlbum() {
        return album;
    }


    @Override
    public String getArtistsNames() {
        return artists.get(0).getName();
    }
*/
    @Override
    public long getDurationMillis() {
        return this.durationMs;
    }

    @Override
    public String getDuration() {
        if (durationMs < 3600000) {
            return new SimpleDateFormat("mm:ss").format(new Date(durationMs));
        } else {
            return new SimpleDateFormat("H:mm:ss").format(new Date(durationMs));
        }
    }

    @Override
    public String getCoverUrl() {
        return images.get(0).getUrl();
    }
}
