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

	public boolean isHost; // if true the host will wait for a
											
	private boolean connected = false; // client
	private String hostAddr = "192.168.1.5";

	// private int hostCmd = 99998;
	// private int clientCmd = 99999;
	private String hostAnim = ACTION.IDLE.toString();
	private String clientAnim = ACTION.IDLE.toString();
	private String hostDirH = DIRECTION.DOWN.toString();
	private String hostDirV = DIRECTION.DOWN.toString();
	private String clientDirH = DIRECTION.DOWN.toString();
	private String clientDirV = DIRECTION.DOWN.toString();
	private String[] hostStatus = { hostAnim, "90f", "12f", hostDirH, hostDirV }; // "cmd/playerPosX/playerPosY"
	private String[] clientStatus = { clientAnim,
			String.valueOf(Constants.L0_GIRL_INIT_POS.x),
			String.valueOf(Constants.L0_GIRL_INIT_POS.y), clientDirH,
			clientDirV }; // "cmd/playerPosX/playerPosY"
	private Vector2 hostPos = new Vector2(90f, 12f);
	private Vector2 clientPos = new Vector2(Constants.L0_GIRL_INIT_POS.x,
			Constants.L0_GIRL_INIT_POS.y);
	private String[] enemyStatus;
	private ArrayList<String[]> enemyList = new ArrayList<String[]>();
	private Entity robo;

	Host host;
	Client client;

	public NetworkController(Input input, boolean isHost) {
		super(input);
		this.isHost = isHost;
		
		if (isHost) {
			girlControlScheme = Constants.INPUT_ARROWS;
			robotControlScheme = Constants.INPUT_NOTHING;
			host = new Host();
		} else {
			robotControlScheme = Constants.INPUT_ARROWS;
			girlControlScheme = Constants.INPUT_NOTHING;
			client = new Client(hostAddr);
			connected = client.isConnected();
		}
	}

	@Override
	protected void initCamera() {
		super.initCamera();
	}

	public String concat;

	@Override
	public void update() {
		dt = Gdx.graphics.getDeltaTime();
		if (isHost && !connected) {
			connected = host.clientConnected();
		}

		reorganizeEntities(girlId, robotId);
		updateEntities();

		camera.setTarget(aEntity.get(currentGirlIndex));
		camera.update();

		if (!isHost) {
			camera.setTarget(aEntity.get(currentRobotIndex));
			camera.update();

		}

		connectionSendUpdateObj();
		connectionReceiveUpdateObj();

		aEntity.get(currentRobotIndex).position.x = Float
				.valueOf(clientStatus[1]);
		aEntity.get(currentRobotIndex).position.y = Float
				.valueOf(clientStatus[2]);
	}

	private void connectionSendUpdateObj() {
		if (connected) {
			if (isHost) {
				host.sendObjectToClient(aEntity);
			} else {
				client.sendToHost(clientStatus);
				// client.sendObjectToHost(aEntity);
			}
		}
	}

	ArrayList<Entity> pre;

	private void connectionReceiveUpdateObj() {
		if (isHost) {
			clientStatus = host.getFromClientSTR();
			clientAnim = clientStatus[0];
			clientDirH = clientStatus[3];
			clientDirV = clientStatus[4];
			clientPos = new Vector2(Float.parseFloat(clientStatus[1]),
					Float.parseFloat(clientStatus[2]));
		} else {
			aEntity = client.getObjFromHost();
		}
	}

	protected void updateEntityPosition(Entity ent) {
		super.updateEntityPosition(ent);
		if (!isHost && ent.id == Constants.ROBOT_ID) {
			ent.position.x += ent.velocity.x;
			ent.position.y += ent.velocity.y;
		}
		String[] tes;
		/*
		 * if(ent.id == Constants.ENEMY_AI_ID && !isHost){ tes =
		 * enemyList.get(index); ent.position.x = Float.parseFloat(tes[1]);
		 * ent.position.y = Float.parseFloat(tes[2]); }
		 */

		/*
		 * if (ent.id == Constants.GIRL_ID && isHost){ hostStatus[0] =
		 * ent.action.toString(); hostStatus[1] =
		 * String.valueOf(ent.position.x); hostStatus[2] =
		 * String.valueOf(ent.position.y); hostStatus[3] =
		 * ent.horizontalDir.toString(); hostStatus[4] =
		 * ent.verticalDir.toString();
		 * 
		 * } else if(ent.id == Constants.GIRL_ID && !isHost){
		 * 
		 * ent.position = hostPos; ent.action = checkAnim(hostStatus[0]);
		 * ent.horizontalDir = checkDir(hostStatus[3]); ent.verticalDir =
		 * checkDir(hostStatus[4]);
		 * 
		 * }
		 */
		if (ent.id == Constants.ROBOT_ID && !isHost) {

			clientStatus[0] = ent.action.toString();
			clientStatus[1] = String.valueOf(ent.position.x);
			clientStatus[2] = String.valueOf(ent.position.y);
			clientStatus[3] = ent.horizontalDir.toString();
			clientStatus[4] = ent.verticalDir.toString();

		} else if (ent.id == Constants.ROBOT_ID && isHost) {

			ent.action = checkAnim(clientStatus[0]);
			ent.position = clientPos;
			ent.horizontalDir = checkDir(clientStatus[3]);
			ent.verticalDir = checkDir(clientStatus[4]);
		}
	}

	private ACTION checkAnim(String status) {
		if (status.equals(ACTION.WALK.toString())) {
			return ACTION.WALK;
		} else {
			return ACTION.IDLE;
		}
	}

	private DIRECTION checkDir(String status) {
		if (status.equals(DIRECTION.UP.toString())) {
			return DIRECTION.UP;
		} else if (status.equals(DIRECTION.DOWN.toString())) {
			return DIRECTION.DOWN;
		} else if (status.equals(DIRECTION.RIGHT.toString())) {
			return DIRECTION.RIGHT;
		} else {
			return DIRECTION.LEFT;
		}
	}

}
