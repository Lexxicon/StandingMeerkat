package com.lexxiconstudios.vestibule.core.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

public class ParticleEffComponent extends Component {

	ParticleEffect particleEffect;
	float[] lowMin, lowMax;
	float[] highMin, highMax;
	Position position;

	public void applyRotation(float rotation) {
		if (position != null) {
			rotation += position.getRotation();
		}
		if (particleEffect != null) {
			for (int i = 0; i < particleEffect.getEmitters().size; i++) {
				ParticleEmitter em = (ParticleEmitter) ((Object[])particleEffect.getEmitters().items)[i];
				em.getAngle().setHigh(highMin[i] + rotation, highMax[i] + rotation);
				em.getAngle().setLow(lowMin[i] + rotation, lowMax[i] + rotation);
			}
		}
	}

	public Position getOffset() {
		return position;
	}

	public void setOffset(Position position) {
		this.position = position;
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
