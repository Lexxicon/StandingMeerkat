package com.lexxiconstudios.vestibule.core.system.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.lexxiconstudios.vestibule.core.Constants;
import com.lexxiconstudios.vestibule.core.component.Offset;
import com.lexxiconstudios.vestibule.core.component.ParticleFXComponent;
import com.lexxiconstudios.vestibule.core.system.camera.LXViewportSystem;

import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Renderable;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;

public class ParticleRenderSystem extends DeferredEntityProcessingSystem {

	ComponentMapper<ParticleFXComponent> effectMapper;
	ComponentMapper<Pos> positionMapper;
	ComponentMapper<Offset> offsetMapper;
	ComponentMapper<Angle> angleMapper;

	@Wire
	SpriteBatch spriteBatch;
	@Wire
	LXViewportSystem cameraSystem;
	
	public ParticleRenderSystem(RenderBatchingSystem renderBatcher) {
		super(Aspect.all(ParticleFXComponent.class, Renderable.class), renderBatcher);
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
		ParticleFXComponent effComp = effectMapper.get(e);
		ParticleEffect effect = effComp.getParticleEffect();
		if (effect.isComplete()) {
			if (effComp.isLoop()) {
				effect.reset();
				effect.start();
			} else {
				return;
			}
		}

		Angle parentAngle = angleMapper.getSafe(effComp.getParentId(), Angle.NONE);
		Angle angleOffset = angleMapper.getSafe(e, Angle.NONE);
		
		effComp.setRotation(angleOffset.rotation + parentAngle.rotation);
	
		Pos parentPos = positionMapper.getSafe(effComp.getParentId(), Constants.EMPTY_POS);
		Offset offsetPos = offsetMapper.getSafe(e, Constants.EMPTY_OFFSET);
		Vector2 rotatedOffset = offsetPos.xy.cpy().rotate(parentAngle.rotation).add(parentPos.xy);
		
		effect.setPosition(rotatedOffset.x, rotatedOffset.y);
		effect.update(world.delta);
		effect.draw(spriteBatch);
	}

}
