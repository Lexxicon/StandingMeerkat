package com.lexxiconstudios.vestibule.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lexxiconstudios.vestibule.core.component.ParticleEffComponent;
import com.lexxiconstudios.vestibule.core.component.Position;
import com.lexxiconstudios.vestibule.core.util.DeltaProvider;

public class ParticleRenderSystem extends EntityProcessingSystem {

	ComponentMapper<ParticleEffComponent> effectMapper;
	ComponentMapper<Position> positionMapper;

	private final Position emptyPosition = new Position();

	@Wire
	DeltaProvider deltaP;
	@Wire
	SpriteBatch spriteBatch;

	public ParticleRenderSystem() {
		super(Aspect.all(ParticleEffComponent.class));
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
	protected void process(Entity e) {
		ParticleEffComponent effComp = effectMapper.get(e);
		ParticleEffect effect = effComp.getParticleEffect();
		Position p = positionMapper.getSafe(e, emptyPosition);	
		effComp.applyRotation(p.getRotation());
		if(effect.isComplete()){
			effect.start();
		}
		effect.update(deltaP.getDelta());
		effect.setPosition(p.getX() + effComp.getOffset().getX(), p.getY() + effComp.getOffset().getY());
		effect.draw(spriteBatch);
	}

}
