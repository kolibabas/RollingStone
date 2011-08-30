package com.memeworks.rollingstone;

import android.app.Activity;
import android.os.Bundle;

public class RollingStone extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Util.STATE_PLAY = new PlayState();
        Util.STATE_MENU = new MenuState();
        
        Util.STATE_CURRENT = Util.STATE_PLAY;
    }
    
    public static void Change_State(GameState state)
    {
    	Util.STATE_CURRENT.Leave();
    	Util.STATE_CURRENT = state;
    	Util.STATE_CURRENT.Enter();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
        RollingStone.this.finish();
    }
}

