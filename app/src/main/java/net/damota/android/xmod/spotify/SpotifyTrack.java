package net.damota.android.xmod.spotify;

import com.squareup.moshi.Json;

import net.damota.android.xmod.Album;
import net.damota.android.xmod.Artist;
import net.damota.android.xmod.Track;
import net.damota.android.xmod.deezer.DeezerArtist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class SpotifyTrack implements Track {

    @Json(name = "duration_ms")
    long durationMs;

    /**
     * The album on which the track appears. The album object includes a link in href to full information about the album.
     */
    public SpotifyAlbum album;

    /**
     * The artists who performed the track. Each artist object includes a link in href to more detailed information about the artist.
     */
    public List<SpotifyArtist> artists;
    /**
     * The name of the track.
     */
    public String name;

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
        return artists.get(0).getName();
    }

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
}
