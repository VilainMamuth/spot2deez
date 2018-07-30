package net.damota.android.xmod;

import java.util.List;

public interface Track {

    String getTitle();

    @Override
    String toString();

    Album getAlbum();

    String getArtistsNames();

    long getDurationMillis();

    String getDuration();
}
