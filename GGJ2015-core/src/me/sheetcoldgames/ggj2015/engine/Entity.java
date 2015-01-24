package me.sheetcoldgames.ggj2015.engine;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Entity {
public int id;
	
	public Vector2 position;
	public Vector2 velocity;
	
	public float width;
	public float height;
	
	/* How much of the player will stick out of the wall, must be positive */
	public float offset;
	
	public float accel;
	
	public float maxSpeed;
	public float minSpeed;
	
	public float stateTime = 0f;
	
	public boolean movingUp = false;
	public boolean movingRight = true;
	
	public ACTION action;
	public DIRECTION horizontalDir;
	public DIRECTION verticalDir;
	
	public ArrayList<Vector2> followPoints;
	
	public Entity() {
		position = new Vector2();
		velocity = new Vector2();
		width = .8f;
		maxSpeed = 1f;
		height = 1f;
		offset = .2f;
		minSpeed = offset / 4f;
		
		accel = 1.f;
		
		action = ACTION.IDLE;
		horizontalDir = DIRECTION.RIGHT;
		verticalDir = DIRECTION.DOWN;
		
		followPoints = new ArrayList<Vector2>();
	}
	

}
