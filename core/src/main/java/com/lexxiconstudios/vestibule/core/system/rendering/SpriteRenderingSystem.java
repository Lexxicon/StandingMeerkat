package com.lexxiconstudios.vestibule.core.system.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.lexxiconstudios.vestibule.core.component.LXSprite;
import com.lexxiconstudios.vestibule.core.component.Offset;
import com.lexxiconstudios.vestibule.core.system.camera.LXViewportSystem;

import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Renderable;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;

public class SpriteRenderingSystem extends DeferredEntityProcessingSystem {

	@Wire
	private SpriteBatch batch;
	@Wire
	private LXViewportSystem cameraSystem;

	ComponentMapper<Pos> positionMapper;
	ComponentMapper<Angle> angleMapper;
	ComponentMapper<LXSprite> spriteMapper;
	ComponentMapper<Offset> offsetMapper;
	RenderBatchingSystem as;

	public SpriteRenderingSystem(RenderBatchingSystem renderBatcher) {
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
		Vector2 v2 = positionMapper.get(e).xy;
		Offset ofs = offsetMapper.getSafe(e, Offset.NONE);
		LXSprite sprite = spriteMapper.get(e);
		sprite.get().setPosition(v2.x + ofs.getX(), v2.y + ofs.getY());
		sprite.get().setRotation(angleMapper.getSafe(e, Angle.NONE).rotation);
		sprite.get().draw(batch);

	}

}
