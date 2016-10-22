package yingchen.cs.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.MediaController;

import java.util.ArrayList;

import yingchen.cs.musicplayer.visualizer.Concepts.Data;
import yingchen.cs.musicplayer.visualizer.Concepts.Song;
import yingchen.cs.musicplayer.visualizer.Concepts.SongList;
import yingchen.cs.musicplayer.visualizer.VisualizerView;
import yingchen.cs.musicplayer.visualizer.renderer.BarGraphRenderer;
import yingchen.cs.musicplayer.visualizer.renderer.CircleBarRenderer;
import yingchen.cs.musicplayer.visualizer.renderer.CircleRenderer;
import yingchen.cs.musicplayer.visualizer.renderer.LineRenderer;
import yingchen.cs.musicplayer.utils.TunnelPlayerWorkaround;
import android.media.MediaPlayer;

public class PlaySongActivity extends ActionBarActivity implements MediaController.MediaPlayerControl {
    private ArrayList<Song> songList;
    private FrameLayout songView;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private MusicController controller;
    private boolean paused=false, playbackPaused=false;

    private int position;
    private SongList mSongList;
    private Toolbar toolbar;

    private MediaPlayer mSilentPlayer;  /* to avoid tunnel player issue */
    private VisualizerView mVisualizerView;

    private ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            musicSrv.setList(songList);
            musicBound = true;

            setController();
               songView = (FrameLayout)findViewById(R.id.controller);
            ArrayList<Data> data = new ArrayList<Data>();
            for(int i = 0; i<songList.size();i++){
                Song s = songList.get(i);
                data.add(s);
            }

           //  SongAdapter songAdt = new SongAdapter(getApplicationContext(), data);
            // songView.setAdapter(songAdt);

            //from songPicked
            musicSrv.setSong(position);
            musicSrv.playSong();
            if(playbackPaused){
                setController();
                playbackPaused=false;
            }
            controller.show(getDuration());
            mVisualizerView = (VisualizerView) findViewById(R.id.visualizerViewMain);
            mVisualizerView.link(musicSrv.player);
            addLineRenderer();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        //set app bar and navigation drawer
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get songList, so musicService can setList.
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
        Intent i = this.getIntent();
        mSongList =   this.getIntent().getParcelableExtra(MainActivity.SONGS_FOR_PARCELABLE);
        songList = (ArrayList<Song>) mSongList;

      //  songList=new ArrayList<Song>();
        Bundle b = this.getIntent().getExtras();
        position = b.getInt(MainActivity.POSITION_CLICKED);
    }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
      onBackPressed();
      return super.onKeyUp(keyCode, event);
  }
    @Override
    public void onBackPressed() {
       android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

      //  unbindService(musicConnection);

        if (fm.getBackStackEntryCount() > 0) {
           // fm.popBackStack();
            fm.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
        cleanUp();

    }
    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
        initTunnelPlayerWorkaround();
        //init();
    }
    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }
    private void setController(){
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.controller));
        controller.setEnabled(true);
    }
    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(getDuration());
    }
    //play previous
    private void playPrev(){
        musicSrv.playPrev();

        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(getDuration());
    }
    @Override
    protected void onDestroy() {
        stopService(playIntent);
        cleanUp();
        musicSrv=null;
        super.onDestroy();
    }
    @Override
    public void start() {musicSrv.go();}
    @Override
    public void pause() {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }
    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }
    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }
    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }
    @Override
    public int getBufferPercentage() {
        return 0;
    }
    @Override
    public boolean canPause() {
        return true;
    }
    @Override
    public boolean canSeekBackward() {
        return true;
    }
    @Override
    public boolean canSeekForward() {
        return true;
    }
    @Override
    public int getAudioSessionId() {
        return 0;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_song, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id) {
            case R.id.circle:
              addCircleRenderer();
                break;
            case R.id.bar:
               addBarGraphRenderers();
                break;
            case R.id.circlebar:
                addBarGraphRenderers();
                break;

        }

        return super.onOptionsItemSelected(item);
    }



    private void cleanUp()
    {
        if (musicSrv.cleanUp())
        {
            mVisualizerView.release();
        }

    }
    // Workaround (for Galaxy S4)
    //
    // "Visualization does not work on the new Galaxy devices"
    //    https://github.com/felixpalmer/android-visualizer/issues/5
    //
    // NOTE:
    //   This code is not required for visualizing default "test.mp3" file,
    //   because tunnel player is used when duration is longer than 1 minute.
    //   (default "test.mp3" file: 8 seconds)
    //
    private void initTunnelPlayerWorkaround() {
        // Read "tunnel.decode" system property to determine
        // the workaround is needed
        if (TunnelPlayerWorkaround.isTunnelDecodeEnabled(this)) {
            mSilentPlayer = TunnelPlayerWorkaround.createSilentMediaPlayer(this);
        }
    }
    // Methods for adding renderers to visualizer
    private void addBarGraphRenderers()
    {
        Paint paint = new Paint();
        paint.setStrokeWidth(50f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 56, 138, 252));
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(16, paint, false);
        mVisualizerView.addRenderer(barGraphRendererBottom);

        Paint paint2 = new Paint();
        paint2.setStrokeWidth(12f);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.argb(200, 181, 111, 233));
        BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(4, paint2, true);
        mVisualizerView.addRenderer(barGraphRendererTop);
    }

    private void addCircleBarRenderer()
    {
        Paint paint = new Paint();
        paint.setStrokeWidth(8f);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleBarRenderer circleBarRenderer = new CircleBarRenderer(paint, 32, true);
        mVisualizerView.addRenderer(circleBarRenderer);
    }

    private void addCircleRenderer()
    {
        Paint paint = new Paint();
        paint.setStrokeWidth(3f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleRenderer circleRenderer = new CircleRenderer(paint, true);
        mVisualizerView.addRenderer(circleRenderer);
    }

    private void addLineRenderer()
    {
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(1f);
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.argb(88, 0, 128, 255));

        Paint lineFlashPaint = new Paint();
        lineFlashPaint.setStrokeWidth(5f);
        lineFlashPaint.setAntiAlias(true);
        lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
        LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint, true);
        mVisualizerView.addRenderer(lineRenderer);
    }
   /* public void startPressed(View view) throws IllegalStateException, IOException
    {
        if(mPlayer.isPlaying())
        {
            return;
        }
        mPlayer.prepare();
        mPlayer.start();
    }*/
    public void stopPressed(View view)
    {
      //  mPlayer.stop();
    }
    public void barPressed(View view)
    {
        addBarGraphRenderers();
    }
    public void circlePressed(View view)
    {
        addCircleRenderer();
    }
    public void circleBarPressed(View view)
    {
        addCircleBarRenderer();
    }
    public void linePressed(View view)
    {
        addLineRenderer();
    }

    public void clearPressed(View view)
    {
        mVisualizerView.clearRenderers();
    }
}
