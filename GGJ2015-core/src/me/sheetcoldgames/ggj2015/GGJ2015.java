package me.sheetcoldgames.ggj2015;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;

public class GGJ2015 extends ApplicationAdapter {
	
	public void create() {
		
	}
	
	public void dispose() {
		
	}
	
	public void render() {
		Gdx.gl.glClearColor(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

}
