package yingchen.cis690.musicplayer.visualizer.Concepts;

/**
 * Created by yingchen on 7/12/15.
 */
public class Artist extends Data{
    private long id;
    private String name;

    public Artist(long songID, String name) {
        id = songID;
        this.name = name;
    }
    public long getID(){
        return id;}
    public String getName(){return name;}
}
