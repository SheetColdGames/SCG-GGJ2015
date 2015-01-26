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
	Texture logot;
	Texture selec;
	Animation logoAnim;
	Animation selecAnim;
	
	float state;
	
	Music music;
	
	public MenuRenderer(MenuController controller) {
		this.controller = controller;
		sr = new ShapeRenderer();
		sb = new SpriteBatch();
		background = new Texture("Background.png");
		logot = new Texture("Logo.png");
		int tam = 144;
		TextureRegion logo[] = TextureRegion.split(logot, tam, 96)[0];
		logoAnim = new Animation(1/9f, logo);
		logoAnim.setPlayMode(PlayMode.NORMAL);
		selec = new Texture("Selecao.png");
		TextureRegion sel[] = TextureRegion.split(selec,92,35)[0];
		selecAnim = new Animation(1/6f,sel);
		selecAnim.setPlayMode(PlayMode.NORMAL);
	}
	
	public void dispose() {
		sr.dispose();		
	}
	
	public void render() {
		if (controller.menuMp){
			clearScreen();
			debugRenderMP();
			//System.out.println("teste");
		}
		else{
			//debugRender();
			//clearScreen();
			rend();
			//System.out.println("teste2");
		}

	}
	
	public void rend(){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		sb.setProjectionMatrix(controller.camera.combined);
		sb.begin();
		sb.draw(background,0,0);
		sb.end();
		sb.begin();
		TextureRegion currentFrame;
		state += (Gdx.graphics.getDeltaTime()*1.5f);
		currentFrame = logoAnim.getKeyFrame(state);
		sb.draw(currentFrame,160,170,144, 96);
		sb.end();
		selectAnimM();
		font();
		
		
		//font();
	}
	public float posSelW = 190f;
	public float posSelH = 100f;
	public int sel = 1;
	public void selectAnimM(){
		sb.begin();
		TextureRegion atual;
		state += (Gdx.graphics.getDeltaTime());
		atual = selecAnim.getKeyFrame(state);
		sb.draw(atual, posSelW, posSelH, 92,35);
		sb.end();
		
		if (MenuController.input.buttons[Input.DOWN]){
			posSelH = 50f;
			sel = 2;
		}
		else if(MenuController.input.buttons[Input.UP]){
			posSelH = 100f;
			sel = 1;
		}
		else if(MenuController.input.buttons[Input.ENTER]){
			if (sel == 1){
				MenuController.isFinished = true;
				music.stop();
				
			} else {
				dispose();
			}
		}
	}
	
	
	private void clearScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	
	
	private void font(){
		BitmapFont font;
		String str = "Single Player";
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("visitor1.ttf"));
		//FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		//parameter.size = 12;
		//BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
		//generator.dispose(); // don't forget to dispose to avoid memory leaks!
		font = new BitmapFont(Gdx.files.internal("minecraftia.fnt"), Gdx.files.internal("minecraftia_0.png"), false);
		font.setScale(1/2f);
		sb.begin();
		font.drawMultiLine(sb, str, 175, 125);
		font.drawMultiLine(sb, "Exit", 210, 75);
		sb.end();
	}
	/*
	private void fonte(){
		SpriteBatch spriteBatch;
		 BitmapFont font;
		 String str = "Single Player";
		 spriteBatch = new SpriteBatch();
		 font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("font.ttf"), FONT_CHARACTERS, 12.5f, 7.5f, 1.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		 font = new BitmapFont(Gdx.files.internal("Calibri.ttf"));
		 spriteBatch.begin();
		 font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		 font.draw(spriteBatch, str, 25, 160);
		 spriteBatch.end();
	}*/
	
	public void debugRender() {
		sr.begin(ShapeType.Filled);
		
		// sr.rect(controller.button.x, controller.button.y, controller.button.width, controller.button.height);
		for (int i = 0; i < controller.buttons.size(); i++) {
			sr.setColor(Color.RED);
			sr.rect(controller.buttons.get(i).x, controller.buttons.get(i).y, controller.buttons.get(i).width, controller.buttons.get(i).height);
		}
		sr.end();
		sr.begin(ShapeType.Line);
		sr.end();	
	}
	
	public void debugRenderMP(){
		sr.begin(ShapeType.Filled);
		
		for (int i = 0; i< controller.buttonsMP.size(); i++){
			sr.setColor(Color.RED);
			sr.rect(controller.buttonsMP.get(i).x, controller.buttonsMP.get(i).y, controller.buttonsMP.get(i).width, controller.buttonsMP.get(i).height);
		}
		sr.end();
		sr.begin(ShapeType.Line);
		sr.end();
		
	}
	/*
	public void animation()}{
		Texture region currentFrame;*/
	}