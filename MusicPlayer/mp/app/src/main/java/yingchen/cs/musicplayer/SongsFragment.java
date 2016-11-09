package yingchen.cs.musicplayer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

import yingchen.cs.musicplayer.visualizer.Concepts.Album;
import yingchen.cs.musicplayer.visualizer.Concepts.Artist;
import yingchen.cs.musicplayer.visualizer.Concepts.Data;
import yingchen.cs.musicplayer.visualizer.Concepts.Song;
import yingchen.cs.musicplayer.visualizer.Concepts.SongList;

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

    private String mDiscoverResult;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        songClickListener = (SongClickListener) activity;
    }

    /*parse the string result get from server to structured data
    * data is used to suply data displayed in listview.
    * */
    private void parseResult(ArrayList<Song> songList, String res){
         /* private long id;
            private String name;
            private String artist;
            	44, name	never8,	hotness	1, loudness	0.9
         * */
        Log.i("SongsFragment: ", "parseResult!!!");
        String[] arr = res.split("id");
        for(String str : arr){
            if(str.trim().length() == 0) continue;
            String[] a = str.trim().split(",");
            if(a.length != 4) continue;

            long id = Long.parseLong(a[0].trim());
            String name = a[1].trim().split("\t")[1];
            String artist = a[3].trim().split("\t")[1];
            songList.add(new Song(id, name, artist));
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("SongsFragment: ", "onActivityCreated!!!");
        Bundle bundle = getArguments();
        if(bundle==null)  return;
        mDiscoverResult = bundle.getString(FrontendConstant.DISCOVER_RESULT);
        songView = (ListView)getActivity().findViewById(R.id.music_list_view);
        data = new ArrayList<Data>();
        songListForParcelable = bundle.getParcelable(FrontendConstant.SONGS_FOR_PARCELABLE);
        songList = (ArrayList<Song>) songListForParcelable;
        if(songList == null) songList = new ArrayList<>();

        // when discover is clicked.
        if(mDiscoverResult != null){
            parseResult(songList, mDiscoverResult);
            for(int i = 0; i<songList.size();i++){
                Song s = songList.get(i);
                data.add(s);
            }
            SongAdapter songAdt = new SongAdapter(getActivity(), data);
            songView.setAdapter(songAdt);
            Log.i("songsFragement", "before return ");
            return;
        }
        Log.i("songsFragement", "after return ");
        //when other items are clicked.
        positionInNavigation = bundle.getInt(FrontendConstant.POSITION_IN_NAVI_DRAWER);
        mLevel = bundle.getInt(FrontendConstant.LEVEL);
        text = bundle.getString(FrontendConstant.TEXT);
        positionClicked = bundle.getInt(FrontendConstant.POSITION_CLICKED);
        if(mLevel==0){
            if(positionInNavigation==2){
                //oncreate
                songList = new SongList();
                getSortedSongList(null);
                for(int i = 0; i<songList.size();i++){
                    Song s = songList.get(i);
                    data.add(s);
                }
            }
            else if(positionInNavigation == 1){ data = getSortedAlbums(null);
                }
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
                else{ //play the song, create new activity, visualize the song.
                      //posi=2,level 1; songlist and position
                      // posi = 1 level 2; // po =0, level 3
                      playTheSong(songList,position);
                }

            }
        });
    }

    private void playTheSong(ArrayList<Song> songList, int position){
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putInt(FrontendConstant.POSITION_CLICKED,position);
        SongList songListForParcel = (SongList) songList;
        b.putParcelable(FrontendConstant.SONGS_FOR_PARCELABLE, songListForParcel);
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
