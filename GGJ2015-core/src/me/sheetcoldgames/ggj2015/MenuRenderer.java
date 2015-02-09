package me.sheetcoldgames.ggj2015;

import me.sheetcoldgames.ggj2015.controller.MenuController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class MenuRenderer {

	MenuController controller;
	SpriteBatch sb;

	Texture background;
	Texture logoTexture;
	Texture selectTexture;
	Animation logoAnim;
	Animation selecAnim;
	BitmapFont font;

	public MenuRenderer(MenuController controller) {
		this.controller = controller;

		sb = new SpriteBatch();

		background = new Texture("Background.png");

		logoTexture = new Texture("Logo.png");
		TextureRegion logo[] = TextureRegion.split(logoTexture, 144, 96)[0];
		logoAnim = new Animation(controller.logoTotalTime / logo.length, logo);
		logoAnim.setPlayMode(PlayMode.NORMAL);

		selectTexture = new Texture("selection.png");
		TextureRegion sel[] = TextureRegion.split(selectTexture,
				selectTexture.getWidth() / 6, selectTexture.getHeight())[0];
		selecAnim = new Animation(controller.SELECT_TOTAL_TIME / sel.length,
				sel);
		selecAnim.setPlayMode(PlayMode.NORMAL);

		font = new BitmapFont(Gdx.files.internal("inconsolatabmp.fnt"),
				Gdx.files.internal("inconsolatabmp_0.png"), false);
		font.setScale(1f);
	}

	public void dispose() {
		sb.dispose();
	}

	public void setController(MenuController controller) {
		this.controller = controller;
	}
	
	public void render() {
		sb.setProjectionMatrix(controller.camera.combined);
		sb.begin();
		clearScreen();
		background();
		if (controller.lan) {
			lanMenu();
		} else if (controller.online) {
			onlineMenu();
		} else if (logoAnim.isAnimationFinished(controller.logoStateTime)) {
			mainMenu();
			renderMessage();
		} else {
			introMenuAnimation();
		}
		
		sb.end();
	}

	public void background() {
		sb.draw(background, 0f, 0f, 640f, 480f);
	}
	
	private void renderMessage() {
		if (MathUtils.sin(controller.logoStateTime * 6) > 0f) {
			font.drawMultiLine(sb, controller.currentMessage,
				controller.camera.viewportWidth/2f - controller.currentMessage.length() * MenuController.VI, 420);
		}
	}

	TextureRegion currentFrame;

	public void introMenuAnimation() {
		currentFrame = logoAnim.getKeyFrame(controller.logoStateTime);

		sb.draw(currentFrame,
				controller.camera.viewportWidth / 2f
						- currentFrame.getRegionWidth() / 2f,
				controller.camera.viewportHeight / 2f
						+ currentFrame.getRegionHeight() / 2f,
				currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
	}

	TextureRegion atual;

	private void select(float posH, float posV) {
		atual = selecAnim.getKeyFrame(controller.selectStateTime);
		// top bar
		sb.draw(atual, posH - atual.getRegionWidth(),
				posV + atual.getRegionHeight() * 3 / 4f,
				atual.getRegionWidth() * 2, atual.getRegionHeight());
		// bottom bar
		sb.draw(atual, posH - atual.getRegionWidth(),
				posV + atual.getRegionHeight(), atual.getRegionWidth() * 2,
				-atual.getRegionHeight());

	}

	private void clearScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	private void drawMenuOptions(String[] options, float initialY) {
		for (int k = 0; k < options.length; k++) {
			font.drawMultiLine(sb, options[k],
					controller.camera.viewportWidth / 2f - options[k].length() * 6,
					initialY + MenuController.THE_NUMBER_23 - MenuController.SEU_PEQUENO_MUNDINHO_SERÁ_ABALADO * k);
		}
		select(controller.camera.viewportWidth / 2f, controller.selectorYPos);
	}
	
	private void mainMenu() {
		introMenuAnimation();
		
		drawMenuOptions(controller.mainMenuOptions, controller.mainMenuInitialY);
	}
	
	private void lanMenu() {
		// Header of the menu
		font.drawMultiLine(sb, controller.mainMenuOptions[MenuController.LAN_OPTION],
				controller.camera.viewportWidth / 2f - controller.mainMenuOptions[MenuController.LAN_OPTION].length() * 6, 420);// 73
		
		drawMenuOptions(controller.lanMenuOptions, controller.lanInitialY);
		
		// current ip typed
		if(controller.handleType){
			font.drawMultiLine(sb, controller.strIp, controller.camera.viewportWidth / 2f
				- controller.strIp.length() * 6, 320);
		} else if(controller.waitConnetion){
			font.drawMultiLine(sb, controller.myIp, controller.camera.viewportWidth / 2f
					- controller.myIp.length() * 6, 320);
			
			font.drawMultiLine(sb, controller.hostWaitText, controller.camera.viewportWidth / 2f
					- controller.hostWaitText.length() * 6, 290);
		}
	}
	
	private void onlineMenu() {
		// Header of the menu
		font.drawMultiLine(sb, controller.mainMenuOptions[MenuController.ONLINE_OPTION],
				controller.camera.viewportWidth / 2f - controller.mainMenuOptions[MenuController.ONLINE_OPTION].length() * 6, 420);// 73
		
		drawMenuOptions(controller.lanMenuOptions, controller.lanInitialY);
		
		// current ip typed
		if(controller.handleType){
			font.drawMultiLine(sb, controller.strIp, controller.camera.viewportWidth / 2f
				- controller.strIp.length() * 6, 320);
		} 
		if(controller.waitConnetion) {
			
			font.drawMultiLine(sb, controller.hostWaitText, controller.camera.viewportWidth / 2f
					- controller.hostWaitText.length() * 6, 290);
		}
	}
}