package me.sheetcoldgames.ggj2015.controller;

import java.util.ArrayList;

import me.sheetcoldgames.ggj2015.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

/**
 * TODO Criar constantes para todos os botões do menu para facilitar a leitura
 * das estruturas condicionais
 * 
 * @author João Vitor
 *
 */
public class MenuController {

	public boolean isFinished = false;
	public OrthographicCamera camera;
	Music menuTheme;

	public Input input;

	public ArrayList<Rectangle> buttons;
	public ArrayList<Rectangle> buttonsMP;

	public boolean isHost;

	public boolean mp = false;

	// delta time
	public float logoStateTime;
	public float selectStateTime;

	public MenuController(Input input) {
		camera = new OrthographicCamera(640f, 480f);
		camera.position.set(camera.viewportWidth / 2f,
				camera.viewportHeight / 2f, 0f);
		camera.update();

		this.input = input;

		mpSelH = camera.viewportWidth / 2f;
		mpSelV = 175f;
		mpOption = 1;

		menuTheme = Gdx.audio.newMusic(Gdx.files.internal("menu_theme.mp3"));
		menuTheme.setLooping(true);
		menuTheme.setVolume(0f);
		menuTheme.play();
		
		strIp = new StringBuffer();
	}

	public void dispose() {

	}

	public final float logoTotalTime = 1 / 3f;
	public final float SELECT_TOTAL_TIME = 1 / 4f;

	public void update() {
		logoStateTime += Gdx.graphics.getDeltaTime();
		if (logoStateTime > logoTotalTime) {
			selectStateTime += Gdx.graphics.getDeltaTime();
		}
		handleInput();
	}

	public float posSelH = 190f;
	public float posSelV = 100f;
	private int option = 1;

	private void handleInput() {

		if (!mp) {
			menuInput();
		} else {
			mpInput();
		}
	}

	private void menuInput() {
		if (option != 3) {
			if (input.buttons[Input.DOWN]) {
				input.releaseAllKeys();
				selectStateTime = 0;
				posSelV = posSelV - 30f;// 50f
				option = option + 1;
			}
		}
		if (option != 1) {
			if (input.buttons[Input.UP]) {
				input.releaseAllKeys();
				selectStateTime = 0;
				posSelV = posSelV + 30f;// 100f
				option = option - 1;
			}
		}
		if (input.buttons[Input.ENTER]) {
			input.releaseAllKeys();
			if (option == 1) {
				finish();
			} else if (option == 2) {
				mp = true;
			} else {
				dispose();
				Gdx.app.exit();
			}
		}
	}

	public float mpSelH = 190f;
	public float mpSelV = 175f;
	private int mpOption = 1;
	
	public StringBuffer strIp;
	
	public int IP_LENGTH = 15;
	public int IP_BLOCK_LENGTH = 3;
	
	private void mpInput() {
		if (mpOption != 3) {
			if (input.buttons[Input.DOWN]) {
				input.releaseAllKeys();
				selectStateTime = 0;
				mpSelV = mpSelV - 30f;// 50f
				mpOption = mpOption + 1;
			}
		}
		if (mpOption != 1) {
			if (input.buttons[Input.UP]) {
				input.releaseAllKeys();
				selectStateTime = 0;
				mpSelV = mpSelV + 30f;// 100f
				mpOption = mpOption - 1;
			}
		}
		if (input.buttons[Input.ENTER]) {
			input.releaseAllKeys();
			if (mpOption == 1) { // ======= HOST BUTTON =======
				// initMP(true);
				isHost = true;
				finish();
				// music.stop();
			} else if (mpOption == 2) { // ======= JOIN BUTTON =======
				// initMP(false);
				isHost = false;
				finish();
			} else if (mpOption == 3) { // ======= BACK BUTTON =======
				mp = false;
			} else {
				dispose();
			}
		}
		
		if (input.hasTyped) {
			if (input.lastCharTyped == '\b') {
				if (strIp.length() > 0) {
					strIp.deleteCharAt(strIp.length()-1);
				}
			} else {
				if (strIp.length() < IP_LENGTH) {
					strIp.append(input.lastCharTyped);
				}
			}
			
			System.out.println("The string length: " + strIp.length() + " and content: " + strIp);
			input.hasTyped = false;
		}
	}
	
	private void finish() {
		isFinished = true;
		menuTheme.stop();
	}
}
