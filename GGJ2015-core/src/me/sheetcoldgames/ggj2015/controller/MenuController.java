package me.sheetcoldgames.ggj2015.controller;

import java.util.ArrayList;

import me.sheetcoldgames.ggj2015.GameRenderer;
import me.sheetcoldgames.ggj2015.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class MenuController {
	
	public static boolean isFinished = false;
	public NetworkController netController;
	public GameRenderer netRenderer;
	public boolean mp = false;
	public boolean menuMp = false;
	public OrthographicCamera camera;
	Music music;
	
	
	public Input input;
	
	public ArrayList<Rectangle> buttons;
	public ArrayList<Rectangle> buttonsMP;
	
	public MenuController(Input input) {
		camera = new OrthographicCamera(480f, 320f);
		camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0f);
		camera.update();
		
		this.input = input;
		
	}
	
	public void initMP(boolean host){
		NetworkController.isHost = host;
		netController = new NetworkController(input);
		
		netRenderer = new GameRenderer(netController);
	}
	
	public void dispose() {
		if(mp){
			netController.dispose();
			netRenderer.dispose();
		}
	}
	
	public void update() {
		handleInput();
	}
	
	public float posSelH = 190f;
	public float posSelV = 100f;
	public int option = 1;
	
	private void handleInput() {
		
		if (input.buttons[Input.DOWN]){
			posSelV = 50f;
			option = 2;
		}
		else if(input.buttons[Input.UP]){
			posSelV = 100f;
			option = 1;
		}
		else if(input.buttons[Input.ENTER]){
			if (option == 1){
				isFinished = true;
				//music.stop();	
			} else {
				dispose();
			}
		}
		
	}
}
