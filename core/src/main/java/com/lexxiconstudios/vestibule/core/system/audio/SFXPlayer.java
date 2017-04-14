package com.lexxiconstudios.vestibule.core.system.audio;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.lexxiconstudios.vestibule.core.component.SoundEffect;

public class SFXPlayer extends IteratingSystem {

	ComponentMapper<SoundEffect> sfxMapper;

	public SFXPlayer() {
		super(Aspect.all(SoundEffect.class));
	}

	@Override
	protected void process(int e) {
		SoundEffect fx = sfxMapper.get(e);
		if (fx.shouldPlay) {
			fx.id = fx.sfx.play(sfxMapper.get(e).volume);
			fx.shouldPlay = false;
		}
		
		if (fx.shouldStop) {
			fx.sfx.stop(fx.id);
			;
			fx.shouldStop = false;
		}
	}

}
