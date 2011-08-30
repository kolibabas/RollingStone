package com.memeworks.rollingstone;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class PlayerBall {
	
	public int Current_Color;
	public int Current_State;
	public boolean Airborne;
	public Bitmap image;
	
	public int Position_X;
	public int Position_Y;
	public int Collision_X;
	public int Collision_Y;
	public int Width;
	
	private int radius;	
	private float current_rotation;
	private float vertical_rate;
	private float falling_time;
	private float hang_time;
	private boolean jump_held;
	
	public float Roll_Rate;
	private final float GRAVITY = 9.8f;
	
	public PlayerBall()
	{
		Current_Color = Util.COLOR_RED;
		Current_State = Util.PLAYER_STATE_ROLLING;
		Airborne = true;
		Roll_Rate = 0;
		current_rotation = 0.0f;
		vertical_rate = 0.0f;
		falling_time = 1.0f;
		hang_time = 0.0f;
		jump_held = false;
		
		image = Util.BMP_PLAYER_RED;
		radius = Util.BMP_PLAYER_RED.getWidth() / 2;
		Width = Util.BMP_PLAYER_RED.getWidth();
		Position_X = 6 * radius;
		Collision_X = Position_X + Width;
	}
	
	public void Change_Form(int form)
	{
		Current_Color = form;
		
		switch (form)
		{
		case Util.COLOR_BLUE:
			image = Util.BMP_PLAYER_BLUE;
			break;
		case Util.COLOR_YELLOW:
			image = Util.BMP_PLAYER_YELLOW;
			break;		
		case Util.COLOR_RED:
		default:
			image = Util.BMP_PLAYER_RED;
			break;
		}
	}
	
	public void Jump_Started()
	{
		if (!Airborne)
		{
			vertical_rate = -7.0f; //Initial rate decays slower if held
			jump_held = true;
			Airborne = true;
		}
	}
	
	public void Jump_Finished()
	{
		jump_held = false;
	}
	
	public Canvas Draw(Canvas c) {
		c.save();
		c.rotate(current_rotation, Position_X + radius, Position_Y + radius);
		c.drawBitmap(image, Position_X, Position_Y, null);
		c.restore();
		
		return c;
	}

	public void Frame_Started(float frame_time, int current_floor, boolean platform_under) {
		
		if (Current_State == Util.PLAYER_STATE_ROLLING)
		{
			if (Airborne ||
				Collision_Y	!= current_floor)
			{
				Compute_Vertical(frame_time, current_floor, platform_under);
			}
			
			current_rotation = (current_rotation + (Roll_Rate * frame_time) % 360);
		}
	}
	
	private void Compute_Vertical(float frame_time, int current_floor, boolean platform_under)
	{
		if (!platform_under)
		{
			Airborne = true;
		}
		
		if (jump_held)
		{
			hang_time += frame_time; 
			
			if (hang_time >= 0.5)
			{
				jump_held = false;
				hang_time = 0.0f;
			}
		}
		else
		{
			falling_time += frame_time;
			vertical_rate += GRAVITY * falling_time * frame_time;
		}

		Position_Y += vertical_rate;
		Collision_Y = Position_Y + (radius * 2);
		
		if (Collision_Y >= current_floor &&
			platform_under)
		{
			Position_Y = current_floor - (radius * 2);
			Collision_Y = Position_Y + (radius * 2);
			
			Airborne = false;
			vertical_rate = 0;
			falling_time = 1.0f;
		}
	}

}
