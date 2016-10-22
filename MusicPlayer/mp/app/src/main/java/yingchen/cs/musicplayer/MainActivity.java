package yingchen.cs.musicplayer;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import yingchen.cs.musicplayer.visualizer.Concepts.NamesInNavDrawer;
import yingchen.cs.musicplayer.visualizer.Concepts.Song;
import yingchen.cs.musicplayer.visualizer.Concepts.SongList;

public class MainActivity extends ActionBarActivity implements
        NavigationDrawerFragment.ClickListener, SongsFragment.SongClickListener, FetchData.RunAfterExecute {

    public static final String POSITION_IN_NAVI_DRAWER = "positionInNavigation";
    public static final String LEVEL = "level";
    public static final String TEXT = "text";
    public static final String POSITION_CLICKED = "positionClicked";
    public static final String SONGS_FOR_PARCELABLE = "songsForParcelable";
    public static final String DISCOVER_RESULT ="discoverResult";

    private static final String TAG = "Main_Activity";
    private static final String FIREBASE_PROJECT_ID = “your_firebase_project_id”;
    private static final String PACKAGE_NAME = “your_cloud_storage_package_name”;
    private static final String URL = “your_app_engine_url”;

    private Toolbar mToolbar;
    public NavigationDrawerFragment mDrawerFragment;
    private SongsFragment mSongsFragment;
    private boolean isSongClicked;
    private int mCurrentLevel;

    private  ListView mMusicListView;

    private static final String BUCKET_NAME = “your_appengine_bucket_name”;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(); //init app bar and navigation drawer fragment.
    }

    /* init app bar and navigation drawer fragment. */
    private void init() {
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), mToolbar);
    }

    /* navi drawer clickListener methods: onClick and onLongClick.*/
    @Override
    public void onClick(View view, int position) {
    }

    /* prepare for starting firebase connection. */
    private void startConnectFirebase() {
        client.connect();    // for firebase.
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
                Uri.parse("android-app://"+PACKAGE_NAME+ "/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.i(TAG, "onSaveInstanceState!!!!!!! ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.i(TAG, "onResume!!!!!!! " + mSuggestionFlag);
        Bundle bundle = this.getIntent().getExtras();
        if(mDrawerFragment.mSignedInState) mDrawerFragment.updateUI(false, true);
        initSongs(bundle);
    }

    /* init for the home screen.  */
    private void initSongs(Bundle bundle) {
        if (bundle != null && !isSongClicked) {
            int position = Integer.parseInt(bundle.getString(POSITION_IN_NAVI_DRAWER));
            addSongsFragmentLevelZero(position);
        } else if (bundle != null) {
            getSupportFragmentManager().popBackStack();
        } else {
            addSongsFragmentLevelZero(2);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // for firebase
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://"+ PACKAGE_NAME + "/http/host/path")
        );
        if (client != null) {
            AppIndex.AppIndexApi.end(client, viewAction);
            client.disconnect();
        }
    }

    /* init firebase for users to provide suggestions  */
    private void firebaseInit(LinearLayout suggestionLayout) {

        final ListView listView = (ListView) suggestionLayout.findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);

        // Use Firebase to populate the list.
        Firebase.setAndroidContext(this);
        // The onChildAdded event is typically used when retrieving a list of items in the Firebase database.
        // the onChildAdded event is triggered once for each existing child
        // and then again every time a new child is added to the specified path
        new Firebase(FIREBASE_PROJECT_ID)
                .addChildEventListener(new ChildEventListener() {

                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        adapter.add((String) dataSnapshot.child("text").getValue());
                    }

                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        adapter.remove((String) dataSnapshot.child("text").getValue());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }

                });

        // Add items via the Button and EditText at the bottom of the window.
        final EditText text = (EditText) suggestionLayout.findViewById(R.id.todoText);
        final Button button = (Button) suggestionLayout.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Firebase(FIREBASE_PROJECT_ID)
                        .push()
                        .child(MainActivity.TEXT)
                        .setValue(text.getText().toString());
                text.setText("");
                Toast.makeText(getApplicationContext(), "Comments sent successfully.", Toast.LENGTH_LONG).show();

            }
        });
        // Delete items when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                new Firebase(FIREBASE_PROJECT_ID)
                        .orderByChild(MainActivity.TEXT)
                        .equalTo((String) listView.getItemAtPosition(position))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                    firstChild.getRef().removeValue();
                                }
                            }

                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*   */
    private void addSongsFragmentLevelZero(int positionInNavigation) {

        // when suggestion is clicked in navigation drawer
        if (positionInNavigation == NavigationDrawerFragment.IS_SUGGESTION) {
            LinearLayout  parent = (LinearLayout) findViewById (R.id.song_list_container);
            LinearLayout suggestionLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.suggestion_layout, parent, false);
            firebaseInit(suggestionLayout);
            startConnectFirebase();
            parent.removeAllViews();
            parent.setBackgroundColor(Color.parseColor("#ffffff"));
            parent.addView(suggestionLayout);
        }else if(positionInNavigation == NavigationDrawerFragment.IS_DISCOVER){
            fetchData(positionInNavigation);
        } else {
            replaceFragment(positionInNavigation,null);
        }
    }

    // get data from server when user click Discover in Navigation drawer.
    private String fetchData(int positionInNavigation){
        //String idToken = getIntent().getExtras().getString(NavigationDrawerFragment.IDTOKEN);
        FetchData fd = new FetchData(this,positionInNavigation);
        fd.execute(new String[] {URL});
        return fd.getResult();
    }

    // from FetchData  RunAfterExecute interface.
    @Override
    public void onPost(int poitionInNavi, String res) {
        Log.i(TAG, "onPost result"+ res);
        replaceFragment(poitionInNavi,res);
    }

    // replace blank fragement with a new fragement depending on which position is clicked.
    public void replaceFragment(int positionInNavigation, String res){
        Log.i(TAG, "replaceFragment"+ positionInNavigation);
        isSongClicked = false;
        mSongsFragment = new SongsFragment();
        mCurrentLevel = 0;
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_IN_NAVI_DRAWER, positionInNavigation);
        if(res != null)  bundle.putString(DISCOVER_RESULT, res);
        bundle.putInt(LEVEL, mCurrentLevel);
        mSongsFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.song_list_container, mSongsFragment);
        transaction.addToBackStack(null);
        //transaction.commit();
        transaction.commitAllowingStateLoss();
        Log.i(TAG, "replaceFragment  after commit");
    }

    @Override
    public void onLongClick(View view, int position) {
        onClick(view, position);
    }

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
        mSongsFragment = new SongsFragment();
        mCurrentLevel = level;
        Bundle bundle = new Bundle();
        bundle.putInt(LEVEL, level);
        bundle.putString(TEXT, text);
        bundle.putInt(POSITION_IN_NAVI_DRAWER, positionInNavigation);
        bundle.putInt(POSITION_CLICKED, positionClicked);
        SongList songListForParcelable = (SongList) songList;
        bundle.putParcelable(SONGS_FOR_PARCELABLE, songListForParcelable);
        mSongsFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.song_list_container, mSongsFragment);
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

}

