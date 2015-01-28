package me.sheetcoldgames.ggj2015;

import me.sheetcoldgames.ggj2015.controller.MenuController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
		logoAnim = new Animation(1/9f, logo);
		logoAnim.setPlayMode(PlayMode.NORMAL);
		
		selectTexture = new Texture("Selecao.png");
		TextureRegion sel[] = TextureRegion.split(selectTexture,92,35)[0];
		selecAnim = new Animation(1/6f,sel);
		selecAnim.setPlayMode(PlayMode.NORMAL);
		
		font = new BitmapFont(Gdx.files.internal("minecraftia.fnt"), Gdx.files.internal("minecraftia_0.png"), false);
		//font.setScale(1/2.4f);
		font.setScale(1/2.5f, 1/2.7f);
	}
	
	public void dispose() {
		sr.dispose();		
	}
	
	public void render() {
		clearScreen();
		background();
		if(controller.menuMp){
			textMp();
		} else{
			Anim();
		}
		
	}
	
	public void background(){
		sb.begin();
		sb.draw(background,0,0);
		sb.end();
	}
	
	TextureRegion currentFrame;
	public void Anim(){
		sb.setProjectionMatrix(controller.camera.combined);
		sb.begin();
		state += (Gdx.graphics.getDeltaTime()*1.5f);
		currentFrame = logoAnim.getKeyFrame(state);
		
		sb.draw(currentFrame,165,180,144, 96);
		sb.end();
		if (logoAnim.isAnimationFinished(state)){
			text();
		}
	}
	
	TextureRegion atual;
	private void select(float posH, float posV){
		sb.begin();
		state2 += (Gdx.graphics.getDeltaTime()*2.0);
		atual = selecAnim.getKeyFrame(state2);
		sb.draw(atual, posH, posV, 92,35);
		sb.end();
		
	}
	
	private void clearScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	String host = "Host";
	String join = "Join";
	String mp = "Multiplayer";
	
	private void textMp(){
		sb.begin();
		font.drawMultiLine(sb, host, 215, 200);
		font.drawMultiLine(sb, join, 215, 167);
		font.drawMultiLine(sb, mp, 190, 280);//73
		sb.end();
		select(controller.mpSelH,controller.mpSelV);
	}
	
	
	String sp = "Single";
	String exit = "Exit";
	private void text(){		
		sb.begin();
		font.drawMultiLine(sb, sp, 213, 123);
		font.drawMultiLine(sb, mp, 195, 93);
		font.drawMultiLine(sb, exit, 220, 63);//73
		sb.end();
		select(controller.posSelH, controller.posSelV);
	}
	
}