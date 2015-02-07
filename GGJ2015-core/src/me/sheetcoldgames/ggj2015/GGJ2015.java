package me.sheetcoldgames.ggj2015;

import me.sheetcoldgames.ggj2015.controller.GameController;
import me.sheetcoldgames.ggj2015.controller.MenuController;
import me.sheetcoldgames.ggj2015.controller.NetworkController;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GGJ2015 extends ApplicationAdapter {
	Input input;

	MenuController menuController;
	GameController gameController;

	MenuRenderer menuRenderer;
	GameRenderer gameRenderer;

	public void create() {
		input = new Input();

		menuController = new MenuController(input);

		menuRenderer = new MenuRenderer(menuController);

		Gdx.input.setInputProcessor(input);

		menuController.isFinished = false;
	}

	public void dispose() {
		if (gameController != null) {
			gameController.dispose();
		}
		menuController.dispose();

		if (gameRenderer != null) {
			gameRenderer.dispose();
		}
	}

	public void render() {
		// Gdx.gl.glClearColor(MathUtils.random(), MathUtils.random(),
		// MathUtils.random(), 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (menuController.isFinished) {
			gameController.update();
			gameRenderer.render(gameController.debugRender);
		} else {
			menuController.update();
			menuRenderer.render();
			if (menuController.isFinished) {
				System.out.println("Game Started!");
				gameController = menuController.mp ? (new NetworkController(
						input, menuController.isHost)) : (new GameController(
						input));
				gameRenderer = new GameRenderer(gameController);
				menuController.dispose();
			}
		}
	}
}
