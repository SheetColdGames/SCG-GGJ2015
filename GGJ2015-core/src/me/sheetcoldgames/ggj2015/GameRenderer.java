package me.sheetcoldgames.ggj2015;

import me.sheetcoldgames.ggj2015.controller.GameController;
import me.sheetcoldgames.ggj2015.engine.ACTION;
import me.sheetcoldgames.ggj2015.engine.DIRECTION;
import me.sheetcoldgames.ggj2015.engine.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;

public class GameRenderer {
	
	GameController controller;
	ShapeRenderer sr;
	
	SpriteBatch sb;
	
	// MAP OF THE GAME
	private OrthogonalTiledMapRenderer mapRenderer;
	
	// ======= SPRITESHEETS =======
	// GIRL =======
	Texture girlSpritesheet;
	Animation girlDownAnim;
	Animation girlUpAnim;
	Animation girlRightAnim;
	Animation girlLeftAnim;
	// =======
	
	// G976 =======
	Texture robotSpritesheet;
	Animation robotDownAnim;
	Animation robotUpAnim;
	Animation robotRightAnim;
	Animation robotLeftAnim;
	// =======
	
	// G976 =======
	Texture soldierSpritesheet;
	Animation soldierDownAnim;
	Animation soldierUpAnim;
	Animation soldierRightAnim;
	Animation soldierLeftAnim;
	// =======
	
	public GameRenderer(GameController controller) {
		this.controller = controller;
		sr = new ShapeRenderer();
		
		sb = new SpriteBatch();
		
		entColors = new Color[controller.aEntity.size()];
		for (int k = 0; k < controller.aEntity.size(); k++) {
			entColors[k] = new Color(
					MathUtils.random()*.3f+.3f,
					MathUtils.random()*.3f+.4f,
					MathUtils.random()*.3f+.4f, 1f);
		}
		
		initGirlAnimations();
		initRobotAnimations();
		initSoldiersAnimations();
		
		// Initializing the map
		mapRenderer = new OrthogonalTiledMapRenderer(controller.map, 1f/16f);
	}
	
	public void dispose() {
		sr.dispose();
	}
	
	private void initGirlAnimations() {
		girlSpritesheet = new Texture("girl_spritesheet.png");
		
		TextureRegion downRegions[] = 
				TextureRegion.split(girlSpritesheet, 
						(int) (Constants.GIRL_WIDTH * Constants.TILE_SIZE), 
						(int) (Constants.GIRL_HEIGHT * Constants.TILE_SIZE))[0];
		girlDownAnim = new Animation(1/6f, downRegions);
		girlDownAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion upRegions[] = TextureRegion.split(girlSpritesheet,
				(int) (Constants.GIRL_WIDTH * Constants.TILE_SIZE), 
				(int) (Constants.GIRL_HEIGHT * Constants.TILE_SIZE))[1];
		girlUpAnim = new Animation(1/6f, upRegions);
		girlUpAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion rightRegions[] = TextureRegion.split(girlSpritesheet,
				(int) (Constants.GIRL_WIDTH * Constants.TILE_SIZE), 
				(int) (Constants.GIRL_HEIGHT * Constants.TILE_SIZE))[2];
		girlRightAnim = new Animation(1/6f, rightRegions);
		girlRightAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion leftRegions[] = TextureRegion.split(girlSpritesheet,
				(int) (Constants.GIRL_WIDTH * Constants.TILE_SIZE), 
				(int) (Constants.GIRL_HEIGHT * Constants.TILE_SIZE))[2];
		
		for (int i = 0; i < leftRegions.length; i++) {
			leftRegions[i].flip(true, false);
		}
		girlLeftAnim = new Animation(1/6f, leftRegions);
		girlLeftAnim.setPlayMode(PlayMode.LOOP);
	}
	
	private void initRobotAnimations() {
		robotSpritesheet = new Texture("g976_spritesheet.png");
		
		TextureRegion downRegions[] = TextureRegion.split(robotSpritesheet, 32, 48)[0];
		robotDownAnim = new Animation(1/6f, downRegions);
		robotDownAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion upRegions[] = TextureRegion.split(robotSpritesheet, 32, 48)[1];
		robotUpAnim = new Animation(1/6f, upRegions);
		robotUpAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion rightRegions[] = TextureRegion.split(robotSpritesheet, 32, 48)[2];
		robotRightAnim = new Animation(1/6f, rightRegions);
		robotRightAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion leftRegions[] = TextureRegion.split(robotSpritesheet, 32, 48)[2];
		
		for (int i = 0; i < leftRegions.length; i++) {
			leftRegions[i].flip(true, false);
		}
		robotLeftAnim = new Animation(1/6f, leftRegions);
		robotLeftAnim.setPlayMode(PlayMode.LOOP);
	}
	
