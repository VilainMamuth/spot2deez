package net.damota.android.xmod;

public interface Album {

    String getTitle();

    @Override
     String toString();

    String getCoverUrl();

    String getArtistsNames();

    String getGenresNames();

    String getReleaseYear();

}
