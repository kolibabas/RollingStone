package com.memeworks.rollingstone;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Wall {

	public int Color;
	public float Position_X;
	public int Position_Y;
	public int Width;
	public boolean Visible;
	
	private Bitmap image;
	
	public Wall()
	{
		Visible = false;
	}
	
	public void Show(int color, int position_x, int position_y)
	{
		Color = color;
		
		switch (Color)
		{
		case Util.COLOR_BLUE:
			image = Util.BMP_WALL_BLUE;
			break;
		case Util.COLOR_YELLOW:
			image = Util.BMP_WALL_YELLOW;
			break;
		case Util.COLOR_RED:
		default:
			image = Util.BMP_WALL_RED;
			break;				
		}
		
		Position_X = position_x;
		Position_Y = position_y - image.getHeight();
		Width = image.getWidth();
		
		Visible = true;
	}
	
	public Canvas Draw(Canvas c) {
		if (Visible)
		{
			c.drawBitmap(image, Position_X, Position_Y, null);
		}
		return c;
	}
	
}
