package me.sheetcoldgames.ggj2015.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SheetCamera extends OrthographicCamera {
	Entity target;
	Vector2 minPos;
	Vector2 maxPos;
	
	public SheetCamera() {
		super();
		minPos = new Vector2(0f, 0f);
		maxPos = new Vector2(40f-viewportWidth/2f, 60f-viewportHeight/2f);
	}
	
	public SheetCamera(float w, float h) {
		super(w, h);
		minPos = new Vector2(0f, 0f);
		maxPos = new Vector2(40f-viewportWidth/2f, 60f-viewportHeight/2f);
	}
	
	public void setMinimumPosition(float x, float y) {
		minPos.set(x, y);
	}
	
	public void setMaximumPosition(float x, float y) {
		maxPos.set(x, y);
	}
	
	private float cameraProgress = .1f;
	
	public void setCameraProgress(float val) {
		cameraProgress = val;
	}
	
	public float getCameraProgress() {
		return cameraProgress;
	}
	
	public void update() {
		if (hasTarget()) {
			position.x = MathUtils.lerp(position.x, target.position.x, cameraProgress);
			position.y = MathUtils.lerp(position.y, target.position.y, cameraProgress);
			
			position.x = MathUtils.clamp(position.x, viewportWidth/2f, 40f-viewportWidth/2f);
			position.y = MathUtils.clamp(position.y, viewportHeight/2f, 60f-viewportHeight/2f);
		}
		super.update();
	}
	
	public Entity getTarget() {
		return target;
	}
	
	public boolean hasTarget() {
		return getTarget() != null;
	}
	
	public void setTarget(Entity ent) {
		this.target = ent;
	}
}
