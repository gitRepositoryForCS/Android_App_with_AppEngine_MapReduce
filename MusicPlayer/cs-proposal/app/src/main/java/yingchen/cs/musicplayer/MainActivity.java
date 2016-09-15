package yingchen.cs.musicplayer;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import yingchen.cis690.musicplayer.visualizer.Concepts.Song;
import yingchen.cis690.musicplayer.visualizer.Concepts.SongList;

import com.firebase.client.Firebase;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;

import android.widget.EditText;
import android.widget.Button;
import com.firebase.client.ValueEventListener;
import android.widget.AdapterView;

public class MainActivity extends ActionBarActivity implements
        NavigationDrawerFragment.ClickListener, SongsFragment.SongClickListener {

    private Toolbar toolbar;
    private NavigationDrawerFragment drawerFragment;
    private SongsFragment songsFragment;
    private boolean isSongClicked;
    private int mCurrentLevel;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(); //init app bar and navigation drawer fragment.



        //firebase storage
      //  FirebaseStorage storage = FirebaseStorage.getInstance();
       // StorageReference storageRef = storage.getReferenceFromUrl("gs://<your-bucket-name>");


        //firebase
        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);

        listView.setAdapter(adapter);

        Log.w("MainActivity onCreate", "before Firebase!!!!!!!!");
        // Use Firebase to populate the list.
        Firebase.setAndroidContext(this);

        // The onChildAdded event is typically used when retrieving a list of
        // items in the Firebase database.
        // the onChildAdded event is triggered once for each existing child
        // and then again every time a new child is added to the specified path
        new Firebase("https://project-musicPlayer.firebaseio.com/todoItems")
                .addChildEventListener(new ChildEventListener() {

                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Log.w("inside onChildAdded", "Firebase works!!!!!!!!!!!");
                        adapter.add((String) dataSnapshot.child("text").getValue());
                    }
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        adapter.remove((String) dataSnapshot.child("text").getValue());
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {}

                });




        // Add items via the Button and EditText at the bottom of the window.
        final EditText text = (EditText) findViewById(R.id.todoText);
        final Button button = (Button) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Firebase("https://project-musicPlayer.firebaseio.com/todoItems")
                        .push()
                        .child("text")
                        .setValue(text.getText().toString());
            }
        });


        // Delete items when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                new Firebase("https://project-musicPlayer.firebaseio.com/todoItems")
                        .orderByChild("text")
                        .equalTo((String) listView.getItemAtPosition(position))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                    firstChild.getRef().removeValue();
                                }
                            }
                            public void onCancelled(FirebaseError firebaseError) { }
                        });
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void init() {
        setContentView(R.layout.activity_main);
        //set app bar and navigation drawer
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), toolbar);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = this.getIntent().getExtras();
        initSongs(bundle);
    }
    private void initSongs(Bundle bundle){
        if (bundle != null && !isSongClicked) {
            int position = Integer.parseInt(bundle.getString("position"));
            addSongsFragmentLevelZero(position);
            Toast.makeText(getApplicationContext(), "MainActivity onResume inside if !!!!!!!", Toast.LENGTH_LONG).show();
        } else if (bundle != null) {
            // onSongClicked(View view, int positionInNavigation, int level, int positionClicked, ArrayList<Song> songList)
        /*  int  positionInNavigation = bundle.getInt("positionInNavigation");
          int  mLevel = bundle.getInt("level");
         // String  text = bundle.getString("text");
          int  positionClicked = bundle.getInt("positionClicked");
          ArrayList<Song> songList = (ArrayList<Song>) bundle.getParcelable("songs");
          ListView songView = (ListView)this.findViewById(R.id.song_list);
              Fragment f = getSupportFragmentManager().findFragmentByTag("onSongClicked");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.song_list_container,f);
            transaction.addToBackStack("onSongClicked");
            transaction.commit();
          */
            //  getFragmentManager().popBackStack();
            // getFragmentManager().popBackStackImmediate();
            getSupportFragmentManager().popBackStack();
            Toast.makeText(getApplicationContext(), "MainActivity onResume inside else !!!!!!!", Toast.LENGTH_LONG).show();
        } else {
            addSongsFragmentLevelZero(2);
        }
    }
    //navi drawer clickListener methods: onClick and onLongClick.
    @Override
    public void onClick(View view, int position) {
        addSongsFragmentLevelZero(position);
    }

    private void addSongsFragmentLevelZero(int positionInNavigation) {
        isSongClicked = false;
        songsFragment = new SongsFragment();
        mCurrentLevel = 0;
        Bundle bundle = new Bundle();
        bundle.putInt("positionInNavigation", positionInNavigation);
        bundle.putInt("level", 0);
        songsFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.song_list_container, songsFragment);
       // Toast.makeText(this, "in mainActivity addSongsFragmentLevelZero =)", Toast.LENGTH_LONG).show();
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLongClick(View view, int position) {}

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mCurrentLevel == 1) {
            onBackPressed();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        mCurrentLevel--;
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();// fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    //  listener. SongsFragment methods. handle the case when an item is clicked.
    @Override
    public void onSongClicked(View view, int positionInNavigation, int level, int positionClicked, ArrayList<Song> songList) {
        isSongClicked = true;
        View v = view.findViewById(R.id.song_title);
        TextView textView = (TextView) v;
        String text = textView.getText().toString();
        addSongClickedFragment(level, text, positionInNavigation, positionClicked, songList);
    }

    private void addSongClickedFragment(int level, String text, int positionInNavigation, int positionClicked, ArrayList<Song> songList) {
        songsFragment = new SongsFragment();
        mCurrentLevel = level;
        Bundle bundle = new Bundle();
        bundle.putInt("level", level);
        bundle.putString("text", text);
        bundle.putInt("positionInNavigation", positionInNavigation);
        bundle.putInt("positionClicked", positionClicked);
        SongList songListForParcelable = (SongList) songList;
        bundle.putParcelable("songs", songListForParcelable);
        songsFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.song_list_container, songsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //handle top right corner button.
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://yingchen.musicplayer/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://yingchen.musicplayer/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mSavedBundle!=null){
            outState = mSavedBundle;
        }
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        int positionInNavigation = savedInstanceState.getInt("positionInNavigation");
        int  mLevel = savedInstanceState.getInt("level");
        String text = savedInstanceState.getString("text");
        int positionClicked = savedInstanceState.getInt("positionClicked");
        ArrayList<Song> songList =  (ArrayList<Song>) savedInstanceState.getParcelable("songs");
        addSongClickedFragment(mLevel,text,positionInNavigation,positionClicked,songList);
        super.onRestoreInstanceState(savedInstanceState);
    }
    */
/*
        Intent intent = getIntent();
        int s1 = intent.getIntExtra("Check",0);

        if(s1==1)
        {
            s1 = 0;
            flag =true;
            Fragment fragment = new SongsFragment();
            if (fragment != null) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.song_list_container,fragment);
                //transaction.addToBackStack(null);
                transaction.commit();
            }
        } */