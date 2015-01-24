package me.sheetcoldgames.ggj2015;

import me.sheetcoldgames.ggj2015.controller.GameController;
import me.sheetcoldgames.ggj2015.controller.MenuController;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;

public class GGJ2015 extends ApplicationAdapter {
	Input input;
	
	MenuController menuController;
	GameController gameController;
	
	MenuRenderer menuRenderer;
	GameRenderer gameRenderer;
	
	public void create() {
		input = new Input();
		
		gameController = new GameController(input);
		menuController = new MenuController(input);
		
		gameRenderer = new GameRenderer(gameController);
		menuRenderer = new MenuRenderer(menuController);
		
		Gdx.input.setInputProcessor(input);
		
		menuController.isFinished = true;
	}
	
	public void dispose() {
		gameController.dispose();
		menuController.dispose();
		
		gameRenderer.dispose();
		menuRenderer.dispose();
	}
	
	public void render() {
		Gdx.gl.glClearColor(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (menuController.isFinished) {
			gameController.update();
			gameRenderer.render(true);
		} else {
			menuController.update();
			menuRenderer.render();
		}
	}
}
