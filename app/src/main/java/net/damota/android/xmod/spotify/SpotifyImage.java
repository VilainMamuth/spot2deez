package net.damota.android.xmod.spotify;

import net.damota.android.xmod.Image;

public class SpotifyImage implements Image {
    public int height;
    public int width;
    public String url;

    @Override
    public String getUrl() {
        return url;
    }
}
