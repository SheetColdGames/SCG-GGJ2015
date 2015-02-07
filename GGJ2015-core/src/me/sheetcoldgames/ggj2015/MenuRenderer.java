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
	}

	public void render() {
		sb.setProjectionMatrix(controller.camera.combined);
		sb.begin();
		clearScreen();
		background();
		if (controller.mp) {
			textMp();
		} else {
			Anim();
		}
		sb.end();
	}

	public void background() {
		sb.draw(background, 0f, 0f, 640f, 480f);
	}

	TextureRegion currentFrame;

	public void Anim() {
		currentFrame = logoAnim.getKeyFrame(controller.logoStateTime);

		sb.draw(currentFrame,
				controller.camera.viewportWidth / 2f
						- currentFrame.getRegionWidth() / 2f,
				controller.camera.viewportHeight / 2f
						+ currentFrame.getRegionHeight() / 2f,
				currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
		if (logoAnim.isAnimationFinished(controller.logoStateTime)) {
			text();
		}
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

	String host = "Host";
	String join = "Join";
	String backText = "Back";
	String mp = "Multiplayer";

	private void textMp() {
		font.drawMultiLine(sb, mp,
				controller.camera.viewportWidth / 2f - mp.length() * 6, 420);// 73
		font.drawMultiLine(sb, host, controller.camera.viewportWidth / 2f
				- host.length() * 6, 198);
		font.drawMultiLine(sb, join, controller.camera.viewportWidth / 2f
				- join.length() * 6, 168);
		font.drawMultiLine(sb, backText, controller.camera.viewportWidth / 2f
				- join.length() * 6, 138);
		select(controller.camera.viewportWidth / 2f, controller.mpSelV);
	}

	String sp = "Single";
	String exit = "Exit";

	private void text() {
		font.drawMultiLine(sb, sp,
				controller.camera.viewportWidth / 2f - sp.length() * 6, 123);
		font.drawMultiLine(sb, mp,
				controller.camera.viewportWidth / 2f - mp.length() * 6, 93);
		font.drawMultiLine(sb, exit, controller.camera.viewportWidth / 2f
				- exit.length() * 6, 63);// 73
		select(controller.camera.viewportWidth / 2f, controller.posSelV);
	}

}