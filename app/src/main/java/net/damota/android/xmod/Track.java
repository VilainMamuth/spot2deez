package net.damota.android.xmod;

public interface Track {

    String getTitle();

    @Override
    String toString();

    Album getAlbum();

    String getArtistsNames();

    long getDurationMillis();

    String getDuration();
}
