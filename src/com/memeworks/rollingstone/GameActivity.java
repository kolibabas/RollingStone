package com.memeworks.rollingstone;

import com.memeworks.rollingstone.R;
import com.memeworks.rollingstone.MainView.MainThread;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class GameActivity extends Activity {

    /** A handle to the thread that's actually running the animation. */
    private MainThread main_thread;

    /** A handle to the View in which the game is running. */
    @SuppressWarnings("unused")
	private MainView main_view;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tell system to use the layout defined in the XML file
        setContentView(R.layout.main);

        // get handles to the MainView from XML, and its MainThread
        main_view = (MainView) findViewById(R.id.game);
        main_thread.doStart();
    } 
	
    /**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        //main_thread.setRunning(false);
    }
    
    /**
     * Standard override to get key-press events.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        
        return true;
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }

}
