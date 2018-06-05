package com.example.ed3907en.spot2deez;

import java.util.List;

public class Track {
    /**
     * The album on which the track appears. The album object includes a link in href to full information about the album.
     */
    public Album album;

    /**
     * The artists who performed the track. Each artist object includes a link in href to more detailed information about the artist.
     */
    public List<Artist> artists;
    /**
     *  	The name of the track.
     */
    public String name;


    @Override
    public String toString() {
        return "Track{" +
                "album=" + album +
                ", artists=" + artists +
                ", name='" + name + '\'' +
                '}';
    }
}
