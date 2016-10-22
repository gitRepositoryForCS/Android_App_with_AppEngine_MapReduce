package yingchen.cs.musicplayer.visualizer.Concepts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yingchen on 6/9/15.
 */
public class Song extends Data implements Parcelable{
    private long id;
    private String name;
    private String artist;

    private static final long serialVersionUID = -7060210544600464481L;

    public Song(long songID, String songTitle, String songArtist) {
        id = songID;
        name = songTitle;
        artist = songArtist;
    }
    public long getID(){return id;}
    public String getName(){return name;}//super.getName();}
    public String getArtist(){return artist;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(artist);
    }

    public static final Parcelable.Creator<Song> CREATOR
            = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    private Song(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.artist = in.readString();
    }
}
