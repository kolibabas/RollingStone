package com.memeworks.rollingstone;

import android.graphics.Bitmap;

public class Util {
	
	//Global Variables
    public static int SCREEN_WIDTH;
	public static int SCREEN_CENTER_X;
	public static int SCREEN_HEIGHT;
	public static int SCREEN_CENTER_Y;
	
	public static int PLATFORM_HEIGHT_MIN;
	public static int PLATFORM_HEIGHT_MAX;
	
	public static GameState STATE_CURRENT;
	public static PlayState STATE_PLAY;
	public static MenuState	STATE_MENU;
	
	public static Bitmap BMP_PLAYER_RED;
	public static Bitmap BMP_PLAYER_BLUE;
	public static Bitmap BMP_PLAYER_YELLOW;
	
	public static Bitmap BMP_WALL_RED;
	public static Bitmap BMP_WALL_BLUE;
	public static Bitmap BMP_WALL_YELLOW;
	
	public static Bitmap BMP_PLATFORM;
	public static Bitmap BMP_PLATFORM_GEN;
	
	public static Bitmap BMP_BUTTON_JUMP_NRML;
	public static Bitmap BMP_BUTTON_RED_NRML;
	public static Bitmap BMP_BUTTON_BLUE_NRML;
	public static Bitmap BMP_BUTTON_YELLOW_NRML;
	public static Bitmap BMP_BUTTON_JUMP_PRSD;
	public static Bitmap BMP_BUTTON_RED_PRSD;
	public static Bitmap BMP_BUTTON_BLUE_PRSD;
	public static Bitmap BMP_BUTTON_YELLOW_PRSD;
	public static Bitmap BMP_BUTTON_RED_BACK;
	public static Bitmap BMP_BUTTON_BLUE_BACK;
	public static Bitmap BMP_BUTTON_YELLOW_BACK;
	
	//Global Constants
	public static final int DEAD_SPACE_MIN = 75;
	public static final int DEAD_SPACE_MAX = 150;
	
	public static final int PLATFORM_WIDTH_MIN = 3;
	public static final int PLATFORM_WIDTH_MAX = 7;
	
	public static final int PLATFORM_MAX_DIFFERENTIAL = 150;
	
	public static final int WORLD_SCROLL_RATE_INIT = 300;

		
	//Enums
	public static final int COLOR_RED = 0;
	public static final int COLOR_BLUE = 1;
	public static final int COLOR_YELLOW  = 2;
	public static final int COLOR_COUNT = 3;
	
	public static final int PLAYER_STATE_ROLLING = 0;
	public static final int PLAYER_STATE_DYING = 1;
	public static final int PLAYER_STATE_DEAD = 2;

}
