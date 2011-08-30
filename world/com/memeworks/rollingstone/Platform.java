package com.memeworks.rollingstone;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Platform {
	
	public float Position_X;
	public int Position_Y;
	public int Pixel_Width;
	public int Height;
	public boolean Visible;
	public int Platform_Count;
	
	private Bitmap image;
	private Bitmap image_gen;
	private int platform_width;
	private int generator_index;
	private boolean has_wall;
	
	public Platform()
	{
		Visible = false;
		
		image = Util.BMP_PLATFORM;
		image_gen = Util.BMP_PLATFORM_GEN;
		Height = image.getHeight();
		platform_width = image.getWidth();
	}	
	
	public void Show(int position_y, int width, boolean wall_created)
	{
		Position_Y = position_y;
		Position_X = Util.SCREEN_WIDTH;
		Pixel_Width = width * platform_width;
		Platform_Count = width;
		generator_index = width / 2;
		has_wall = wall_created;
		
		Visible = true;
	}
	
	public Canvas Draw(Canvas c) {
		for (int i = 0; i < Platform_Count; i++)
		{
			if (has_wall && i == generator_index)
			{
				c.drawBitmap(image_gen, Position_X + (i * platform_width), Position_Y, null);
			}
			else
			{
				c.drawBitmap(image, Position_X + (i * platform_width), Position_Y, null);
			}
		}
		return c;
	}
		
}
