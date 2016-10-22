package yingchen.cs.musicplayer.visualizer.Concepts;

/**
 * Created by yingchen on 7/12/15.
 */
public class Album extends Data {
    private long id;
    private String name;
    private String artist;

    public Album(long songID, String name, String artist) {
        id = songID;
        this.name = name;
        this.artist = artist;
    }
    public long getID(){
        return id;}
    public String getName(){return name;}
    public String getArtist(){return artist;}
}