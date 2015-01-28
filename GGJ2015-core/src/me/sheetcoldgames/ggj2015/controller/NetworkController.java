package me.sheetcoldgames.ggj2015.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import me.sheetcoldgames.ggj2015.Constants;
import me.sheetcoldgames.ggj2015.Input;
import me.sheetcoldgames.ggj2015.engine.ACTION;
import me.sheetcoldgames.ggj2015.engine.Client;
import me.sheetcoldgames.ggj2015.engine.DIRECTION;
import me.sheetcoldgames.ggj2015.engine.Entity;
import me.sheetcoldgames.ggj2015.engine.Host;
import me.sheetcoldgames.ggj2015.engine.SheetCamera;
import me.sheetcoldgames.ggj2015.engine.SheetPoint;

public class NetworkController extends GameController {
	
	public static boolean isHost = false; //if true the host will wait for a client
	private boolean connected = false;
	private String hostAddr = "192.168.1.2";
	
	//private int hostCmd = 99998;
	//private int clientCmd = 99999;
	private String hostAnim = ACTION.IDLE.toString();
	private String clientAnim = ACTION.IDLE.toString();
	private String hostDirH = DIRECTION.DOWN.toString();
	private String hostDirV = DIRECTION.DOWN.toString();
	private String clientDirH = DIRECTION.DOWN.toString();
	private String clientDirV = DIRECTION.DOWN.toString();
	private String[] hostStatus = {hostAnim,"90f","12f",hostDirH,hostDirV}; // "cmd/playerPosX/playerPosY"
	private String[] clientStatus = {clientAnim,"94f","12f",clientDirH,clientDirV}; // "cmd/playerPosX/playerPosY"
	private Vector2 hostPos = new Vector2(90f,12f);
	private Vector2 clientPos = new Vector2(31f,12f);
	private String[] enemyStatus;
	private ArrayList<String[]> enemyList = new ArrayList<String[]>();
	private Entity robo;
	
	
	Host host;
	Client client;
	SheetCamera robotCamera;
	
	public NetworkController(Input input) {
		super(input);
		
		girlControlScheme = Constants.INPUT_ARROWS;
		robotControlScheme = Constants.INPUT_ARROWS;
		
		if(isHost){
			host = new Host();
		} else{
			client = new Client(hostAddr);
			connected = client.isConnected();
		}
	}
	
	@Override
	protected void initCamera() {

		super.initCamera();
			
		if(!isHost){
			girlCamera.setTarget(aEntity.get(currentRobotIndex));
			girlCamera.update();
		}
	}
	
	public String concat;
	
	
	@Override
	public void update() {
		dt = Gdx.graphics.getDeltaTime();
		
		if(isHost && !connected){
			connected = host.clientConnected();
		}
		
		reorganizeEntities(girlId, robotId);
		updateEntities();
		
		girlCamera.update();
		
		connectionSendUpdateObj();
		
		connectionReceiveUpdateObj();
		
		
		aEntity.get(currentRobotIndex).position.x = Float.valueOf(clientStatus[1]);
		aEntity.get(currentRobotIndex).position.y = Float.valueOf(clientStatus[2]);
		
		
		
		
		
	}
	
	private void upRobo(){
		
		robo.action = checkAnim(clientStatus[0]);
		robo.position = clientPos;
		robo.horizontalDir = checkDir(clientStatus[3]);
		robo.verticalDir = checkDir(clientStatus[4]);
	}
	
