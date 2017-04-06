package com.lexxiconstudios.vestibule.core.component;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

public class ParticleFXComponent extends Component {

	@EntityId int parentId = -1;
	ParticleEffect particleEffect;
	float[] lowMin, lowMax;
	float[] highMin, highMax;
	boolean loop = true;
	
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	public boolean isLoop() {
		return loop;
	}

	public void setRotation(float rot) {
		if (particleEffect != null) {
			for (int i = 0; i < particleEffect.getEmitters().size; i++) {
				ParticleEmitter em = (ParticleEmitter) ((Object[])particleEffect.getEmitters().items)[i];
				em.getAngle().setHigh(highMin[i] + rot, highMax[i] + rot);
				em.getAngle().setLow(lowMin[i] + rot, lowMax[i] + rot);
			}
		}
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	public int getParentId() {
		return parentId;
	}

	public ParticleEffect getParticleEffect() {
		return particleEffect;
	}

	public void setParticleEffect(ParticleEffect particleEffect) {
		this.particleEffect = new ParticleEffect(particleEffect);
		int emitters = particleEffect.getEmitters().size;
		lowMin = lowMax = highMin = highMax = new float[emitters];
		for (int i = 0; i < emitters; i++) {
			ParticleEmitter em = (ParticleEmitter) ((Object[])particleEffect.getEmitters().items)[i];
			lowMin[i] = em.getAngle().getLowMin();
			highMin[i] = em.getAngle().getHighMin();
			lowMax[i] = em.getAngle().getLowMax();
			highMax[i] = em.getAngle().getHighMax();
		}
	}
}
