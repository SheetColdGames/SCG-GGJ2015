package me.sheetcoldgames.ggj2015.controller;

import java.util.ArrayList;
import java.util.LinkedList;

import me.sheetcoldgames.ggj2015.Constants;
import me.sheetcoldgames.ggj2015.engine.DIRECTION;
import me.sheetcoldgames.ggj2015.engine.Entity;
import me.sheetcoldgames.ggj2015.engine.SheetPoint;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

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
	Circle enemyRadius;
	public boolean collideWall = false;
	public float lastPlayerPositionX;
	public float lastPlayerPositionY;
	public boolean isPursuiting = false;
	public float enemySpeedRun = .10f;
	public int patrolPointCount = 0;
	public boolean canThrow = false;
	public boolean playerInsideRadius = false; 
	
	public AIController(GameController controller) {
		
		this.controller = controller;
		currentRadius = new Circle();
		currentRadius.radius = .25f;
		enemyRadius = new Circle();
		enemyRadius.radius = 10f;
		
		controller.aEntity.get(2).patrolPoints.clear();
		controller.aEntity.get(2).patrolPoints.add(new Vector2(92f, 32f));
		controller.aEntity.get(2).patrolPoints.add(new Vector2(92f, 38f));
		
		// controller.girlCamera.zoom =  2f;
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
			// Pursuiting(enemy,girl, robot);
			updatePatrolPoints(enemy, girl, robot);
			followPatrolPoints(enemy);
		} else {
			System.out.println("out of bounds");
		}
	}
	
	private void updatePatrolPoints(Entity enemy, Entity girl, Entity robot) {
		enemyRadius.x = enemy.position.x;
		enemyRadius.y = enemy.position.y;
		
		if (enemyRadius.contains(girl.position)) {
			Vector2 intersectedPoint = seeGirl(enemy.position.x, enemy.position.y,
					girl.position.x, girl.position.y);
			if (intersectedPoint != null) {
				enemy.patrolPoints.clear();
				enemy.currentPatrolPoint = 0;
				enemy.patrolPoints.add(new Vector2((int)enemy.position.x, (int)enemy.position.y));
				if (intersectedPoint.x >enemy.position.x) {
					controller.moveEntity(enemy, DIRECTION.LEFT);
				} else if (intersectedPoint.x < enemy.position.x) {
					controller.moveEntity(enemy, DIRECTION.RIGHT);
				}
				
				if (intersectedPoint.y >enemy.position.y) {
					controller.moveEntity(enemy, DIRECTION.DOWN);
				} else if (intersectedPoint.y < enemy.position.y) {
					controller.moveEntity(enemy, DIRECTION.UP);
				}
				enemy.patrolPoints.add(new Vector2());
				enemy.patrolPoints.add(new Vector2(girl.position.x, girl.position.y));
			}
			
		}
		
		if (enemyRadius.contains(robot.position)) {
			
		}
	}
	
	private Vector2 seeGirl(float ox, float oy, float dx, float dy) {
		Vector2 intersectedPoint = new Vector2(0f, 0f);
		for (int currentGroup = 0; currentGroup < controller.groupPoints.size(); currentGroup++) {
			for (int currentPoint = 0; currentPoint < controller.groupPoints.get(currentGroup).size() - 1; currentPoint++) {
				if (Intersector.intersectSegments(
						ox, oy, dx, dy,
						controller.groupPoints.get(currentGroup).get(currentPoint).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint).getY(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getY(),
						intersectedPoint)) {
					return intersectedPoint;
				}
				if (Intersector.intersectSegments(
						ox-Constants.ENEMY_AI_WIDTH/2f, oy-Constants.ENEMY_AI_HEIGHT/2f,
						dx, dy,
						controller.groupPoints.get(currentGroup).get(currentPoint).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint).getY(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getY(),
						intersectedPoint)) {
					
				}
				if (Intersector.intersectSegments(
						ox-Constants.ENEMY_AI_WIDTH/2f, oy+Constants.ENEMY_AI_HEIGHT/2f, 
						dx, dy,
						controller.groupPoints.get(currentGroup).get(currentPoint).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint).getY(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getY(),
						intersectedPoint)) {
					
				}
				if (Intersector.intersectSegments(
						ox+Constants.ENEMY_AI_WIDTH/2f, oy-Constants.ENEMY_AI_HEIGHT/2f, dx, dy,
						controller.groupPoints.get(currentGroup).get(currentPoint).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint).getY(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getY(),
						intersectedPoint)) {
					
				}
				if (Intersector.intersectSegments(
						ox+Constants.ENEMY_AI_WIDTH/2f, oy+Constants.ENEMY_AI_HEIGHT/2f, 
						dx, dy,
						controller.groupPoints.get(currentGroup).get(currentPoint).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint).getY(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getY(),
						intersectedPoint)) {
					
				}
			}
		}
		return null;
	}
	
	private void followPatrolPoints(Entity enemy) {
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
	
	// pursuit the player till last point generated
	private void Pursuiting(Entity enemy, Entity girl, Entity robot) {
		enemyRadius.x = enemy.position.x;
		enemyRadius.y = enemy.position.y;
		if(enemyRadius.contains(girl.position)) {
			
			isPursuiting = true;
			
		} else {
			
			isPursuiting = false;
			
		}
		
		if(enemyRadius.contains(robot.position)) {
			
			isPursuiting = true;
			
		} else {
			
			isPursuiting = false;
			
		}
		
		if(isPursuiting) {
			System.out.println("pursuit");
			if(girl.position.x > enemy.position.x) {
				
				controller.moveEntity(enemy, DIRECTION.RIGHT);
				
			}
			
			if(girl.position.x < enemy.position.x) {
				
				controller.moveEntity(enemy, DIRECTION.LEFT);
				
			}
			
			if(girl.position.y > enemy.position.y) {
				
				controller.moveEntity(enemy, DIRECTION.UP);
				
			}
			
			if(girl.position.y < enemy.position.y) {
				
				controller.moveEntity(enemy, DIRECTION.DOWN);
				
			}
			
			if(robot.position.x > enemy.position.x) {
				
				controller.moveEntity(enemy, DIRECTION.RIGHT);
				
			}
			
			if(robot.position.x < enemy.position.x) {
				
				controller.moveEntity(enemy, DIRECTION.LEFT);
				
			}
			
			if(robot.position.y > enemy.position.y) {
				
				controller.moveEntity(enemy, DIRECTION.UP);
				
			}
			
			if(robot.position.y < enemy.position.y) {
				
				controller.moveEntity(enemy, DIRECTION.DOWN);
				
			}
			
			
			
		}
		
	
	}

}
