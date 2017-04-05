package com.lexxiconstudios.vestibule.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lexxiconstudios.vestibule.core.component.ParticleEffComponent;

import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Renderable;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;

public class ParticleRenderSystem extends DeferredEntityProcessingSystem  {

	ComponentMapper<ParticleEffComponent> effectMapper;
	ComponentMapper<Pos> positionMapper;
	ComponentMapper<Angle> angleMapper;

	@Wire
	SpriteBatch spriteBatch;

	public ParticleRenderSystem(RenderBatchingSystem renderBatcher) {
		super(Aspect.all(ParticleEffComponent.class, Pos.class, Renderable.class), renderBatcher);
	}

	@Override
	protected void begin() {
		super.begin();
		spriteBatch.begin();
	}

	@Override
	protected void end() {
		super.end();
		spriteBatch.end();
	}

	@Override
	protected void process(int e) {
		ParticleEffComponent effComp = effectMapper.get(e);
		ParticleEffect effect = effComp.getParticleEffect();
		Angle a = angleMapper.getSafe(e, Angle.NONE);
		effComp.applyRotation(a.rotation);
		if (effect.isComplete()) {
			effect.start();
		}
		Pos p = positionMapper.get(e);
		effect.update(world.delta);
		effect.setPosition(p.getX() + effComp.getOffset().getX(), p.getY() + effComp.getOffset().getY());
		effect.draw(spriteBatch);
	}

}
