package me.sheetcoldgames.ggj2015;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.WINDOW_WIDTH;
		config.height = Constants.WINDOW_HEIGHT;
		config.resizable = false;
		config.title = Constants.WINDOW_TITLE;
		
		new LwjglApplication(new GGJ2015(), config);
	}
}
