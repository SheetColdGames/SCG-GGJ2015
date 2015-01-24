package me.sheetcoldgames.ggj2015.controller;

import java.util.ArrayList;





import me.sheetcoldgames.ggj2015.GameRenderer;
import me.sheetcoldgames.ggj2015.Input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

public class MenuController {
	
	public boolean isFinished = false;
	public NetworkController netController;
	public GameRenderer netRenderer;
	public boolean mp = false;
	public boolean menuMp = false;
	
	OrthographicCamera camera;
	
	Input input;
	
	public ArrayList<Rectangle> buttons;
	public ArrayList<Rectangle> buttonsMP;
	
	public MenuController(Input input) {
		camera = new OrthographicCamera(640f, 480f);
		camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0f);
		camera.update();
		
		this.input = input;
		
		buttons = new ArrayList<Rectangle>();
		for (int i = 0; i < 3; i++) {
			buttons.add(
					new Rectangle(
							camera.viewportWidth/2f-64f,
							camera.viewportHeight/2f+64f-i*64f, 
							128f, 48f)
					);
		}
		
		buttonsMP = new ArrayList<Rectangle>();
		for (int i = 0; i < 2; i++) {
			buttonsMP.add(
					new Rectangle(
							camera.viewportWidth/2f-64f,
							camera.viewportHeight/2f+64f-i*64f, 
							128f, 48f)
					);
		}
		
	}
	
	public void initMP(boolean host){
		NetworkController.isHost = host;
		netController = new NetworkController(input);
		
		netRenderer = new GameRenderer(netController);
	}
	
	public void dispose() {
		if(mp){
			netController.dispose();
			//netRenderer.dispose();
		}
	}
	
	public void update() {
		handleInput();
	}
	
	boolean mouseReleased = false;
	
	private void handleInput() {
		camera.unproject(input.currentRawPoint);
		
		if (input.mouseDown) {
			mouseReleased = true;
		}
		
		if (!input.mouseDown && input.mouseReleased && mouseReleased) {
			mouseReleased = false;
			
			if(!menuMp){
				for (int i = 0; i < buttons.size(); i++) {
					if (buttons.get(i).contains(input.currentRawPoint.x, input.currentRawPoint.y)) {
						if (i == 0){
							isFinished = true;
						}
						
						if (i == 1){
							menuMp = true;
							//initMP(true);
							//mp = true;
							//isFinished = true;
						} else if(i == 2){
							//initMP(false);
							//mp = true;
							//isFinished = true;
						}
						System.out.println("Index of button: " + i);
					}
				}
			} else{
				for (int i = 0; i < buttonsMP.size(); i++) {
					if (buttonsMP.get(i).contains(input.currentRawPoint.x, input.currentRawPoint.y)) {
						if (i == 0){
							menuMp = true;
							initMP(true);
							mp = true;
							isFinished = true;
						}
						
						else if (i == 1){
							initMP(false);
							mp = true;
							isFinished = true;
						}
						System.out.println("Index of button: " + i);
					}
				}
			}
		}
	}
}
