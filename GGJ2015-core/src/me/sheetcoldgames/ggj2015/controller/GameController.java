package me.sheetcoldgames.ggj2015.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

import me.sheetcoldgames.ggj2015.Constants;
import me.sheetcoldgames.ggj2015.Input;
import me.sheetcoldgames.ggj2015.engine.DIRECTION;
import me.sheetcoldgames.ggj2015.engine.Entity;
import me.sheetcoldgames.ggj2015.engine.SheetCamera;
import me.sheetcoldgames.ggj2015.engine.SheetPoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameController {
	
	Input input;
	public SheetCamera girlCamera;
	
	public ArrayList<Entity> aEntity;
	
	public int girlId = Constants.GIRL_ID;
	public int currentGirlIndex = -1;
	int girlControlScheme;
	
	public int robotId = Constants.ROBOT_ID;
	public int currentRobotIndex = -1;
	int robotControlScheme;
	
	public ArrayList<LinkedList<SheetPoint>> groupPoints;
	LinkedList<SheetPoint> currentPoints;
	
	AIController aiController;
	
	public GameController(Input input) {
		// initializing keyboard input
		this.input = input;
		// =======
		
		// Initializing the points for collision detection
		initTestMap(Constants.PHYSICAL_MAP);
		// =======
		
		initEntities();
		
		girlControlScheme = Constants.INPUT_ARROWS;
		robotControlScheme = Constants.INPUT_WASD;
		
		// Initializing camera
		initCamera();
		
		aiController = new AIController(this);
	}
	
	/**
	 * Initializes the camera for the girl if single player and for both the
	 * girl and the robot on multiplayer
	 * Protected method to be overwritten by the NetworkController if necessary
	 */
	protected void initCamera() {
		girlCamera = new SheetCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		girlCamera.position.set(girlCamera.viewportWidth/2f, girlCamera.viewportHeight/2f, 0f);
		girlCamera.update();
		
		// Defines the girl as the target of this camera
		girlCamera.setTarget(aEntity.get(currentGirlIndex));
		girlCamera.update();
	}
	
	public void dispose() {
		
	}
	
	private void initTestMap(String filename) {
		groupPoints = new ArrayList<LinkedList<SheetPoint>>();
		// here we'll load the map inside the ArrayList<LinkedList<>>
		FileHandle handle = Gdx.files.internal(filename);
		Scanner scan = new Scanner(handle.readString());
		int i = -1;
		
		while (scan.hasNext()) {
			StringBuffer line = new StringBuffer(scan.nextLine());
			String[] sa = line.toString().split(" ");
			if (sa.length == 1) {
				// we're dealing with a new group
				groupPoints.add(new LinkedList<SheetPoint>());
				i++;
			} else {
				// we're dealing with new points
				groupPoints.get(i).add(
						new SheetPoint(Float.parseFloat(sa[0]), Float.parseFloat(sa[1])));
			}
			
		}
		scan.close();
		if (i == -1) {
			// We don't have any points, we must initialize with a group with no points
			groupPoints.add(new LinkedList<SheetPoint>());
		}
	}
	
	private void initEntities() {		
		// let's make an array of entities
		aEntity = new ArrayList<Entity>();
		
		// This is the GIRL
		aEntity.add(new Entity());
		aEntity.get(0).id 		= Constants.GIRL_ID;
		aEntity.get(0).width 	= Constants.GIRL_WIDTH;
		aEntity.get(0).height 	= Constants.GIRL_HEIGHT;
		aEntity.get(0).maxSpeed = Constants.GIRL_WALK_SPEED;
		aEntity.get(0).position.set(90f, 12f);
		currentGirlIndex = 0;
		
		// This is the ROBOT
		aEntity.add(new Entity());
		aEntity.get(1).id 		= Constants.ROBOT_ID;
		aEntity.get(1).width 	= Constants.ROBOT_WIDTH;
		aEntity.get(1).height 	= Constants.ROBOT_HEIGHT;
		aEntity.get(1).maxSpeed = Constants.ROBOT_WALK_SPEED;
		aEntity.get(1).position.set(94f, 12f);
		currentRobotIndex = 1;
		
		// Then, for a new set of enemies, we keep adding them in for loops
		int t = aEntity.size()+2;
		for (int k = aEntity.size(); k < t; k++) {
			aEntity.add(new Entity());
			aEntity.get(k).id 		= Constants.ENEMY_AI_ID;
			aEntity.get(k).width 	= Constants.ENEMY_AI_WIDTH/2f;
			aEntity.get(k).height 	= Constants.ENEMY_AI_HEIGHT/2f;
			aEntity.get(k).maxSpeed = Constants.ENEMY_AI_WALK_SPEED;
		}
		
		aEntity.get(2).position.set(90f, 15f);
		aEntity.get(3).position.set(94f, 15f);
	}
	
	float dt; // deltaTime
	
	public void update() {
		dt = Gdx.graphics.getDeltaTime();
		reorganizeEntities(girlId, robotId);
		
		updateEntities();
		
		girlCamera.update();
	}
	
	protected void reorganizeEntities(int girlId, int robotId) {
		// Since we're dealing with few entities, bubble sort
		// Reorganizing the enemies
		for (int i = 0; i < aEntity.size(); i++) {
			for (int j = i+1; j < aEntity.size(); j++) {
				// We want the guys with higher y values to be rendered first
				if (aEntity.get(j).position.y > aEntity.get(i).position.y) {
					Collections.swap(aEntity, i, j);
				}
			}
		}
		
		// Let's find the player
		for (int k = 0; k < aEntity.size(); k++) {
			if (aEntity.get(k).id == girlId) {
				currentGirlIndex = k;
			} else if (aEntity.get(k).id == robotId) {
				currentRobotIndex = k;
			}
		}
	}
	
	protected void updateEntities() {
		for (Entity ent : aEntity) {
			if (ent.id == Constants.GIRL_ID) {
				handleInput(ent, girlControlScheme, currentGirlIndex);
			} else if (ent.id == Constants.ROBOT_ID) {
				handleInput(ent, robotControlScheme, currentRobotIndex);
			} else if (ent.id == Constants.ENEMY_AI_ID) {
				aiController.updateEnemy(ent, groupPoints, aEntity.get(currentGirlIndex), aEntity.get(currentRobotIndex));				
			}
			// after we make sure that every entity has used the correct method,
			// we update it
			updateEntityPosition(ent);
		}
	}
	
	private void handleInput(Entity ent, int controlScheme, int id) {		
		if (controlScheme == Constants.INPUT_ARROWS) {
			// horizontal motion
			if (input.buttons[Input.RIGHT] ^ input.buttons[Input.LEFT]) {
				if (input.buttons[Input.RIGHT]) {
					moveEntity(ent, DIRECTION.RIGHT);
				} else if (input.buttons[Input.LEFT]) {
					moveEntity(ent, DIRECTION.LEFT);
				}
			} else {
				resetXVelocity(ent);
			}
			
			// vertical motion
			if (input.buttons[Input.UP] ^ input.buttons[Input.DOWN]) {
				if (input.buttons[Input.UP]) {
					moveEntity(ent, DIRECTION.UP);
				} else if (input.buttons[Input.DOWN]) {
					moveEntity(ent, DIRECTION.DOWN);
				}
			} else {
				resetYVelocity(ent);
			}
		} else {
			// horizontal motion
			if (input.buttons[Input.CAM_RIGHT] ^ input.buttons[Input.CAM_LEFT]) {
				if (input.buttons[Input.CAM_RIGHT]) {
					moveEntity(ent, DIRECTION.RIGHT);
				} else if (input.buttons[Input.CAM_LEFT]) {
					moveEntity(ent, DIRECTION.LEFT);
				}
			} else {
				resetXVelocity(ent);
			}
			
			// vertical motion
			if (input.buttons[Input.CAM_UP] ^ input.buttons[Input.CAM_DOWN]) {
				if (input.buttons[Input.CAM_UP]) {
					moveEntity(ent, DIRECTION.UP);
				} else if (input.buttons[Input.CAM_DOWN]) {
					moveEntity(ent, DIRECTION.DOWN);
				}
			} else {
				resetYVelocity(ent);
			}
		}
		
		// clamping the maximum speed
	}
	
	public void resetXVelocity(Entity ent) {
		ent.velocity.x = MathUtils.lerp(ent.velocity.x, 0f, .4f);
	}
	
	public void resetYVelocity(Entity ent) {
		ent.velocity.y = MathUtils.lerp(ent.velocity.y, 0f, .4f);
	}
	
	public void moveEntity(Entity ent, DIRECTION dir) {
		if (dir == DIRECTION.RIGHT) {
			if (ent.velocity.x == 0) {
				ent.velocity.x = ent.minSpeed;
			}
			ent.velocity.x += ent.accel * dt;
			
			ent.velocity.x = MathUtils.clamp(ent.velocity.x, -ent.maxSpeed, ent.maxSpeed);
		} else if (dir == DIRECTION.LEFT) {
			if (ent.velocity.x == 0) {
				ent.velocity.x = -ent.minSpeed;
			}
			ent.velocity.x -= ent.accel* dt;
			
			ent.velocity.x = MathUtils.clamp(ent.velocity.x, -ent.maxSpeed, ent.maxSpeed);
		} else if (dir == DIRECTION.UP) {
			if (ent.velocity.y == 0) {
				ent.velocity.y = ent.minSpeed;
			}
			ent.velocity.y += ent.accel * dt;
			
			ent.velocity.y = MathUtils.clamp(ent.velocity.y, -ent.maxSpeed, ent.maxSpeed);
		} else if (dir == DIRECTION.DOWN) {
			if (ent.velocity.y == 0) {
				ent.velocity.y = -ent.minSpeed;
			}
			ent.velocity.y -= ent.accel * dt;
			
			ent.velocity.y = MathUtils.clamp(ent.velocity.y, -ent.maxSpeed, ent.maxSpeed);
		}
	}
	
	protected void updateEntityPosition(Entity ent) {
		// This variable should be outside to avoid constant creation of new objects
		Vector2 newEntPosition = new Vector2(
				ent.position.x + ent.velocity.x, 
				ent.position.y + ent.velocity.y);
		
		Vector2 intersectedPoint = new Vector2();
		
		// walk through all the walls
		for (int currentGroup = 0; currentGroup < groupPoints.size(); currentGroup++) {
			for (int currentPoint = 0; currentPoint < groupPoints.get(currentGroup).size()-1; currentPoint++) {
				SheetPoint p1 = groupPoints.get(currentGroup).get(currentPoint);
				SheetPoint p2 = groupPoints.get(currentGroup).get(currentPoint+1);
				// does this wall intersects the ent?
				// Let's check horizontally
				if (Intersector.intersectSegments(
						p1.pos.x, p1.pos.y, p2.pos.x, p2.pos.y,
						ent.position.x - ent.width/2f - Math.abs(ent.velocity.x) - ent.offset,
						ent.position.y + ent.height/2f,
						ent.position.x + ent.width/2f + Math.abs(ent.velocity.x) + ent.offset,
						ent.position.y + ent.height/2f,
						intersectedPoint)) { // TOP
					// Checking horizontal collision
					// Cancel horizontal velocity
					ent.velocity.x = 0;
					// Update the horizontal position with a slight offset
					// p1 and p2 have the same x position
					if (intersectedPoint.x < ent.position.x) {
						// this value is smaller than the radius of the ent (else, it wouldn't
						float tmp = ent.position.x - newEntPosition.x;
						ent.position.x = p1.pos.x + ent.width/2f - tmp + ent.offset + ent.minSpeed;
					} else {
						float tmp = newEntPosition.x - ent.position.x ;
						ent.position.x = p1.pos.x - ent.width/2f + tmp - ent.offset - ent.minSpeed;
					}
				}
				if (Intersector.intersectSegments(
						p1.pos.x, p1.pos.y, p2.pos.x, p2.pos.y,
						ent.position.x - ent.width/2f - Math.abs(ent.velocity.x) - ent.offset,
						ent.position.y - ent.height/2f,
						ent.position.x + ent.width/2f + Math.abs(ent.velocity.x) + ent.offset,
						ent.position.y - ent.height/2f,
						intersectedPoint)) { // BOTTOM
					ent.velocity.x = 0;
					// Update the horizontal position with a slight offset
					// p1 and p2 have the same x position
					if (intersectedPoint.x < ent.position.x) {
						// this value is smaller than the radius of the ent (else, it wouldn't
						float tmp = ent.position.x - newEntPosition.x;
						ent.position.x = intersectedPoint.x + ent.width/2f - tmp + ent.offset + ent.minSpeed;
					} else {
						float tmp = newEntPosition.x - ent.position.x ;
						ent.position.x = intersectedPoint.x - ent.width/2f + tmp - ent.offset - ent.minSpeed;
					}
				} 
				if (Intersector.intersectSegments(
						p1.pos.x, p1.pos.y, p2.pos.x, p2.pos.y,
						ent.position.x - ent.width/2f,
						ent.position.y - ent.height/2f - Math.abs(ent.velocity.y) - ent.offset,
						ent.position.x - ent.width/2f,
						ent.position.y + ent.height/2f + Math.abs(ent.velocity.y) + ent.offset,
						intersectedPoint)) { // LEFT
					// Checking vertical collision
					// Update the vertical position with a slight offset
					if (intersectedPoint.y < ent.position.y) {
						// this value is smaller than the radius of the ent (else, it wouldn't
						float tmp = ent.position.y - newEntPosition.y;
						ent.position.y = intersectedPoint.y + ent.height/2f - tmp + ent.offset + ent.minSpeed;
					} else {
						float tmp = newEntPosition.y - ent.position.y;
						ent.position.y = intersectedPoint.y - ent.height/2f + tmp - ent.offset - ent.minSpeed;
					}
					// Do not forget to reset the velocity
					ent.velocity.y = 0;
					
				}
				if (Intersector.intersectSegments(
						p1.pos.x, p1.pos.y, p2.pos.x, p2.pos.y,
						ent.position.x + ent.width/2f,
						ent.position.y - ent.height/2f - Math.abs(ent.velocity.y) - ent.offset,
						ent.position.x + ent.width/2f,
						ent.position.y + ent.height/2f + Math.abs(ent.velocity.y) + ent.offset,
						intersectedPoint)) { // RIGHT
					// Checking vertical collision
					
					// Update the vertical position with a slight offset
					if (intersectedPoint.y < ent.position.y) {
						// this value is smaller than the radius of the ent (else, it wouldn't
						float tmp = ent.position.y - newEntPosition.y;
						ent.position.y = intersectedPoint.y + ent.height/2f - tmp + ent.offset + ent.minSpeed;
					} else {
						float tmp = newEntPosition.y - ent.position.y;
						ent.position.y = intersectedPoint.y - ent.height/2f + tmp - ent.offset - ent.minSpeed;
					}
					// Don't forget to reset the velocity to 0
					ent.velocity.y = 0;
				}
			}
		}
		ent.position.x += ent.velocity.x;
		ent.position.y += ent.velocity.y;
	}
}
