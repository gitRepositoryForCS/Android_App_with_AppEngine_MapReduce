package yingchen.cis690.musicplayer.visualizer.Concepts;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by yingchen on 7/23/15.
 */
public class SongList extends ArrayList<Song> implements Parcelable {
    private static final long serialVersionUID = 663585476779879096L;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = this.size();

        // We have to write the list size, we need him recreating the list
        dest.writeInt(size);

        for (int i = 0; i < size; i++) {
            Song r = this.get(i);
            dest.writeLong(r.getID());
            dest.writeString(r.getName());
            dest.writeString(r.getArtist());
        }
    }

    public SongList(){}


    public SongList(Parcel in){
        this();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.clear();

        // First we have to read the list size
        int size = in.readInt();

        for (int i = 0; i < size; i++) {
            Song r = new Song(in.readLong(), in.readString(), in.readString());
            this.add(r);
        }
    }

    public static final Parcelable.Creator<SongList> CREATOR = new Parcelable.Creator<SongList>() {
        public SongList createFromParcel(Parcel in) {
            return new SongList(in);
        }

        public SongList[] newArray(int size) {
            return new SongList[size];
        }
    };


}
