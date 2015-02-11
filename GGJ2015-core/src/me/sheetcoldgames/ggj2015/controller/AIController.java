package me.sheetcoldgames.ggj2015.controller;

import java.util.ArrayList;
import java.util.LinkedList;

import me.sheetcoldgames.ggj2015.engine.ACTION;
import me.sheetcoldgames.ggj2015.engine.DIRECTION;
import me.sheetcoldgames.ggj2015.engine.Entity;
import me.sheetcoldgames.ggj2015.engine.SheetPoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
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
		enemyRadius.radius = 4f;
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
		/*
		 * if the enemy status is guarding, then he follow his patrol points
		 * if, while following his patrol points, he finds the girl or the robot
		 * then he should update his status to attack the girl or the robot
		 */
		if (enemy.action == ACTION.WALK) {
			followPatrolPoints(enemy, true);
		} else if (enemy.action == ACTION.IDLE) {
			guardPatrolPoint(enemy, 2f, 4);
		} else if (enemy.action == ACTION.ATTACK) {
			
		}
		System.out.println(enemy.action);
		System.out.println(enemy.horizontalDir + "\n");
		/*
		if (enemy.patrolPoints.size() != 0) {
			enemyRadius.x = enemy.position.x;
			enemyRadius.y = enemy.position.y;
			// updatePatrolPoints(enemy, girl, robot);
			if (enemy.action != ACTION.ATTACK) {
				if (enemyRadius.contains(girl.position.x, girl.position.y) || 
						enemyRadius.contains(robot.position.x, robot.position.y)) {
					// in case the enemy sees the girl
					if (seeEntity(enemy.position.x, enemy.position.y, girl.position.x, girl.position.y)) {
						//System.out.println("I'm supposed to attack the girl now");
						if (enemy.id == Constants.YELLOW_ENEMY_AI_ID) {
							enemy.velocity.set(0f, 0f);
							enemy.action = ACTION.ATTACK;
							enemy.stateTime = 0f;
							if (enemy.position.y < girl.position.y) {
								enemy.verticalDir = DIRECTION.UP;
							} else if ((int) enemy.position.x < (int) girl.position.x) {
								enemy.horizontalDir = DIRECTION.RIGHT;
							} else if ((int) enemy.position.x > (int) girl.position.x) {
								enemy.horizontalDir = DIRECTION.LEFT;
							} else {
								enemy.horizontalDir = DIRECTION.DOWN;
							}
						}
					} else if (seeEntity(enemy.position.x, enemy.position.y, robot.position.x, robot.position.y)) {
						//System.out.println("I'm supposed to attack the robot now");
					} 
				} else {
					followPatrolPoints(enemy);
				}
			} else {
				
				if (enemy.position.y < girl.position.y) {
					enemy.verticalDir = DIRECTION.UP;					
				} else if ((int) enemy.position.x < (int) girl.position.x) {
					enemy.horizontalDir = DIRECTION.RIGHT;
				} else if ((int) enemy.position.x > (int) girl.position.x) {
					enemy.horizontalDir = DIRECTION.LEFT;
				} else {
					enemy.horizontalDir = DIRECTION.DOWN;
				}
				if (enemy.id == Constants.BLUE_ENEMY_AI_ID) {
					if (enemy.stateTime > controller.blueEnemyAttackDuration) {
						enemy.action = ACTION.IDLE;
					}
				} else if (enemy.id == Constants.YELLOW_ENEMY_AI_ID) {
					if (enemy.stateTime > controller.yellowEnemyAttackDuration) {
						enemy.action = ACTION.IDLE;
					}
				}
			}
		} else {
			//System.out.println("out of bounds");
		}
		*/
	}
	
	/*
	private void updatePatrolPoints(Entity enemy, Entity girl, Entity robot) {
		enemyRadius.x = enemy.position.x;
		enemyRadius.y = enemy.position.y;
		
		if (enemyRadius.contains(girl.position)) {
			Vector2 intersectedPoint = seeGirl(enemy, enemy.position.x, enemy.position.y,
					girl.position.x, girl.position.y);
			if (intersectedPoint != null) {
				enemy.patrolPoints.clear();
				enemy.currentPatrolPoint = 0;
				enemy.patrolPoints.add(new Vector2((int)enemy.position.x, (int)enemy.position.y));
				if (intersectedPoint.x > enemy.position.x) {
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
	*/
	
	private boolean seeEntity(float ox, float oy, float dx, float dy) {
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
					// return intersectedPoint;
					return false;
				}
				/*
				if (Intersector.intersectSegments(
						ox-enemy.width/2f, oy-enemy.height/2f,
						dx, dy,
						controller.groupPoints.get(currentGroup).get(currentPoint).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint).getY(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getY(),
						intersectedPoint)) {
					
				}
				if (Intersector.intersectSegments(
						ox-enemy.width/2f, oy+enemy.height/2f, 
						dx, dy,
						controller.groupPoints.get(currentGroup).get(currentPoint).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint).getY(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getY(),
						intersectedPoint)) {
					
				}
				if (Intersector.intersectSegments(
						ox+enemy.width/2f, oy-enemy.height/2f, dx, dy,
						controller.groupPoints.get(currentGroup).get(currentPoint).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint).getY(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getY(),
						intersectedPoint)) {
					
				}
				if (Intersector.intersectSegments(
						ox+enemy.width/2f, oy+enemy.height/2f, 
						dx, dy,
						controller.groupPoints.get(currentGroup).get(currentPoint).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint).getY(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getX(),
						controller.groupPoints.get(currentGroup).get(currentPoint+1).getY(),
						intersectedPoint)) {
				}
				*/
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param enemy the enemy that will follow the patrol points and guard the area
	 * @param checkGuard if true, then guard will change its stance to idle every time he meets a patrol point
	 */
	private void followPatrolPoints(Entity enemy, boolean checkGuard) {
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
		} else { // the enemy meets a guard point
			enemy.velocity.x = 0;
			enemy.velocity.y = 0;
			enemy.position.x = enemy.patrolPoints.get(enemy.currentPatrolPoint).x;
			enemy.position.y = enemy.patrolPoints.get(enemy.currentPatrolPoint).y;
			int previousPoint = enemy.currentPatrolPoint++;
			
			if (enemy.currentPatrolPoint == enemy.patrolPoints.size()) {
				enemy.currentPatrolPoint = 0;
			}
			if (checkGuard) {
				enemy.action = ACTION.IDLE;
				enemy.stateTime = 0f;
			}
			/*
			Vector2 intersectedPoint = new Vector2();
			
			// We check all the points to see if there's any intersection with this point
			for (int currentGroup = 0; currentGroup < controller.groupPoints.size(); currentGroup++) {
				for (int currentPoint = 0; currentPoint < controller.groupPoints.get(currentGroup).size() - 1; currentPoint++) {
					if (Intersector.intersectSegments(
							enemy.patrolPoints.get(previousPoint).x,
							enemy.patrolPoints.get(previousPoint).y,
							enemy.patrolPoints.get(enemy.currentPatrolPoint).x,
							enemy.patrolPoints.get(enemy.currentPatrolPoint).y,
							controller.groupPoints.get(currentGroup).get(currentPoint).getX(),
							controller.groupPoints.get(currentGroup).get(currentPoint).getY(),
							controller.groupPoints.get(currentGroup).get(currentPoint+1).getX(),
							controller.groupPoints.get(currentGroup).get(currentPoint+1).getY(),
							intersectedPoint)) {
						// it collided, we must create another point for it, let's surround all the
						// possible combinations and create a new point between this collision point and our destiny
					}
				}
			}
			*/
		}
	}
	
	/**
	 * 
	 * @param enemy the enemy instance for use
	 * @param timeStanding how many seconds will take before the enemy does anything else
	 * @param checks the amount of checks the guard will make before doing anything else
	 */
	private void guardPatrolPoint(Entity enemy, float timeStanding, int checks) {
		enemy.verticalDir = DIRECTION.DOWN;
		
		float factorOfTime = MathUtils.PI/timeStanding;
		
		if (enemy.stateTime > timeStanding * checks) {
			enemy.stateTime = 0f;
			enemy.action = ACTION.WALK;
		} else if (MathUtils.sin(enemy.stateTime * factorOfTime) < 0) {
			enemy.horizontalDir = DIRECTION.LEFT;
		} else {
			enemy.horizontalDir = DIRECTION.RIGHT;
		}

		System.out.println("Enemy state time: " + enemy.stateTime);
		System.out.println((MathUtils.cos(enemy.stateTime + MathUtils.PI) + 1)*timeStanding);
		/*
		if (enemy.stateTime < 2f) {
			enemy.horizontalDir = DIRECTION.RIGHT;
		} else if (enemy.stateTime < 4f) {
		} else if (enemy.stateTime < 6f) {
			enemy.horizontalDir = DIRECTION.RIGHT;
		} else if (enemy.stateTime < 8f) {
			
		} else if (enemy.stateTime < 10f) {
			enemy.horizontalDir = DIRECTION.RIGHT;
		} else {
			enemy.stateTime = 0f;
			enemy.action = ACTION.WALK;
		}
		*/
	}
}
