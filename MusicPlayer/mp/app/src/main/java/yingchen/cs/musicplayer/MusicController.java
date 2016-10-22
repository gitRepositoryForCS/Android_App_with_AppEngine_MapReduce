package yingchen.cs.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.MediaController;

/**
 * Created by yingchen on 6/10/15.
 */
public class MusicController extends MediaController{
    public MusicController(Context context) {
        super(context);
    }

    public void hide(){}
    @Override
    public boolean dispatchKeyEvent (KeyEvent event){
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
            ((Activity) getContext()).finish();

        return super.dispatchKeyEvent(event);
    }
}
