package me.sheetcoldgames.ggj2015.controller;

import me.sheetcoldgames.ggj2015.GGJ2015;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioController {
	
	private float themeVolume;
	private float fxVolume;
	
	private Music currentTheme;
	
	// ======= CONSTANTES
	public static final String MENU_THEME = "menu_theme.mp3";
	public static final String MAIN_THEME = "main_theme.mp3";
	
	public AudioController() {
		//themeVolume = Float.parseFloat(GGJ2015.config.get("volume"));
		themeVolume = GGJ2015.prefs.getFloat("volume");
		fxVolume = themeVolume;
	}
	
	public void dispose() {
		currentTheme.dispose();
	}
	
	// current theme stuff =======
	public void loadTheme(String theme) {
		if (currentTheme != null) {
			currentTheme.dispose();
		}
		currentTheme = Gdx.audio.newMusic(Gdx.files.internal(theme));
		currentTheme.setVolume(themeVolume);
	}
	
	public void loopTheme(boolean loop) {
		if (currentTheme != null) {
			currentTheme.setLooping(loop);
		}
	}
	
	public void playTheme() {
		currentTheme.play();
	}
	
	// theme vol stuff =======
	public float getThemeVolume() {
		return themeVolume;
	}
	
	public void setThemeVolume(float vol) {
		GGJ2015.config.put("volume", String.valueOf(vol));
		themeVolume = vol;
		currentTheme.setVolume(themeVolume);
	}
	
	public void increaseThemeVolume(float amt) {
		setThemeVolume(themeVolume + amt);
	}
	
	// fx vol stuff =======
	public float getFxVolume() {
		return fxVolume;
	}
	
	public void setFxVolume(float vol) {
		fxVolume = vol;
	}
	
	public void increaseFxVolume(float amt) {
		setThemeVolume(fxVolume + amt);
	}
	
	public void playSound(Sound sound) {
		sound.play(fxVolume);
	}
}
