package me.sheetcoldgames.ggj2015.controller;

import java.util.ArrayList;

import me.sheetcoldgames.ggj2015.GameRenderer;
import me.sheetcoldgames.ggj2015.Input;
import me.sheetcoldgames.ggj2015.MenuRenderer;

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
		mp = true;
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
	private int option = 1;
	
	private void handleInput() {
		
		if (!menuMp){
			menuInput();
		} else{
			mpInput();
		}
		
	}
	
	private void menuInput(){
		if (option != 3){
			if (input.buttons[Input.DOWN]){
				input.releaseAllKeys();
				MenuRenderer.state2 = 0;
				posSelV = posSelV - 30f;//50f
				option = option + 1;
			}
		} if(option != 1){
			if(input.buttons[Input.UP]){
				input.releaseAllKeys();
				MenuRenderer.state2 = 0;
				posSelV = posSelV + 30f;//100f
				option = option - 1;
			}
		}
		if(input.buttons[Input.ENTER]){
			input.releaseAllKeys();
			if (option == 1){
				isFinished = true;
				//music.stop();	
			} else if(option == 2){
				menuMp = true;
			} else {
				dispose();
			}
		}
	}
	
	public float mpSelH = 190f;
	public float mpSelV = 175f;
	private int mpOption = 1;
	
	private void mpInput(){
		if (mpOption != 2){
			if (input.buttons[Input.DOWN]){
				input.releaseAllKeys();
				MenuRenderer.state2 = 0;
				mpSelV = mpSelV - 30f;//50f
				mpOption = mpOption + 1;
			}
		} if(mpOption != 1){
			if(input.buttons[Input.UP]){
				input.releaseAllKeys();
				MenuRenderer.state2 = 0;
				mpSelV = mpSelV + 30f;//100f
				mpOption = mpOption - 1;
			}
		}
		if(input.buttons[Input.ENTER]){
			input.releaseAllKeys();
			if (mpOption == 1){
				initMP(true);
				isFinished = true;
				//music.stop();	
			} else if(mpOption == 2){
				initMP(false);
				isFinished = true;
			} else {
				dispose();
			}
		}
	}
}
