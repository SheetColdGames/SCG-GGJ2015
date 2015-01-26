package me.sheetcoldgames.ggj2015.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

public class SheetCamera extends OrthographicCamera {
Entity target;
	
	public SheetCamera() {
		super();
	}
	
	public SheetCamera(float w, float h) {
		super(w, h);
	}
	
	public void update() {
		if (hasTarget()) {
			position.set(target.position.x, target.position.y, 0f);
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
