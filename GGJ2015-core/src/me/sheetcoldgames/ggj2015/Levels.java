package me.sheetcoldgames.ggj2015;

import me.sheetcoldgames.ggj2015.controller.GameController;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

public class Levels {
	public static final Vector2 L1_GIRL_INITIAL_POSITION = new Vector2(32f, 4f);
	
	public void introForest(GameController controller) {
		// let's load the freaking map already
		controller.map = new TmxMapLoader().load("level1.tmx");
		// createGirl(controller);
	}
	
	
}
