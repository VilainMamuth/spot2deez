package net.damota.android.xmod;

public interface Episode {

    String getTitle();

    @Override
    String toString();

    //Album getAlbum();

    //String getArtistsNames();

    long getDurationMillis();

    String getDuration();

    String getCoverUrl();


}
