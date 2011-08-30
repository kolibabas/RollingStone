package com.memeworks.rollingstone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class MainView extends SurfaceView implements SurfaceHolder.Callback {

	class MainThread extends Thread {

        /** Handle to the surface manager object we interact with */
        private SurfaceHolder surface_holder;
        private boolean is_running;
   
        /** Frame Handler Variables */
        private float time_since_last_frame;
        private long time_of_last_frame;

		public MainThread(SurfaceHolder holder, Context context, Handler handler) {
            surface_holder = holder;
            
            // Load Resources here
            Resources res = context.getResources();
            
            //World Elements
            Util.BMP_PLAYER_RED = BitmapFactory.decodeResource(res, R.drawable.playerred);
            Util.BMP_PLAYER_BLUE = BitmapFactory.decodeResource(res, R.drawable.playerblue);
            Util.BMP_PLAYER_YELLOW = BitmapFactory.decodeResource(res, R.drawable.playeryellow);
            Util.BMP_PLATFORM = BitmapFactory.decodeResource(res, R.drawable.platform);
            Util.BMP_PLATFORM_GEN = BitmapFactory.decodeResource(res, R.drawable.platformgenerator);
            Util.BMP_WALL_RED = BitmapFactory.decodeResource(res, R.drawable.wallred);
            Util.BMP_WALL_BLUE = BitmapFactory.decodeResource(res, R.drawable.wallblue);
            Util.BMP_WALL_YELLOW = BitmapFactory.decodeResource(res, R.drawable.wallyellow);
            
            //UI Elements
            Util.BMP_BUTTON_JUMP_NRML = BitmapFactory.decodeResource(res, R.drawable.btnjumpnrml);
            Util.BMP_BUTTON_RED_NRML = BitmapFactory.decodeResource(res, R.drawable.playerred); //TODO Set real button icons
            Util.BMP_BUTTON_BLUE_NRML = BitmapFactory.decodeResource(res, R.drawable.playerblue);
            Util.BMP_BUTTON_YELLOW_NRML = BitmapFactory.decodeResource(res, R.drawable.playeryellow);
            
            Util.BMP_BUTTON_RED_BACK = BitmapFactory.decodeResource(res, R.drawable.btnredback);
            Util.BMP_BUTTON_BLUE_BACK = BitmapFactory.decodeResource(res, R.drawable.btnyellowback);
            Util.BMP_BUTTON_YELLOW_BACK = BitmapFactory.decodeResource(res, R.drawable.btnblueback);
            
		}

        @Override
        public void run() {
            while (is_running) {
            	
                Canvas c = null;
                try {
                	synchronized(TouchEventMutex) {
                		TouchEventMutex.notify();
                	}
                	Thread.yield();
                	
                    c = surface_holder.lockCanvas();
                    synchronized (surface_holder) {
                    	Frame_Started();
                    	Draw(c);
                    }
                }
                catch (Exception e) {
                	Log.e("Exception", e.getStackTrace().toString());
                } 
                finally {
                    if (c != null) {
                        surface_holder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
   
		private void Draw(Canvas c) {
			c.drawColor(0, PorterDuff.Mode.CLEAR);

			Util.STATE_CURRENT.Draw(c);
		}

		/**
         * Starts the game
         */
        public void doStart() {
        	is_running = true;
            RollingStone.Change_State(Util.STATE_PLAY);
        }
        
        /**
         * Handles pretty much all of the game play 
         * Optimized to reduce object allocation where possible, but it's ugly
         */
        private void Frame_Started() {
        	//Calculate frame time
            long now = System.currentTimeMillis();
            if (time_of_last_frame > now) return;
            time_since_last_frame = (now - time_of_last_frame) / 1000.0f;
            time_of_last_frame = now;
            
            if (time_since_last_frame > 100000)
            {
            	time_since_last_frame = 0.0000f;
            }
            
            Util.STATE_CURRENT.Frame_Started(time_since_last_frame);
        }

		//Input Handlers
		public boolean doTouchEvent(MotionEvent evt) {
			Util.STATE_CURRENT.Touch_Event(evt);			
			return true;
		}

		/* Callback invoked when the surface dimensions change. */
		public void setSurfaceSize(int width, int height) {
            synchronized (surface_holder) {
            	Util.SCREEN_WIDTH = width;
            	Util.SCREEN_HEIGHT = height;
            	Util.SCREEN_CENTER_X = width / 2;
            	Util.SCREEN_CENTER_Y = height / 2;
            	
            	Util.PLATFORM_HEIGHT_MIN = (int) (height * 0.25f);
            	Util.PLATFORM_HEIGHT_MAX = (int) (height * 0.75f);
  
                doStart();
            }
		}

    };
    
    /** The thread that actually draws the animation */
    private MainThread thread;
    
    /** Mutex object for touch thread */
    private Object TouchEventMutex = new Object();
    
    public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // create thread only; it's started in surfaceCreated()
        thread = new MainThread(holder, context, new Handler() {
        	@Override
            public void handleMessage(Message m) {
        		
            }
        });

        // make sure we get key events
        setFocusableInTouchMode(true);
        setFocusable(true); 
	}
 
    
    /**
     * Fetches the animation thread corresponding to this MainView.
     * 
     * @return the animation thread
     */
    public MainThread getThread() {
        return thread;
    }

    /**
     * Standard override for touch events
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent evt) {
    	thread.doTouchEvent(evt);
    	
    	synchronized(TouchEventMutex) {
    		try {
    			TouchEventMutex.wait(1000L);
    		}
    		catch (InterruptedException e) {}
    	}
    	return true;
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
        	thread.is_running = false;
        }
    }

    
    /* Callback invoked when the surface dimensions change. */
	public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {
		thread.setSurfaceSize(width, height);
	}

	
	/*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
	public void surfaceCreated(SurfaceHolder holder) {
		// Start the thread here so that we don't busy-wait in run()
        // Waiting for the surface to be created
		thread.start();
	}

	
    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Tell thread to shut down & wait for it to finish
        boolean retry = true;
        
        while (retry) {
            try {
                thread.join();
                retry = false;
            } 
            catch (InterruptedException e) {}
        }
	}
}

