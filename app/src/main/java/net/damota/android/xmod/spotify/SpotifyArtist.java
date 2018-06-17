package net.damota.android.xmod.spotify;

import net.damota.android.xmod.Artist;

public class SpotifyArtist implements Artist {

    public String name;


    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public String getName() {
        return name;
    }
}
