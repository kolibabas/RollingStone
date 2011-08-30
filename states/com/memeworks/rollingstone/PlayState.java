package com.memeworks.rollingstone;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class PlayState implements GameState {
	
	private GameWorld world;
	private UIManager ui_manager;
	
	private boolean game_over;
	
	public PlayState() {
		game_over = false;
	}

	@Override
	public Canvas Draw(Canvas c) {
		world.Draw(c);
		ui_manager.Draw(c);
		return c;
	}

	@Override
	public void Frame_Started(float frame_time) {
		if (world.Player.Current_State == Util.PLAYER_STATE_DEAD)
		{
			game_over = true;
		}
		
		world.Frame_Started(frame_time);
		ui_manager.Frame_Started(frame_time, game_over);
	}

	@Override
	public boolean Touch_Event(MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN)
		{
			if (world.Player.Current_State == Util.PLAYER_STATE_DEAD)
			{
				Enter(); //Restart
			}
			else if (ui_manager.Jump_Button.contains((int)evt.getX(), (int)evt.getY()))
			{
				world.Jump_Down();
			}
			else if (ui_manager.Rock_Button.contains((int)evt.getX(), (int)evt.getY()))
			{
				world.Change_Player_Form(Util.COLOR_RED);
			}
			else if (ui_manager.Lava_Button.contains((int)evt.getX(), (int)evt.getY()))
			{
				world.Change_Player_Form(Util.COLOR_BLUE);
			}
			else if (ui_manager.Ice_Button.contains((int)evt.getX(), (int)evt.getY()))
			{
				world.Change_Player_Form(Util.COLOR_YELLOW);
			}
		}
		else if (evt.getAction() == MotionEvent.ACTION_UP)
		{
			world.Jump_Up();
		}
		return false;
	}

	@Override
	public void Enter() {
		game_over = false;
		world = new GameWorld();	
		ui_manager = new UIManager();
	}

	@Override
	public void Leave() {
		// TODO Auto-generated method stub
		
	}

}
