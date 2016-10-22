package yingchen.cs.musicplayer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import yingchen.cs.musicplayer.visualizer.Concepts.Album;
import yingchen.cs.musicplayer.visualizer.Concepts.Artist;
import yingchen.cs.musicplayer.visualizer.Concepts.Data;
import yingchen.cs.musicplayer.visualizer.Concepts.Song;

/**
 * Created by yingchen on 6/9/15.
 */
public class SongAdapter extends BaseAdapter {
    private ArrayList<Data> data;
    private LayoutInflater songInf;

    public SongAdapter(Context c, ArrayList<Data> data){
        this.data=data;
        songInf=LayoutInflater.from(c);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout songLay = (LinearLayout)songInf.inflate(R.layout.song, parent, false);
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);

        if(data!=null && data.get(0) instanceof Song){
            Song currSong = (Song) data.get(position);
            songView.setText(currSong.getName());
            artistView.setText(currSong.getArtist());
            songLay.setTag(position);
        }else if(data!=null && data.get(0) instanceof Album){
            Album currAlbum =(Album) data.get(position);
            songView.setText(currAlbum.getName());
            artistView.setText(currAlbum.getArtist());
            songLay.setTag(position);
        }else if(data!=null && data.get(0) instanceof Artist){
            Artist currAlbum = (Artist) data.get(position);
            songView.setText(currAlbum.getName());
            artistView.setHeight(0);
        }
        return songLay;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
}
