package me.sheetcoldgames.ggj2015;



import me.sheetcoldgames.ggj2015.controller.MenuController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class MenuRenderer {
	
	MenuController controller;
	ShapeRenderer sr;
	
	
	public MenuRenderer(MenuController controller) {
		this.controller = controller;
		sr = new ShapeRenderer();
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
			debugRender();
			//System.out.println("teste2");
		}

	}
	
	private void clearScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
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
}