package me.sheetcoldgames.ggj2015;

import me.sheetcoldgames.ggj2015.controller.MenuController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MenuRenderer {

	MenuController controller;
	ShapeRenderer sr;
	SpriteBatch sb;
	Texture background;
	Texture logoTexture;
	Texture selectTexture;
	Animation logoAnim;
	Animation selecAnim;
	BitmapFont font;
	
	
	

	float state;
	public static float state2;

	public MenuRenderer(MenuController controller) {
		this.controller = controller;

		sr = new ShapeRenderer();

		sb = new SpriteBatch();

		background = new Texture("Background.png");

		logoTexture = new Texture("Logo.png");
		TextureRegion logo[] = TextureRegion.split(logoTexture, 144, 96)[0];
		logoAnim = new Animation(1 / 9f, logo);
		logoAnim.setPlayMode(PlayMode.NORMAL);

		// selectTexture = new Texture("Selecao.png");
		selectTexture = new Texture("selection.png");
		TextureRegion sel[] = TextureRegion.split(selectTexture, selectTexture.getWidth()/6, selectTexture.getHeight())[0];
		selecAnim = new Animation(1 / 6f, sel);
		selecAnim.setPlayMode(PlayMode.NORMAL);

		font = new BitmapFont(Gdx.files.internal("inconsolatabmp.fnt"),
				Gdx.files.internal("inconsolatabmp_0.png"), false);
		font.setScale(1f);
		// font.setScale(1f, 1f);
	}

	public void dispose() {
		sr.dispose();
	}

	public void render() {
		sr.setProjectionMatrix(controller.camera.combined);
		sb.setProjectionMatrix(controller.camera.combined);
		sb.begin();
		clearScreen();
		background();
		if (controller.menuMp) {
			textMp();
		} else {
			Anim();
		}
		sb.end();
		
		sr.begin(ShapeType.Line);
		// renderGrid(sr);
		sr.end();
	}

	public void background() {
		// sb.begin();
		// sb.draw(background, 0, 0);
		sb.draw(background, 0f, 0f, 640f, 480f);
		// sb.end();
	}

	TextureRegion currentFrame;

	public void Anim() {
		// sb.setProjectionMatrix(controller.camera.combined);
		// sb.begin();
		state += (Gdx.graphics.getDeltaTime() * 1.5f);
		currentFrame = logoAnim.getKeyFrame(state);

		sb.draw(currentFrame, 
				controller.camera.viewportWidth/2f-currentFrame.getRegionWidth()/2f,
				controller.camera.viewportHeight/2f+currentFrame.getRegionHeight()/2f, 
				currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
		// sb.end();
		if (logoAnim.isAnimationFinished(state)) {
			text();
		}
	}

	TextureRegion atual;

	private void select(float posH, float posV) {
		// sb.begin();
		state2 += (Gdx.graphics.getDeltaTime() * 2.0);
		atual = selecAnim.getKeyFrame(state2);
		// top bar
		// sb.draw(atual, posH+atual.getRegionWidth()/2f, posV+atual.getRegionHeight()*3/4f, atual.getRegionWidth()*9/5f+1f, atual.getRegionHeight());
		sb.draw(atual, posH-atual.getRegionWidth(), posV+atual.getRegionHeight()*3/4f, atual.getRegionWidth()*2, atual.getRegionHeight());
		// bottom bar
		// sb.draw(atual, posH+atual.getRegionWidth()/2f, posV+atual.getRegionHeight(), atual.getRegionWidth()*9/5f+1f, -atual.getRegionHeight());
		sb.draw(atual, posH-atual.getRegionWidth(), posV+atual.getRegionHeight(), atual.getRegionWidth()*2, -atual.getRegionHeight());
		// sb.end();

	}

	private void clearScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	String host = "Host";
	String join = "Join";
	String mp = "Multiplayer";

	private void textMp() {
		// sb.setProjectionMatrix(controller.camera.combined);
		// sb.begin();
		font.drawMultiLine(sb, mp, controller.camera.viewportWidth/2f-mp.length()*6, 420);// 73
		font.drawMultiLine(sb, host, controller.camera.viewportWidth/2f-host.length()*6, 200);
		font.drawMultiLine(sb, join, controller.camera.viewportWidth/2f-join.length()*6, 167);
		// sb.end();
		// select(controller.mpSelH-120, controller.mpSelV);
		select(controller.camera.viewportWidth/2f, controller.mpSelV);
	}

	String sp = "Single";
	String exit = "Exit";

	private void text() {
		// sb.begin();
		font.drawMultiLine(sb, sp, controller.camera.viewportWidth/2f-sp.length()*6, 123);
		font.drawMultiLine(sb, mp, controller.camera.viewportWidth/2f-mp.length()*6, 93);
		font.drawMultiLine(sb, exit, controller.camera.viewportWidth/2f-exit.length()*6, 63);// 73
		// sb.end();
		// select(controller.posSelH, controller.posSelV);
		select(controller.camera.viewportWidth/2f, controller.posSelV);
	}
	
	Color gridColor = new Color(.2f, .2f, .2f, .1f);
	
	/**
	 * Renders a grid that follows the editor no matter what
	 * It has 1 meter of size
	 * @param renderer a initialized renderer to draw the grid
	 */
	private void renderGrid(ShapeRenderer renderer) {
		int posX = 0; // (int) (controller.camera.position.x - controller.camera.viewportWidth/2f - 1f);
		int posY = 0; // (int) (controller.camera.position.y - controller.camera.viewportHeight/2f - 1f);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.setColor(gridColor);
		for (int x = posX; x < posX + controller.camera.viewportWidth; x += 16) {
			for (int y = posY; y < posY + controller.camera.viewportHeight; y += 16) {
				// renderer.rect(x, y, 16, 16);
				renderer.line(x, y, x, y+controller.camera.viewportHeight);
				// renderer.line(x, y, x+controller.camera.viewportWidth, y);
			}
		}
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

}