	private void initSoldiersAnimations() {
		soldierSpritesheet = new Texture("soldier_spritesheet.png");
		
		TextureRegion downRegions[] = TextureRegion.split(soldierSpritesheet, 24, 32)[0];
		soldierDownAnim = new Animation(1/6f, downRegions);
		soldierDownAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion upRegions[] = TextureRegion.split(soldierSpritesheet, 24, 32)[1];
		soldierUpAnim = new Animation(1/6f, upRegions);
		soldierUpAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion rightRegions[] = TextureRegion.split(soldierSpritesheet, 24, 32)[2];
		soldierRightAnim = new Animation(1/6f, rightRegions);
		soldierRightAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion leftRegions[] = TextureRegion.split(soldierSpritesheet, 24, 32)[2];
		
		for (int i = 0; i < leftRegions.length; i++) {
			leftRegions[i].flip(true, false);
		}
		soldierLeftAnim = new Animation(1/6f, leftRegions);
		soldierLeftAnim.setPlayMode(PlayMode.LOOP);
	}
	
	public void render(boolean debug) {
		clearScreen();
		
		mapRenderer.setView(controller.girlCamera);
		mapRenderer.render(controller.backgroundLayer);
		
		if (debug) {
			debugRender();
		}
		
		sb.setProjectionMatrix(controller.girlCamera.combined);
		sb.begin();
		
		for (Entity ent : controller.aEntity) {
			TextureRegion currentFrame;
			if (ent.id == Constants.GIRL_ID) {
				currentFrame = currentGirlFrame(ent);
			} else if (ent.id == Constants.ROBOT_ID) {
				currentFrame = currentRobotFrame(ent);
			} else {
				currentFrame = currentSoldierFrame(ent);
			}
			sb.draw(currentFrame,
					ent.position.x - ent.width/2f, ent.position.y - ent.height/2f,
					ent.width, ent.height);
		}
		
//		sb.draw(girlDownAnim.getKeyFrame(0),
//				controller.aEntity.get(controller.currentGirlIndex).position.x
//				-controller.aEntity.get(controller.currentGirlIndex).width/2f,
//				controller.aEntity.get(controller.currentGirlIndex).position.y
//				-controller.aEntity.get(controller.currentGirlIndex).height/2f,
//				controller.aEntity.get(controller.currentGirlIndex).width,
//				controller.aEntity.get(controller.currentGirlIndex).height);
//		
//		sb.draw(robotDownAnim.getKeyFrame(0),
//				controller.aEntity.get(controller.currentRobotIndex).position.x
//				-controller.aEntity.get(controller.currentRobotIndex).width/2f,
//				controller.aEntity.get(controller.currentRobotIndex).position.y
//				-controller.aEntity.get(controller.currentRobotIndex).height/2f,
//				controller.aEntity.get(controller.currentRobotIndex).width,
//				controller.aEntity.get(controller.currentRobotIndex).height);
		sb.end();
		mapRenderer.render(controller.foregroundLayer);
	}
	
	private TextureRegion currentGirlFrame(Entity ent) {
		if (ent.action == ACTION.WALK) {
			if (ent.verticalDir == DIRECTION.UP) {
				return girlUpAnim.getKeyFrame(ent.stateTime);
			} else {
				if (ent.horizontalDir == DIRECTION.RIGHT) {
					return girlRightAnim.getKeyFrame(ent.stateTime);
				} else if (ent.horizontalDir == DIRECTION.LEFT) {
					return girlLeftAnim.getKeyFrame(ent.stateTime);
				} else {
					return girlDownAnim.getKeyFrame(ent.stateTime);
				}
			}
		} else if (ent.action == ACTION.IDLE) {
			if (ent.verticalDir == DIRECTION.UP) {
				return girlUpAnim.getKeyFrame(0f);
			} else {
				if (ent.horizontalDir == DIRECTION.RIGHT) {
					return girlRightAnim.getKeyFrame(0f);
				} else if (ent.horizontalDir == DIRECTION.LEFT) {
					return girlLeftAnim.getKeyFrame(0f);
				} else {
					return girlDownAnim.getKeyFrame(0f);
				}
			}
		}
		return null;
	}
	
	private TextureRegion currentRobotFrame(Entity ent) {
		if (ent.action == ACTION.WALK) {
			if (ent.verticalDir == DIRECTION.UP) {
				return robotUpAnim.getKeyFrame(ent.stateTime);
			} else {
				if (ent.horizontalDir == DIRECTION.RIGHT) {
					return robotRightAnim.getKeyFrame(ent.stateTime);
				} else if (ent.horizontalDir == DIRECTION.LEFT) {
					return robotLeftAnim.getKeyFrame(ent.stateTime);
				} else {
					return robotDownAnim.getKeyFrame(ent.stateTime);
				}
			}
		} else if (ent.action == ACTION.IDLE) {
			if (ent.verticalDir == DIRECTION.UP) {
				return robotUpAnim.getKeyFrame(0f);
			} else {
				if (ent.horizontalDir == DIRECTION.RIGHT) {
					return robotRightAnim.getKeyFrame(0f);
				} else if (ent.horizontalDir == DIRECTION.LEFT) {
					return robotLeftAnim.getKeyFrame(0f);
				} else {
					return robotDownAnim.getKeyFrame(0f);
				}
			}
		}
		return null;
	}
	
