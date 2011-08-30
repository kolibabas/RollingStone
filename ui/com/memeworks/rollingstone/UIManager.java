package com.memeworks.rollingstone;

import com.memeworks.rollingstone.Util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


public class UIManager {
	public Rect Jump_Button;
	public Rect Rock_Button;
	public Rect Lava_Button;
	public Rect Ice_Button;
	
	//Game Timer
	private float game_time_elapsed;
	private StringBuilder game_time_text = new StringBuilder();
	private final char game_time_chars[];
	private Paint game_time_paint;
	private float game_time_text_size;
	
	//Distance text
	private float world_scroll_rate;
	private float distance_travelled;
	private StringBuilder distance_text = new StringBuilder();
	private final char distance_chars[];
	private Paint distance_paint;
	private float distance_text_size;

	public UIManager() {
		//Mode Text
		game_time_elapsed = 0;
		game_time_text = new StringBuilder();
		game_time_chars = new char[100];
		game_time_paint = new Paint();
		game_time_text_size = 21;
		
		game_time_paint.setARGB(255, 255, 255, 255);
		//game_time_paint.setTypeface(); //TODO Find/set game text font
		game_time_paint.setTextSize(game_time_text_size);
		game_time_paint.setTextAlign(Paint.Align.LEFT);
		game_time_paint.setAntiAlias(true);
		
		//Distance Text
		world_scroll_rate = Util.WORLD_SCROLL_RATE_INIT;
		distance_travelled = 0;
		distance_text = new StringBuilder();
		distance_chars = new char[100];
		distance_paint = new Paint();
		distance_text_size = 21;
		
		distance_paint.setARGB(255, 255, 255, 255);
		//game_time_paint.setTypeface(); //TODO Find/set game text font
		distance_paint.setTextSize(distance_text_size);
		distance_paint.setTextAlign(Paint.Align.LEFT);
		distance_paint.setAntiAlias(true);
		
		Jump_Button = new Rect(0, 
							   Util.SCREEN_HEIGHT - Util.BMP_BUTTON_JUMP_NRML.getHeight(),
							   Util.BMP_BUTTON_JUMP_NRML.getWidth(),
							   Util.SCREEN_HEIGHT);
		
		Rock_Button = new Rect(
								Util.SCREEN_WIDTH - Util.BMP_BUTTON_RED_NRML.getWidth(),
								(int) (Util.SCREEN_HEIGHT * 0.25f - Util.BMP_BUTTON_RED_NRML.getHeight()), 
								Util.SCREEN_WIDTH,
								(int) (Util.SCREEN_HEIGHT * 0.25f) 
							  );
		
		Lava_Button = new Rect(
								Util.SCREEN_WIDTH - Util.BMP_BUTTON_BLUE_NRML.getWidth(), 
								(int) (Util.SCREEN_HEIGHT * 0.50f - Util.BMP_BUTTON_BLUE_NRML.getHeight()), 
								Util.SCREEN_WIDTH, 
								(int) (Util.SCREEN_HEIGHT * 0.50f) 
							  );
		
		Ice_Button = new Rect(
								Util.SCREEN_WIDTH - Util.BMP_BUTTON_YELLOW_NRML.getWidth(),
								(int) (Util.SCREEN_HEIGHT * 0.75f - Util.BMP_BUTTON_YELLOW_NRML.getHeight()),
								Util.SCREEN_WIDTH, 
								(int) (Util.SCREEN_HEIGHT * 0.75f)
							 );
	}
	
	public Canvas Draw(Canvas c) {
		//Buttons
		c.drawBitmap(Util.BMP_BUTTON_JUMP_NRML, Jump_Button.left, Jump_Button.top, null);
		c.drawBitmap(Util.BMP_BUTTON_RED_NRML, Rock_Button.left, Rock_Button.top, null);
		c.drawBitmap(Util.BMP_BUTTON_BLUE_NRML, Lava_Button.left, Lava_Button.top, null);
		c.drawBitmap(Util.BMP_BUTTON_YELLOW_NRML, Ice_Button.left, Ice_Button.top, null);
		
		//Game Text
		game_time_text.getChars(0, game_time_text.length(), game_time_chars, 0);
		c.drawText(game_time_chars, 0, game_time_text.length(), 7, 23, game_time_paint);
		
		//Distance Text
		distance_text.getChars(0, distance_text.length(), distance_chars, 0);
		c.drawText(distance_chars, 0, distance_text.length(), 7, 60, distance_paint);
		
		return c;
	}

	public void Frame_Started(float frame_time, boolean game_over) {
		if (!game_over)
		{
			//Game Timer
			game_time_elapsed += frame_time;
			game_time_text.setLength(0);
			game_time_text.append("Time: " + (int)game_time_elapsed + " seconds");
			
			//Distance Text
			world_scroll_rate += frame_time * 2.5f;
			distance_travelled += world_scroll_rate * frame_time;
			distance_text.setLength(0);
			distance_text.append("Distance: " + (int)(distance_travelled / 100.0f)+ " meters");
		}
	}	
	
}
