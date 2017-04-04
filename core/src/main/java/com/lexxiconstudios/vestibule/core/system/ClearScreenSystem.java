package com.lexxiconstudios.vestibule.core.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;

public class ClearScreenSystem extends BaseSystem {

	@Override
	protected void processSystem() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
	}

}