	private TextureRegion currentSoldierFrame(Entity ent) {
		if (ent.action == ACTION.WALK) {
			if (ent.verticalDir == DIRECTION.UP) {
				return soldierUpAnim.getKeyFrame(ent.stateTime);
			} else {
				if (ent.horizontalDir == DIRECTION.RIGHT) {
					return soldierRightAnim.getKeyFrame(ent.stateTime);
				} else if (ent.horizontalDir == DIRECTION.LEFT) {
					return soldierLeftAnim.getKeyFrame(ent.stateTime);
				} else {
					return soldierDownAnim.getKeyFrame(ent.stateTime);
				}
			}
		} else if (ent.action == ACTION.IDLE) {
			if (ent.verticalDir == DIRECTION.UP) {
				return soldierUpAnim.getKeyFrame(0f);
			} else {
				if (ent.horizontalDir == DIRECTION.RIGHT) {
					return soldierRightAnim.getKeyFrame(0f);
				} else if (ent.horizontalDir == DIRECTION.LEFT) {
					return soldierLeftAnim.getKeyFrame(0f);
				} else {
					return soldierDownAnim.getKeyFrame(0f);
				}
			}
		}
		return null;
	}
	
	private void clearScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	private void debugRender() {
		sr.setProjectionMatrix(controller.girlCamera.combined);
		
		sr.begin(ShapeType.Filled);
		renderEntities();
		sr.end();
		
		sr.begin(ShapeType.Line);
		entityCollisionRenderer();
		renderMapPoints();
		sr.end();
	}
	
	Color[] entColors;
	
	private void renderEntities() {
		sr.setColor(Colors.PLAYER);
		int i = 0;
		for (Entity ent : controller.aEntity) {
			sr.setColor(entColors[i]);
			sr.rect(ent.position.x-ent.width/2f,
					ent.position.y-ent.height/2f,
					ent.width, ent.height);
			++i;
		}
	}
	
	private void entityCollisionRenderer() {
		for (Entity ent : controller.aEntity) {
			renderCollisionBounds(ent);
			renderPatrolPoints(ent);
		}
	}
	
	private void renderCollisionBounds(Entity ent) {
		sr.setColor(Color.RED);
		// horizontal top line
		sr.line(ent.position.x - ent.width/2f - Math.abs(ent.velocity.x) - ent.offset,
				ent.position.y + ent.height/2f,
				ent.position.x + ent.width/2f + Math.abs(ent.velocity.x) + ent.offset,
				ent.position.y + ent.height/2f);
		
		// horizontal bottom line
		sr.line(ent.position.x - ent.width/2f - Math.abs(ent.velocity.x) - ent.offset,
				ent.position.y - ent.height/2f,
				ent.position.x + ent.width/2f + Math.abs(ent.velocity.x) + ent.offset,
				ent.position.y - ent.height/2f);
		
		// vertical left line
		sr.line(ent.position.x - ent.width/2f,
				ent.position.y - ent.height/2f - Math.abs(ent.velocity.y) - ent.offset,
				ent.position.x - ent.width/2f,
				ent.position.y + ent.height/2f + Math.abs(ent.velocity.y) + ent.offset);
		
		// vertical right line
		sr.line(ent.position.x + ent.width/2f,
				ent.position.y - ent.height/2f - Math.abs(ent.velocity.y) - ent.offset,
				ent.position.x + ent.width/2f,
				ent.position.y + ent.height/2f + Math.abs(ent.velocity.y) + ent.offset);
	}
	
	private void renderPatrolPoints(Entity ent) {
		sr.setColor(Color.CYAN);
		if (!ent.patrolPoints.isEmpty()) {
			for (int k = 0; k < ent.patrolPoints.size()-1; k++) {
				// Let's render a circle
				sr.circle(ent.patrolPoints.get(k).x, ent.patrolPoints.get(k).y,
						.25f, 16);
				// Let's render a line
				sr.line(ent.patrolPoints.get(k), ent.patrolPoints.get(k+1));
			}
			sr.circle(ent.patrolPoints.getLast().x, ent.patrolPoints.getLast().y,
					.25f, 16);
		}
	}
	
	private void renderMapPoints() {
		sr.setColor(Colors.COLLISION_POINT);
		for (int groupIndex = 0; groupIndex < controller.groupPoints.size(); groupIndex++) {
			
			for (int currentIndex = 0; currentIndex < controller.groupPoints.get(groupIndex).size()-1; currentIndex++) {
				// Let's render a circle
				sr.circle(controller.groupPoints.get(groupIndex).get(currentIndex).pos.x,
						controller.groupPoints.get(groupIndex).get(currentIndex).pos.y,
						.25f, 16);
				// Let's render a line
				sr.line(controller.groupPoints.get(groupIndex).get(currentIndex).pos,
						controller.groupPoints.get(groupIndex).get(currentIndex+1).pos);
			}
			// if this is the last point, then render a circle for it
			sr.circle(controller.groupPoints.get(groupIndex).getLast().pos.x,
					controller.groupPoints.get(groupIndex).getLast().pos.y,
					.25f, 16);
		}
	}
}
