package me.sheetcoldgames.ggj2015.controller;

import java.util.ArrayList;
import java.util.LinkedList;

import me.sheetcoldgames.ggj2015.engine.Entity;
import me.sheetcoldgames.ggj2015.engine.SheetPoint;

/**
 * Available methods for this
 * controller.moveEntity(Entity, DIRECTION);
 * controller.resetXVelocity(Entity);
 * controller.resetYVelocity(Entity);
 * @author Rafael Giordanno
 *
 */
public class AIController {
	
	GameController controller;
	
	public AIController(GameController controller) {
		this.controller = controller;
	}
	
	/**
	 * This method uses the groupPoints containing all the points that exist in
	 * the world to check the path of the enemy.
	 * 
	 * @param enemy the current enemy
	 * @param groupPoints all the collision points in the world
	 * @param girl the girl entity controlled by the player
	 * @param robot the robot entity controlled by the player
	 */
	public void updateEnemy(Entity enemy,
			ArrayList<LinkedList<SheetPoint>> groupPoints, Entity girl,
			Entity robot) {
		
	}

}
