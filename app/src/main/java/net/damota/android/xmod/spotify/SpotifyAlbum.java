package net.damota.android.xmod.spotify;

import android.os.Build;
import android.support.annotation.RequiresApi;

import net.damota.android.xmod.Album;
import net.damota.android.xmod.Image;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpotifyAlbum implements Album {

    /**
     * The name of the album. In case of an album takedown, the value may be an empty string.
     */
    public String name;
    /**
     * The cover art for the album in various sizes, widest first.
     */
    public List<SpotifyImage> images;
    /**
     * The artists who performed the track. Each artist object includes a link in href to more detailed information about the artist.
     */
    public List<SpotifyArtist> artists;

    public List<String> genres;

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", images=" + images +
                '}';
    }


    @Override
    public String getCoverUrl() {

        return images.get(0).getUrl();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String getArtistsNames() {
        String concat = "";


        return concat;
    }

    @Override
    public String getGenresNames() {
        return "";
    }


}
