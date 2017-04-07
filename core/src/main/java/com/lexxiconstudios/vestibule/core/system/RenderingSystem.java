package com.lexxiconstudios.vestibule.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.lexxiconstudios.vestibule.core.component.LXSprite;

import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Renderable;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;

public class RenderingSystem extends DeferredEntityProcessingSystem {

	@Wire
	private SpriteBatch batch;
	@Wire
	private CameraSystem cameraSystem;

	ComponentMapper<Pos> positionMapper;
	ComponentMapper<Angle> angleMapper;
	ComponentMapper<LXSprite> spriteMapper;
	RenderBatchingSystem as;

	public RenderingSystem(RenderBatchingSystem renderBatcher) {
		super(Aspect.all(Pos.class, LXSprite.class, Renderable.class), renderBatcher);
	}

	@Override
	protected void begin() {
		super.begin();
		batch.begin();
		batch.setProjectionMatrix(cameraSystem.camera.combined);
	}

	@Override
	protected void end() {
		super.end();
		batch.end();
	}

	@Override
	protected void process(int e) {
		LXSprite sprite = spriteMapper.get(e);
		Vector2 v2 = positionMapper.get(e).xy.cpy().scl(PhysicsSystem.BOX_TO_WORLD);
		Angle a = angleMapper.getSafe(e, Angle.NONE);
		sprite.get().setPosition(v2.x, v2.y);
		sprite.get().setOrigin(0, 0);
		sprite.get().rotate(a.rotation - sprite.get().getRotation());
		sprite.get().draw(batch);

	}

}
