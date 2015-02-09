package me.sheetcoldgames.ggj2015.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import me.sheetcoldgames.ggj2015.GGJ2015;
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
	
	private AudioController audioController;
	public boolean isFinished = false;
	public OrthographicCamera camera;
	Music menuTheme;

	public Input input;

	public ArrayList<Rectangle> buttons;
	public ArrayList<Rectangle> buttonsMP;

	public boolean isHost;

	public boolean online = false;
	public boolean lan = false;
	public boolean handleType = false;
	public boolean waitConnetion = false;
	
	public float selectorYPos;
	
	public String[] mainMenuOptions = {"Single", "Online", "Lan", "Exit"};
	public String[] lanMenuOptions = {"Host", "Join", "Back"};
	
	public String hostWaitText = "Waiting For Client...";
	
	private int currentOption = 0;
	
	public float mainMenuInitialY = 123f;
	public float lanInitialY = 175f;
	
	public StringBuffer strIp;
	private StringBuffer strLanIp;
	private StringBuffer strOnlineIp;
	private StringBuffer strHostIp;
	
	public StringBuffer currentMessage;
	
	// total times
	public final float logoTotalTime = 1 / 3f;
	public final float SELECT_TOTAL_TIME = 1 / 4f;
	
	public final float MESSAGE_TOTAL_TIME = 5f;

	// delta time
	public float logoStateTime;
	public float selectStateTime;
	
	public float messageStateTime;
	
	// CONSTANTS
	
	/* symbolic constant ahead, don't change me, pls */ public static final int SPACE_GAP_BETWEEN_IN_PADDING_EMPTY_BLOCK_IM_GOING_TO_CUM_COME_AND_I_WILL_THROW_YOU_THE_D = 30;
	public static final float SEU_PEQUENO_MUNDINHO_SERÁ_ABALADO = 30f;
	/** DARE TO CHANGE IT */
	public static final float THE_NUMBER_23 = 23f;
	public static final float VI = 6f;
	
	public static final int SINGLE_PLAYER_OPTION = 0;
	public static final int ONLINE_OPTION = 1;
	public static final int LAN_OPTION = 2;
	public static final int EXIT_OPTION = 3;
	
	public static final int HOST_OPTION = 0;
	public static final int CLIENT_OPTION = 1;
	public static final int BACK_OPTION = 2;

	public MenuController(Input input) {
		camera = new OrthographicCamera(640f, 480f);
		camera.position.set(camera.viewportWidth / 2f,
				camera.viewportHeight / 2f, 0f);
		camera.update();

		this.input = input;

		menuTheme = Gdx.audio.newMusic(Gdx.files.internal("menu_theme.mp3"));
		menuTheme.setLooping(true);
		menuTheme.setVolume(0f);
		menuTheme.play();
		
		currentMessage = new StringBuffer();
		
		strIp = new StringBuffer();
		strLanIp = new StringBuffer(GGJ2015.prefs.getString("lastLanIp"));
		strOnlineIp = new StringBuffer(GGJ2015.prefs.getString("lastOnlineIp"));
		strHostIp = new StringBuffer(GGJ2015.prefs.getString("lastHostIp"));
		
		audioController = new AudioController();
		audioController.loadTheme(AudioController.MENU_THEME);
		audioController.loopTheme(true);
		audioController.playTheme();
	}

	public void dispose() {
		audioController.dispose();
		
		GGJ2015.prefs.putString("lastLanIp", strLanIp.toString());
		GGJ2015.prefs.putString("lastOnlineIp", strOnlineIp.toString());
		GGJ2015.prefs.putString("lastHostIp", strHostIp.toString());
		GGJ2015.prefs.flush();
	}

	public void update() {
		logoStateTime += Gdx.graphics.getDeltaTime();
		if (logoStateTime > logoTotalTime) {
			selectStateTime += Gdx.graphics.getDeltaTime();
		}
		handleInput();
		
		if (currentMessage.length() > 0 && messageStateTime < MESSAGE_TOTAL_TIME) {
			messageStateTime += Gdx.graphics.getDeltaTime();
		} else if (currentMessage.length() > 0) {
			currentMessage.delete(0, currentMessage.length());
			messageStateTime = 0f;
		} else {
			messageStateTime = 0f;
		}
	}

	private void handleInput() {	
		if (online) {
			onlineMenu();
		} else if (lan) {
			lanMenu();
		} else {
			mainMenu();
		}
	}
	
	/**
	 * Moves the selector based on the option and key pressed
	 * @param currentOption option currently hovered over
	 * @param numberOfOptions total options of the current menu
	 * @param down true if the cursor is going down
	 * @return
	 */
	private int moveSelector(int currentOption, int numberOfOptions, boolean down) {
		input.releaseAllKeys();
		selectStateTime = 0;
		if (down) {
			if (currentOption < numberOfOptions) {
				++currentOption;
			}
		} else {
			if (currentOption > 0) {
				--currentOption;
			}
		}
		
		return currentOption;
	}
	
	private void mainMenu() {		
		if (input.buttons[Input.DOWN]) {
			currentOption = moveSelector(currentOption, mainMenuOptions.length-1, true);
		} else if (input.buttons[Input.UP]) {
			currentOption = moveSelector(currentOption, mainMenuOptions.length-1, false);
		}
		
		selectorYPos = mainMenuInitialY - currentOption * SEU_PEQUENO_MUNDINHO_SERÁ_ABALADO;
		
		if (input.buttons[Input.ENTER]) {
			input.releaseAllKeys();
			if (currentOption == SINGLE_PLAYER_OPTION) {
				finish();
			} else if (currentOption == ONLINE_OPTION) { 
				online = true;
			} else if (currentOption == LAN_OPTION) {
				lan = true;
			} else if (currentOption == EXIT_OPTION) {
				dispose();
				Gdx.app.exit();
			}
			currentOption = 0;
		}
		
	}
	
	public StringBuffer myIp;
	
	public int IP_LENGTH = 15;
	public int IP_BLOCK_LENGTH = 3;
	
	public float onlineInitialY = 175f;
	
	private void lanMenu() {
		
		if(handleType){
			typeIp();
		} else{
		
			if (input.buttons[Input.DOWN]) {
				currentOption = moveSelector(currentOption, 2, true);
			} else if (input.buttons[Input.UP]) {
				currentOption = moveSelector(currentOption, 2, false);
			}
			
			selectorYPos = lanInitialY - currentOption * SEU_PEQUENO_MUNDINHO_SERÁ_ABALADO;
			
			if (input.buttons[Input.ENTER]) {
				input.releaseAllKeys();
				if (currentOption == HOST_OPTION) {
					isHost = true;
					waitConnetion = true;
					getIp();
				} else if (currentOption == CLIENT_OPTION) {
					isHost = false;
					handleType = true;
					
					resetStrIp(strLanIp);
				} else if (currentOption == BACK_OPTION) {
					lan = false;
				} else {
					dispose();
				}
				currentOption = 0;
			}
		}
	}
	
	private void resetStrIp(StringBuffer nStr) {
		if (strIp.length() > 0) strIp.delete(0, strIp.length());
		strIp.append(nStr);
	}
	
	public void onlineMenu(){
		
		if(handleType){
			typeIp();
		} else {
		
			if (input.buttons[Input.DOWN]) {
				currentOption = moveSelector(currentOption, 2, true);
			} else if (input.buttons[Input.UP]) {
				currentOption = moveSelector(currentOption, 2, false);
			}
			
			selectorYPos = lanInitialY - currentOption * SEU_PEQUENO_MUNDINHO_SERÁ_ABALADO;
			
			if (input.buttons[Input.ENTER]) {
				input.releaseAllKeys();
				if (currentOption == HOST_OPTION) {
					isHost = true;
					handleType = true;
					
					resetStrIp(strHostIp);
				} else if (currentOption == CLIENT_OPTION) {
					isHost = false;
					handleType = true;
					
					resetStrIp(strOnlineIp);
				} else if (currentOption == BACK_OPTION) {
					online = false;
				} else {
					dispose();
				}
				currentOption = 0;
			}
		}
	}
	
	public void typeIp(){
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
		if(input.buttons[Input.ENTER]){
			waitConnetion = true;
			finish();
		} else if (input.buttons[Input.ESCAPE]) {
			handleType = false;
		}
	}
	
	public void getIp(){
		try {
			myIp = new StringBuffer(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			System.out.println("Cold not get the host address.");
		}
		finish();
	}
	
	private void finish() {
		isFinished = true;
		if (online) {
			if (isHost) {
				strHostIp = strIp;
			} else {
				strOnlineIp = strIp;
			}
		} else if (lan) {
			strLanIp = strIp;
		}
		menuTheme.stop();
	}
}
