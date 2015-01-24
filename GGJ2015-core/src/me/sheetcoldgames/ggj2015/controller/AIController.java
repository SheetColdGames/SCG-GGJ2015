package me.sheetcoldgames.ggj2015.controller;

import java.util.ArrayList;
import java.util.LinkedList;

import me.sheetcoldgames.ggj2015.engine.DIRECTION;
import me.sheetcoldgames.ggj2015.engine.Entity;
import me.sheetcoldgames.ggj2015.engine.SheetPoint;

import com.badlogic.gdx.math.Circle;

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
	
	//
	Circle currentRadius;
	
	public AIController(GameController controller) {
		this.controller = controller;
		currentRadius = new Circle();
		currentRadius.radius = .25f;
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
		if (enemy.patrolPoints.size() != 0) {
			// update with the currentRadius we're trying to reach
			currentRadius.x = enemy.patrolPoints.get(enemy.currentPatrolPoint).x;
			currentRadius.y = enemy.patrolPoints.get(enemy.currentPatrolPoint).y;
			
			// We'll update the enemy position until it has reached the point
			if (!currentRadius.contains(enemy.position.x, enemy.position.y)) {
				// let's update the x position
				if (currentRadius.x < enemy.position.x) {
					controller.moveEntity(enemy, DIRECTION.LEFT);
				} else if (enemy.patrolPoints.get(enemy.currentPatrolPoint).x > enemy.position.x) {
					controller.moveEntity(enemy, DIRECTION.RIGHT);
				} else {
					enemy.velocity.x = 0;
					enemy.position.x = currentRadius.x;
				}
				
				// Let's update the y position
				if (currentRadius.y < enemy.position.y) {
					controller.moveEntity(enemy, DIRECTION.DOWN);
				} else if (enemy.patrolPoints.get(enemy.currentPatrolPoint).y > enemy.position.y) {
					controller.moveEntity(enemy, DIRECTION.UP);
				} else {
					enemy.velocity.y = 0;
					
				}
			} else {
				enemy.velocity.x = 0;
				enemy.velocity.y = 0;
				enemy.position.x = enemy.patrolPoints.get(enemy.currentPatrolPoint).x;
				enemy.position.y = enemy.patrolPoints.get(enemy.currentPatrolPoint).y;
				enemy.currentPatrolPoint++;
				
				
				
				if (enemy.currentPatrolPoint == enemy.patrolPoints.size()) {
					enemy.currentPatrolPoint = 0;
				}
				
			}
		}
	}

}
