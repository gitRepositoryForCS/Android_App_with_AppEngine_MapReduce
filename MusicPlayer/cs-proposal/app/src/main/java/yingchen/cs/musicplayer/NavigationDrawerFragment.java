package yingchen.cs.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import yingchen.cis690.musicplayer.visualizer.Concepts.NamesInNavDrawer;

import com.firebase.client.Firebase;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;


/**
 * Navigation Drawer fragment class.
 */
public class NavigationDrawerFragment extends Fragment implements RecyclerViewAdapter.ClickListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private boolean mFinishFlag = false;    //private RecyclerTouchListener clicklistener;
    private NavigationDrawerFragment.ClickListener clickListener;

    public NavigationDrawerFragment() {
    }  //Required empty public constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // mUserLearnedDrawer = Boolean.valueOf(readToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            clickListener = (NavigationDrawerFragment.ClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), getData());
        recyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
/*
        // Get ListView object from xml
        final ListView listView = (ListView) getActivity().findViewById(R.id.listView);

        // Create a new Adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // Use Firebase to populate the list.
        Firebase.setAndroidContext(getActivity());

        // The onChildAdded event is typically used when retrieving a list of
        // items in the Firebase database.
        // the onChildAdded event is triggered once for each existing child
        // and then again every time a new child is added to the specified path
        new Firebase("https://project-for-music-player.firebaseio.com/todoItems")
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

*/
        return layout;
    }

    @Override
    public void itemClicked(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("position", position + "");
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        clickListener.onClick(view, position);   // Log.w("naviagationDrawerFragment", "item clicked!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public static List<NamesInNavDrawer> getData() {
        List<NamesInNavDrawer> data = new ArrayList<>();
        int[] icons = {R.drawable.artists, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};
        String[] titles = {"Artists", "Albums", "Songs", "Folders"};
        for (int i = 0; i < titles.length && i < icons.length; i++) {
            NamesInNavDrawer ds = new NamesInNavDrawer();
            ds.itemId = icons[i];
            ds.title = titles[i];
            data.add(ds);
        }
        return data;
    }

    public void onResume() {
        mFinishFlag = false;
        super.onResume();
    }

    public void onPause() {
        mFinishFlag = true;
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDetach() {
        super.onDetach();
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    //  saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);

        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readToPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView1, final ClickListener clickListener) {
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    //  return super.onSingleTapUp(e);
                    return true;// Log.w("navig Drawer fragment", "onSingleTapUp");
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    // super.onLongPress(e);
                    View child = recyclerView1.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView1.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }
}
