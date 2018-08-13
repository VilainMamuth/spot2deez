package net.damota.android.xmod.spotify;

import net.damota.android.xmod.Album;

import java.util.List;

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
