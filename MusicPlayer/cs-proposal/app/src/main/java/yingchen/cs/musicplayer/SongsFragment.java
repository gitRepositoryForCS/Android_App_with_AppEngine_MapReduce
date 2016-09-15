package yingchen.cs.musicplayer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

import yingchen.cis690.musicplayer.visualizer.Concepts.Album;
import yingchen.cis690.musicplayer.visualizer.Concepts.Artist;
import yingchen.cis690.musicplayer.visualizer.Concepts.Data;
import yingchen.cis690.musicplayer.visualizer.Concepts.Song;
import yingchen.cis690.musicplayer.visualizer.Concepts.SongList;

/*
*
* */
public class SongsFragment extends Fragment {
    public static ArrayList<Song> songList;
    private ListView songView;
    private SongClickListener songClickListener;
    private ArrayList<Data> data;

    private int positionInNavigation;
    private int mLevel;
    private String text;
    private int positionClicked;

    private SongList songListForParcelable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.song_list, container, false);
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        songClickListener = (SongClickListener) activity;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
       // Log.i("SongFragment","onDestroy called !!!!!!!!!!!!!!!!!");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle==null){
            return;
        }
        positionInNavigation = bundle.getInt("positionInNavigation");
        mLevel = bundle.getInt("level");
        text = bundle.getString("text");
        positionClicked = bundle.getInt("positionClicked");
       // songListForParcelable =   getActivity().getIntent().getParcelableExtra("songs");
        songListForParcelable = bundle.getParcelable("songs");
        songList = (ArrayList<Song>) songListForParcelable;

        data = new ArrayList<Data>();
        songView = (ListView)getActivity().findViewById(R.id.song_list);

        if(mLevel==0){
            if(positionInNavigation==2){
                //oncreate
              //  songList = new ArrayList<Song>();
                songList = new SongList();
                getSortedSongList(null);
                for(int i = 0; i<songList.size();i++){
                    Song s = songList.get(i);
                    data.add(s);
                }
            }
            else if(positionInNavigation == 1){ data = getSortedAlbums(null); }
            else if(positionInNavigation == 0){ data = getSortedArtists(); }
        }
        else if(mLevel == 1){
            if(positionInNavigation==0){//display albums from the artist clicked
               data = getSortedAlbums(text);
            }
            else if(positionInNavigation==1){//display songs from the album clicked.
                songList = new SongList();
                getSortedSongList(text);
                for(int i = 0; i<songList.size();i++){
                    Song s = songList.get(i);
                    data.add(s);
                }
             }
        }
        else if(mLevel == 2){
            if(positionInNavigation == 0){
                //display songs in the album clicked.
                songList = new SongList();
                getSortedSongList(text);
                for(int i = 0; i<songList.size();i++){
                    Song s = songList.get(i);
                    data.add(s);
                }
            }
            else if(positionInNavigation == 1){
               //play the song
                playTheSong(songList,positionClicked);
                 }

        }
        else if (mLevel ==3){
            if(positionInNavigation ==0){
                //play the song from the album
                playTheSong(songList,positionClicked);
              }
        }
        SongAdapter songAdt = new SongAdapter(getActivity(), data);
        songView.setAdapter(songAdt);
        songView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  itemClickedListener(view, positionInNavigation, position);
                if(positionInNavigation != 2){
                    mLevel++;
                    songClickListener.onSongClicked(view, positionInNavigation, mLevel, position, songList);
                }
                else{
                    //play the song, create new activity, visualize the song.
                    //posi=2,level 1; songlist and position
                    playTheSong(songList,position);
                    // posi = 1 level 2;
                    // po =0, level 3
                }

            }
        });
    }

    private void playTheSong(ArrayList<Song> songList, int position){
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putInt("position",position);
        SongList songListForParcel = (SongList) songList;
        b.putParcelable("songs", songListForParcel);
        i.putExtras(b);
        i.setClass(getActivity(),PlaySongActivity.class);
       // i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
       // startActivityForResult(i,0);
        startActivity(i);
    }

    private ArrayList<Data> getSortedArtists(){
        String[] columns = { MediaStore.Audio.Albums._ID,MediaStore.Audio.Albums.ARTIST };
        Cursor cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns, null, null, null);
        ArrayList<Data> artists= new ArrayList<Data>();
        if (cursor !=null && cursor.moveToFirst()) {
            do {
                long artists_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String artists_name = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                artists.add(new Artist(artists_id,artists_name));
            } while (cursor.moveToNext());
        }
        return artists;
    }

    public ArrayList<Data> getSortedAlbums(String artist){
        ArrayList<Data> albums= new ArrayList<Data>();
            String[] columns = { android.provider.MediaStore.Audio.Albums._ID,
                    android.provider.MediaStore.Audio.Albums.ALBUM,
                    MediaStore.Audio.Albums.ARTIST };
            Cursor cursor = getActivity().getContentResolver().query(
                    android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns, null,
                    null, null);
            if (cursor !=null && cursor.moveToFirst()) {
                if(artist == null){
                    do {
                        long album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                        String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                        String album_artist = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                        albums.add(new Album(album_id,album_name,album_artist));
                    } while (cursor.moveToNext());
                }
                else{
                    do {
                        long album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                        String album_artist = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                        if(album_artist.equals(artist)){
                            String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                            albums.add(new Album(album_id,album_name,album_artist));
                        }
                    } while (cursor.moveToNext());
                }
            }
        return albums;
        /* get songs that belong to an album.
        String[] column = { MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.MIME_TYPE, };

        String where = android.provider.MediaStore.Audio.Media.ALBUM + "=?";
        // String[] whereVal = new String[albums.size()];
        // whereVal =  albums.toArray(whereVal);
        String[] whereVal ={"Lights"};

        String orderBy = android.provider.MediaStore.Audio.Media.TITLE;
        cursor =getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,column, where, whereVal, orderBy);

        if (cursor.moveToFirst()) {
            do {
                Log.v("Vipul!!!!!!",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
            } while (cursor.moveToNext());
        }*/
    }

    public void getSortedSongList(String album) {

            //retrieve song info
            ContentResolver musicResolver = getActivity().getContentResolver();
            Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

            if(musicCursor!=null && musicCursor.moveToFirst()){
                //get columns
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ARTIST);

                if(album == null){
                    do {
                        long thisId = musicCursor.getLong(idColumn);
                        String thisTitle = musicCursor.getString(titleColumn);
                        String thisArtist = musicCursor.getString(artistColumn);
                        songList.add(new Song(thisId, thisTitle, thisArtist));
                    }
                    while (musicCursor.moveToNext());
                }
                else{
                    int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                    do {
                        String  albumInDB = musicCursor.getString(albumColumn);
                        if(album.equals(albumInDB)){
                            long thisId = musicCursor.getLong(idColumn);
                            String thisTitle = musicCursor.getString(titleColumn);
                            String thisArtist = musicCursor.getString(artistColumn);
                            songList.add(new Song(thisId, thisTitle, thisArtist));
                        }
                    }
                    while (musicCursor.moveToNext());


                }

            //  Collections.sort(songList, new Comparator<Song>(){
            //  public int compare(Song a, Song b){return a.getName().compareTo(b.getName());}});
        }

    }

    public static interface SongClickListener{
        public void onSongClicked(View view,int positionInNavigation, int level, int positionClicked, ArrayList<Song> songList);
    }
}
