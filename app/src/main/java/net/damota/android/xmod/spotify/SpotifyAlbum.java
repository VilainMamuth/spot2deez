package net.damota.android.xmod.spotify;

import net.damota.android.xmod.Album;
import net.damota.android.xmod.Image;

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
}
