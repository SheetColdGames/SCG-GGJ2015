package me.sheetcoldgames.ggj2015;

public class Constants {
	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 480;
	public static final String WINDOW_TITLE = "G976";
	
	public static final int TILE_SIZE = 16;
	public static final float SCALE = 2;
	
	public static final float VIEWPORT_WIDTH = WINDOW_WIDTH / (TILE_SIZE * SCALE);
	public static final float VIEWPORT_HEIGHT= WINDOW_HEIGHT / (TILE_SIZE * SCALE);
	
	public static final int INPUT_ARROWS = 1;
	public static final int INPUT_WASD = 2;
	
	public static final String PHYSICAL_MAP = "map.fis";
	
	// ======= ALL RELATED TO ENTITIES =======
	
	public static final int GIRL_ID = 1;
	public static final float GIRL_WALK_SPEED = .14f;
	public static final float GIRL_WIDTH = 1f;
	public static final float GIRL_HEIGHT = 1.5f;
	
	public static final int ROBOT_ID = 2;
	public static final float ROBOT_WALK_SPEED = .12f;
	public static final float ROBOT_WIDTH = 2f;
	public static final float ROBOT_HEIGHT = 2.5f;
	
	// 
	public static final int CHILDREN_AI_ID = 3;
	public static final float CHILDREN_AI_WALK_SPEED = .18f;
	public static final float CHILDREN_AI_WIDTH = 1.f;
	public static final float CHILDREN_AI_HEIGHT = 1.5f;
	
	public static final int ENEMY_AI_ID = 3;
	public static final float ENEMY_AI_WALK_SPEED = .09f;
	public static final float ENEMY_AI_WIDTH = 1.5f;
	public static final float ENEMY_AI_HEIGHT = 2f;
}
