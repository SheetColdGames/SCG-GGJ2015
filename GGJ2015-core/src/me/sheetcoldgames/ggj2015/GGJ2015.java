package me.sheetcoldgames.ggj2015;

import java.util.HashMap;

import me.sheetcoldgames.ggj2015.controller.GameController;
import me.sheetcoldgames.ggj2015.controller.MenuController;
import me.sheetcoldgames.ggj2015.controller.NetworkController;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;

public class GGJ2015 extends ApplicationAdapter {
	Input input;

	public static FileHandle configFile;
	public static Preferences prefs;
	MenuController menuController;
	GameController gameController;

	MenuRenderer menuRenderer;
	GameRenderer gameRenderer;
	public static HashMap<String, String> config = new HashMap<String, String>();

	public void create() {

		getConfigFile();

		input = new Input();

		menuController = new MenuController(input);

		menuRenderer = new MenuRenderer(menuController);

		Gdx.input.setInputProcessor(input);

		menuController.isFinished = false;
	}

	private void getConfigFile() {
		prefs = Gdx.app.getPreferences("g976.scfg");
		resetPreferences();

	}

	private void resetPreferences() {
		if (prefs.get().size() != 4) {
			prefs.clear();
			System.out.println("Preferences file manually modified or corrupted. Resetting all preferences, bitch!");
		}
		if (!prefs.contains("lastLanIp")) {
			prefs.putString("lastLanIp", "");
		}
		if (!prefs.contains("lastOnlineIp")) {
			prefs.putString("lastOnlineIp", "");
		}
		if (!prefs.contains("lastHostIp")) {
			prefs.putString("lastHostIp", "");
		}
		if (!prefs.contains("volume")) {
			prefs.putFloat("volume", 1f);
		}
		prefs.flush();
	}

	public void dispose() {
		if (gameController != null) {
			gameController.dispose();
		}
		menuController.dispose();

		if (gameRenderer != null) {
			gameRenderer.dispose();
		}

		resetPreferences();
	}

	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (menuController.isFinished) {
			gameController.update();
			gameRenderer.render(gameController.debugRender);
		} else {
			menuController.update();
			menuRenderer.render();
			if (menuController.isFinished) {
				System.out.println("Game Started!");
				if (menuController.isHost) {
					// if there's a host, then probably we are online or on lan
					if (menuController.online) { 
						gameController = new NetworkController(input, menuController.strIp.toString() , true);
					} else {
						gameController = new NetworkController(input, null, true);
					}
				} else {
					// we are probably a client or offline
					if (menuController.lan || menuController.online) {
						gameController = new NetworkController(input, menuController.strIp.toString(), false);
						if (!((NetworkController)gameController).isConnected()) {
							menuController.dispose();
							menuController = null;
							menuController = new MenuController(input);
							
							menuRenderer.setController(menuController);
							
							menuController.currentMessage.append("Could not connect to host :/");
						}
					} else {
						gameController = new GameController(input);	
					}
				}

				gameRenderer = new GameRenderer(gameController);
				menuController.dispose();
			}
		}
	}
}
