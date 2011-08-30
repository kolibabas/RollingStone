package com.memeworks.rollingstone;

import java.util.Random;

import android.graphics.Canvas;

public class GameWorld {
	
	public PlayerBall Player;
	private Platform[] platforms = new Platform[3];
	private Wall[] walls = new Wall[3];
	private int current_platform_index;
	private int current_wall_index;
	private int current_floor_y;
	private int death_y;
	private float world_scroll_rate;
	private float wall_chance;
	private float wall_chance_timer;
	private int max_dead_space;
	private Random random;
	
	public GameWorld()
	{
		Player = new PlayerBall();
		world_scroll_rate = Util.WORLD_SCROLL_RATE_INIT;
		wall_chance = 0.65f;
		wall_chance_timer = 0.0f;
		death_y = Util.SCREEN_HEIGHT + Util.BMP_PLAYER_RED.getHeight();
		current_platform_index = 0;
		current_wall_index = 0;
		
		random = new Random();
		max_dead_space = random.nextInt(Util.DEAD_SPACE_MAX) + Util.DEAD_SPACE_MIN;
		
		platforms[0] = new Platform();
		platforms[1] = new Platform();
		platforms[2] = new Platform();
		
		walls[0] = new Wall();
		walls[1] = new Wall();
		walls[2] = new Wall();
		
		Create_Initial_Platform();
	}
	
	public Canvas Draw(Canvas c) {
		Player.Draw(c);
		
		for (int i = 0; i < 3; i++)
		{
			if (platforms[i].Visible)
			{
				platforms[i].Draw(c);
			}
			
			if (walls[i].Visible)
			{
				walls[i].Draw(c);
			}
		}
		return c;
	}

	public void Frame_Started(float frame_time) {
		
		//Caps wall chance at 90%
		if (wall_chance <= 0.90f)
		{
			wall_chance_timer += frame_time;
			if (wall_chance_timer > 2.5f)
			{
				wall_chance += 0.01;
				wall_chance_timer = 0;
			}
		}
		
		world_scroll_rate += frame_time * 2.5f;
		Player.Roll_Rate = world_scroll_rate;
		
		Process_Platforms();
		boolean platform_under = Calculate_Floor();
		
		//Move Platforms + Walls
		if (Player.Current_State == Util.PLAYER_STATE_ROLLING)
		{
			for (int i = 0; i < 3; i++)
			{
				if (platforms[i].Visible)
				{
					platforms[i].Position_X -= world_scroll_rate * frame_time;
					
					if ( (platforms[i].Position_X + platforms[i].Pixel_Width) <= 0)
					{
						platforms[i].Visible = false;
					}
				}
			}
			
			for (int i = 0; i < 3; i++)
			{
				if (walls[i].Visible)
				{
					walls[i].Position_X -= world_scroll_rate * frame_time;
					
					if ( (walls[i].Position_X + walls[i].Width) <= 0)
					{
						walls[i].Visible = false;
					}
				}
			}
		}
		
		//Animate Player
		Player.Frame_Started(frame_time, current_floor_y, platform_under);
		if (Player.Position_Y > Util.SCREEN_HEIGHT)
		{
			Player.Current_State = Util.PLAYER_STATE_DEAD;
		}
		
		Check_Wall_Collision();
	}
	
	public void Jump_Down()
	{
		Player.Jump_Started();
	}
	
	public void Jump_Up() {
		Player.Jump_Finished();		
	}
	
	public void Change_Player_Form(int form)
	{
		Player.Change_Form(form);
	}
	
	private void Create_Initial_Platform()
	{
		platforms[current_platform_index].Show(Util.SCREEN_CENTER_Y, 7, false);
		platforms[current_platform_index].Position_X = 0; //Only initial platform starts at X = 0 
	}
	
	private boolean Calculate_Floor()
	{
		//Determine floor under player
		current_floor_y = death_y;
		boolean platform_under = false;
		for (int i = 0; i < 3; i++)
		{
			if (platforms[i].Visible &&
				//Right side collision
			   (Player.Position_X + Player.Width >= platforms[i].Position_X &&
				Player.Position_X + Player.Width <= platforms[i].Position_X + platforms[i].Pixel_Width) ||
				
				//Left side collision
			   (Player.Position_X >= platforms[i].Position_X &&
				Player.Position_X <= platforms[i].Position_X + platforms[i].Pixel_Width))
			{
				current_floor_y = platforms[i].Position_Y;
				platform_under = true;
				break;
			}
		}
		
		if (Player.Airborne && Player.Position_Y > current_floor_y)
		{
			current_floor_y = death_y;
		}
		
		return platform_under;
	}
	
	private void Process_Platforms()
	{
		//Determine if new platform should be created
		int right_edge = 0;
		for (int i = 0; i < 3; i++)
		{
			//Find furthest right visible platform
			if (platforms[i].Visible &&
				platforms[i].Position_X + platforms[i].Pixel_Width > right_edge)
			{
				right_edge = (int) (platforms[i].Position_X + platforms[i].Pixel_Width);
			}
		}
		
		if (Util.SCREEN_WIDTH - right_edge > max_dead_space)
		{
			//Determine wall creation
			boolean wall_created = false;
			if (random.nextFloat() < wall_chance)
			{
				wall_created = true;
			}
			
			int new_height = random.nextInt(Util.PLATFORM_HEIGHT_MAX) + Util.PLATFORM_HEIGHT_MIN;
			
			//Limit higher platform spawns to PLATFORM_MAX_DIFFERENTIAL units above
			if (new_height < platforms[current_platform_index].Position_Y - Util.PLATFORM_MAX_DIFFERENTIAL)
			{
				new_height = platforms[current_platform_index].Position_Y - Util.PLATFORM_MAX_DIFFERENTIAL;
			}
			
			current_platform_index = (current_platform_index + 1) % 3;
			int next_plat_width = random.nextInt(Util.PLATFORM_WIDTH_MAX) + Util.PLATFORM_WIDTH_MIN;
			if (next_plat_width % 2 == 0)
			{
				next_plat_width--;
			}
			platforms[current_platform_index].Show(new_height, next_plat_width, wall_created);
			
			if (wall_created)
			{
				current_wall_index = (current_wall_index + 1) % 3;
				int wall_pos_x = Util.SCREEN_WIDTH + (platforms[current_platform_index].Pixel_Width / 2);
				wall_pos_x -= Util.BMP_WALL_RED.getWidth() / 2;
				walls[current_wall_index].Show(random.nextInt(Util.COLOR_COUNT), wall_pos_x, platforms[current_platform_index].Position_Y);
			}	
			
			max_dead_space = random.nextInt(Util.DEAD_SPACE_MAX) + Util.DEAD_SPACE_MIN;
		}
	}
	
	private void Check_Wall_Collision()
	{
		//Check Wall collision
		for (int i = 0; i < 3; i++)
		{
			if (walls[i].Visible)
			{
				if (Player.Collision_X > walls[i].Position_X &&
					Player.Collision_X < (walls[i].Position_X + walls[i].Width))
				{
					//Collision occurred
					if(Player.Current_Color ==walls[i].Color)
					{
						walls[i].Visible = false;
					}
					else
					{
						Player.Current_State = Util.PLAYER_STATE_DEAD;
					}

				}
			}
		}
	}


	
}
