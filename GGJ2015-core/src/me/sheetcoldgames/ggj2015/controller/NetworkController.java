package me.sheetcoldgames.ggj2015.controller;

import me.sheetcoldgames.ggj2015.Constants;
import me.sheetcoldgames.ggj2015.Input;
import me.sheetcoldgames.ggj2015.engine.SheetCamera;

public class NetworkController extends GameController {
	
	SheetCamera robotCamera;
	
	public NetworkController(Input input) {
		super(input);
		
		girlControlScheme = Constants.INPUT_ARROWS;
		robotControlScheme = Constants.INPUT_ARROWS;
	}
	
	@Override
	protected void initCamera() {
		super.initCamera();
		
		robotCamera = new SheetCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		robotCamera.position.set(robotCamera.viewportWidth/2f, robotCamera.viewportHeight/2f, 0f);
		robotCamera.update();
		
		// Defines the girl as the target of this camera
		robotCamera.setTarget(aEntity.get(currentGirlIndex));
		robotCamera.update();
	}
	
	@Override
	public void update() {
		
	}
}
