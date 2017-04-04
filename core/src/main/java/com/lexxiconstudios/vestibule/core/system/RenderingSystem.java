package com.lexxiconstudios.vestibule.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lexxiconstudios.vestibule.core.component.Position;
import com.lexxiconstudios.vestibule.core.component.LXSprite;

public class RenderingSystem extends EntityProcessingSystem {

	@Wire
	private SpriteBatch batch;
	@Wire
	private OrthographicCamera mainCamera;

	ComponentMapper<Position> positionMapper;
	ComponentMapper<LXSprite> spriteMapper;

	public RenderingSystem() {
		super(Aspect.all(Position.class, LXSprite.class));
	}

	@Override
	protected void begin() {
		super.begin();
		batch.begin();
		batch.setProjectionMatrix(mainCamera.combined);
	}

	@Override
	protected void end() {
		super.end();
		batch.end();
	}

	@Override
	protected void process(Entity e) {
		LXSprite sprite = spriteMapper.get(e);
		Position pos = positionMapper.get(e);
		sprite.get().setPosition(pos.getX(), pos.getY());
		sprite.get().setOrigin(0, 0);
		sprite.get().rotate(pos.getRotation() - sprite.get().getRotation());
		sprite.get().draw(batch);
	}

}