	private void connectionSendUpdateObj(){
		if(connected){
			if(isHost){
				host.sendObjectToClient(aEntity);
			} else{
				client.sendToHost(clientStatus);
				//client.sendObjectToHost(aEntity);
			}
		}
	}
	ArrayList<Entity> pre;
	private void connectionReceiveUpdateObj(){
		if (isHost){
			clientStatus = host.getFromClientSTR();
			clientAnim = clientStatus[0];
			clientDirH = clientStatus[3];
			clientDirV = clientStatus[4];
			clientPos = new Vector2(Float.parseFloat(clientStatus[1]), Float.parseFloat(clientStatus[2]));
		} else{
			
			aEntity = client.getObjFromHost();
			
			
		
			
		}
	}
	
	
	private void connectionSendUpdate(){
		if(connected){
			if(!isHost){
				//host.sendToClient(hostStatus);
				client.sendToHost(clientStatus);
			} else{
				//client.sendToHost(clientStatus);
			}
		}
	}
	//String[][] hostHash;
	private void connectionReceiveUpdate(){
		if(connected){
			if(isHost){
				clientStatus = host.getFromClientSTR();
				clientAnim = clientStatus[0];
				clientDirH = clientStatus[3];
				clientDirV = clientStatus[4];
				clientPos = new Vector2(Float.parseFloat(clientStatus[1]), Float.parseFloat(clientStatus[2]));
				
				
			} 
		}
	}
	
	
	
	protected void updateEntityPosition(Entity ent) {
		ent.stateTime += (Gdx.graphics.getDeltaTime() + ent.accel/120f);
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
					ent.stateTime = 0;
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
					ent.stateTime = 0;
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
					ent.stateTime = 0;					
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
					ent.stateTime = 0;
				}
			}
		}
		if (ent.id != Constants.ROBOT_ID){  
		ent.position.x += ent.velocity.x;
		ent.position.y += ent.velocity.y;
		} else if(!isHost && ent.id == Constants.ROBOT_ID){
			ent.position.x += ent.velocity.x;
			ent.position.y += ent.velocity.y;
		}
		String[] tes;
		/*if(ent.id == Constants.ENEMY_AI_ID && !isHost){
			tes = enemyList.get(index);
			ent.position.x = Float.parseFloat(tes[1]);
			ent.position.y = Float.parseFloat(tes[2]);
		}*/
		
/*		if (ent.id == Constants.GIRL_ID && isHost){
			hostStatus[0] = ent.action.toString();
			hostStatus[1] = String.valueOf(ent.position.x);
			hostStatus[2] = String.valueOf(ent.position.y);
			hostStatus[3] = ent.horizontalDir.toString();
			hostStatus[4] = ent.verticalDir.toString();
			
		} else if(ent.id == Constants.GIRL_ID && !isHost){ 
			
			ent.position = hostPos;			
			ent.action = checkAnim(hostStatus[0]);
			ent.horizontalDir = checkDir(hostStatus[3]);
			ent.verticalDir = checkDir(hostStatus[4]);
			
		}*/
	if(ent.id == Constants.ROBOT_ID && !isHost){
		
			clientStatus[0] = ent.action.toString();
			clientStatus[1] = String.valueOf(ent.position.x);
			clientStatus[2] = String.valueOf(ent.position.y);
			clientStatus[3] = ent.horizontalDir.toString();
			clientStatus[4] = ent.verticalDir.toString();
			
	} else if(ent.id == Constants.ROBOT_ID && isHost){
			
			ent.action = checkAnim(clientStatus[0]);
			ent.position = clientPos;
			ent.horizontalDir = checkDir(clientStatus[3]);
			ent.verticalDir = checkDir(clientStatus[4]);
		}
	}
	
	private ACTION checkAnim(String status){
		if (status.equals(ACTION.WALK.toString())){
			return ACTION.WALK;
		} else{
			return ACTION.IDLE;
		}
	}
	
	private DIRECTION checkDir(String status){
		if (status.equals(DIRECTION.UP.toString())){
			return DIRECTION.UP;
		} else if (status.equals(DIRECTION.DOWN.toString())){
			return DIRECTION.DOWN;
		} else if (status.equals(DIRECTION.RIGHT.toString())){
			return DIRECTION.RIGHT;
		} else{
			return DIRECTION.LEFT;
		}
	}
	
	
	
}
