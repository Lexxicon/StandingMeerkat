package com.lexxiconstudios.vestibule.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.lexxiconstudios.vestibule.core.component.Position;
import com.lexxiconstudios.vestibule.core.component.Sprite;

public class RenderingSystem extends EntityProcessingSystem {

	@Wire
	private SpriteBatch batch;
	@Wire
	private OrthographicCamera mainCamera;

	ComponentMapper<Position> positionMapper;
	ComponentMapper<Sprite> spriteMapper;

	public RenderingSystem() {
		super(Aspect.all(Position.class, Sprite.class));
	}

	@Override
	protected void begin() {
		super.begin();
		batch.begin();
	}
	
	@Override
	protected void end() {
		super.end();
		batch.end();
	}
	
	@Override
	protected void process(Entity e) {
		Sprite sprite = spriteMapper.get(e);
		Position pos = positionMapper.get(e);
		batch.draw(
				sprite.getTexture(), 
				pos.getX(), pos.getY(), 
				sprite.getOriginX(), sprite.getOriginY(),
				sprite.getWidth(), sprite.getHeight(), 
				sprite.getScaleX(), sprite.getScaleY(), 
				pos.getRotation(),
				sprite.getSrcX(), sprite.getSrcY(), 
				sprite.getSrcWidth(), sprite.getSrcHeight(),
				sprite.isFlipX(), sprite.isFlipY());
		

	}

}
