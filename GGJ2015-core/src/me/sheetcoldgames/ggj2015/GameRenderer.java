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
	
	// Blue Soldier =======
	Texture blueSoldierSpritesheet;
	Animation blueSoldierDownAnim;
	Animation blueSoldierUpAnim;
	Animation blueSoldierRightAnim;
	Animation blueSoldierLeftAnim;
	// =======
	
	// Yellow Soldier =======
	Texture yellowSoldierSpritesheet;
	Animation yellowSoldierDownAnim;
	Animation yellowSoldierUpAnim;
	Animation yellowSoldierRightAnim;
	Animation yellowSoldierLeftAnim;
	
	Animation yellowSoldierDownThrowAnim;
	Animation yellowSoldierUpThrowAnim;
	Animation yellowSoldierLeftThrowAnim;
	Animation yellowSoldierRightThrowAnim;
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
		// ======= BLUE SOLDIER =======
		blueSoldierSpritesheet = new Texture("blue_soldier_spritesheet.png");
		
		TextureRegion blueDownRegions[] = TextureRegion.split(blueSoldierSpritesheet, 24, 32)[0];
		blueSoldierDownAnim = new Animation(1/6f, blueDownRegions);
		blueSoldierDownAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion blueUpRegions[] = TextureRegion.split(blueSoldierSpritesheet, 24, 32)[1];
		blueSoldierUpAnim = new Animation(1/6f, blueUpRegions);
		blueSoldierUpAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion blueRightRegions[] = TextureRegion.split(blueSoldierSpritesheet, 24, 32)[2];
		blueSoldierRightAnim = new Animation(1/6f, blueRightRegions);
		blueSoldierRightAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion blueLeftRegions[] = TextureRegion.split(blueSoldierSpritesheet, 24, 32)[2];
		
		for (int i = 0; i < blueLeftRegions.length; i++) {
			blueLeftRegions[i].flip(true, false);
		}
		blueSoldierLeftAnim = new Animation(1/6f, blueLeftRegions);
		blueSoldierLeftAnim.setPlayMode(PlayMode.LOOP);
		
		// ======= YELLOW SOLDIER =======
		yellowSoldierSpritesheet = new Texture("yellow_soldier_spritesheet.png");
		
		TextureRegion yellowDownRegions[] = TextureRegion.split(yellowSoldierSpritesheet, 24, 32)[0];
		yellowSoldierDownAnim = new Animation(1/6f, yellowDownRegions[0], yellowDownRegions[1],
				yellowDownRegions[2], yellowDownRegions[3]);
		yellowSoldierDownAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion yellowUpRegions[] = TextureRegion.split(yellowSoldierSpritesheet, 24, 32)[1];
		yellowSoldierUpAnim = new Animation(1/6f, yellowUpRegions[0], yellowUpRegions[1],
				yellowUpRegions[2], yellowUpRegions[3]);
		yellowSoldierUpAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion yellowRightRegions[] = TextureRegion.split(yellowSoldierSpritesheet, 24, 32)[2];
		yellowSoldierRightAnim = new Animation(1/6f, yellowRightRegions[0], yellowRightRegions[1],
				yellowRightRegions[2], yellowRightRegions[3]);
		yellowSoldierRightAnim.setPlayMode(PlayMode.LOOP);
		
		TextureRegion yellowLeftRegions[] = TextureRegion.split(yellowSoldierSpritesheet, 24, 32)[2];
		for (int i = 0; i < yellowLeftRegions.length; i++) {
			yellowLeftRegions[i].flip(true, false);
		}
		yellowSoldierLeftAnim = new Animation(1/6f, yellowLeftRegions[0], yellowLeftRegions[1],
				yellowLeftRegions[2], yellowLeftRegions[3]);
		yellowSoldierLeftAnim.setPlayMode(PlayMode.LOOP);
		
		// ======= throwing to the BOTTOM =======
		TextureRegion yellowDownThrowRegions[] = TextureRegion.split(yellowSoldierSpritesheet, 24, 32)[3];
		yellowSoldierDownThrowAnim = new Animation(controller.yellowEnemyAttackDuration/(float) yellowDownThrowRegions.length,
				yellowDownThrowRegions);
		yellowSoldierDownThrowAnim.setPlayMode(PlayMode.NORMAL);
		
		// ======= throwing to the TOP =======
		TextureRegion yellowUpThrowRegions[] = TextureRegion.split(yellowSoldierSpritesheet, 24, 32)[4];
		yellowSoldierUpThrowAnim = new Animation(controller.yellowEnemyAttackDuration/(float) yellowUpThrowRegions.length,
				yellowUpThrowRegions);
		yellowSoldierUpThrowAnim.setPlayMode(PlayMode.NORMAL);
		
		// ======= throwing to the RIGHT =======
		TextureRegion yellowRightThrowRegions[] = TextureRegion.split(yellowSoldierSpritesheet, 24, 32)[5];
		yellowSoldierRightThrowAnim = new Animation(controller.yellowEnemyAttackDuration/yellowRightThrowRegions.length,
				yellowRightThrowRegions);
		yellowSoldierRightThrowAnim.setPlayMode(PlayMode.NORMAL);
		
		// ======= throwing to the LEFT =======
		TextureRegion yellowLeftThrowRegions[] = TextureRegion.split(yellowSoldierSpritesheet, 24, 32)[5];
		for (int i = 0; i < yellowLeftThrowRegions.length; i++) {
			yellowLeftThrowRegions[i].flip(true, false);
		}
		yellowSoldierLeftThrowAnim = new Animation(controller.yellowEnemyAttackDuration / (float) yellowLeftThrowRegions.length,
				yellowLeftThrowRegions);
		yellowSoldierLeftThrowAnim.setPlayMode(PlayMode.NORMAL);
	}
	
	public void render(boolean debug) {
		clearScreen();
		
		mapRenderer.setView(controller.girlCamera);
		mapRenderer.render(controller.backgroundLayer);
		
		if (controller.aEntity.get(controller.currentGirlIndex).position.y < 34f) {
			mapRenderer.render(controller.disposableLayer);
		}
		
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
			} else if (ent.id == Constants.BLUE_ENEMY_AI_ID) {
				currentFrame = currentBlueSoldierFrame(ent);
			} else { // if (ent.id == Constants.YELLOW_ENEMY_AI_ID) {
				currentFrame = currentYellowSoldierFrame(ent);
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
	
	private TextureRegion currentBlueSoldierFrame(Entity ent) {
		if (ent.action == ACTION.WALK) {
			if (ent.verticalDir == DIRECTION.UP) {
				return blueSoldierUpAnim.getKeyFrame(ent.stateTime);
			} else {
				if (ent.horizontalDir == DIRECTION.RIGHT) {
					return blueSoldierRightAnim.getKeyFrame(ent.stateTime);
				} else if (ent.horizontalDir == DIRECTION.LEFT) {
					return blueSoldierLeftAnim.getKeyFrame(ent.stateTime);
				} else {
					return blueSoldierDownAnim.getKeyFrame(ent.stateTime);
				}
			}
		} else if (ent.action == ACTION.IDLE) {
			if (ent.verticalDir == DIRECTION.UP) {
				return blueSoldierUpAnim.getKeyFrame(0f);
			} else {
				if (ent.horizontalDir == DIRECTION.RIGHT) {
					return blueSoldierRightAnim.getKeyFrame(0f);
				} else if (ent.horizontalDir == DIRECTION.LEFT) {
					return blueSoldierLeftAnim.getKeyFrame(0f);
				} else {
					return blueSoldierDownAnim.getKeyFrame(0f);
				}
			}
		}
		return null;
	}
	
	private TextureRegion currentYellowSoldierFrame(Entity ent) {
		if (ent.action == ACTION.ATTACK) {
			if (ent.verticalDir == DIRECTION.UP) {
				return yellowSoldierUpThrowAnim.getKeyFrame(ent.stateTime);
			} else {
				if (ent.horizontalDir == DIRECTION.RIGHT) {
					return yellowSoldierRightThrowAnim.getKeyFrame(ent.stateTime);
				} else if (ent.horizontalDir == DIRECTION.LEFT) {
					return yellowSoldierLeftThrowAnim.getKeyFrame(ent.stateTime);
				} else {
					return yellowSoldierDownThrowAnim.getKeyFrame(ent.stateTime);
				}
			}
		}
		if (ent.action == ACTION.WALK) {
			if (ent.verticalDir == DIRECTION.UP) {
				return yellowSoldierUpAnim.getKeyFrame(ent.stateTime);
			} else {
				if (ent.horizontalDir == DIRECTION.RIGHT) {
					return yellowSoldierRightAnim.getKeyFrame(ent.stateTime);
				} else if (ent.horizontalDir == DIRECTION.LEFT) {
					return yellowSoldierLeftAnim.getKeyFrame(ent.stateTime);
				} else {
					return yellowSoldierDownAnim.getKeyFrame(ent.stateTime);
				}
			}
		} else if (ent.action == ACTION.IDLE) {
			if (ent.verticalDir == DIRECTION.UP) {
				return yellowSoldierUpAnim.getKeyFrame(0f);
			} else {
				if (ent.horizontalDir == DIRECTION.RIGHT) {
					return yellowSoldierRightAnim.getKeyFrame(0f);
				} else if (ent.horizontalDir == DIRECTION.LEFT) {
					return yellowSoldierLeftAnim.getKeyFrame(0f);
				} else {
					return yellowSoldierDownAnim.getKeyFrame(0f);
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
