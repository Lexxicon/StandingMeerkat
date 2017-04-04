package com.lexxiconstudios.vestibule.core.system;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class DebugPhysicsRenderer extends BaseSystem {

	private Box2DDebugRenderer debug = new Box2DDebugRenderer();
	@Wire
	private OrthographicCamera mainCamera;
	@Wire
	private World box2dWorld;
	private boolean enable = Boolean.parseBoolean(System.getProperty("DebugPhysicsRenderer", "true"));

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	protected boolean checkProcessing() {
		return enable;
	}

	@Override
	protected void processSystem() {
		debug.render(box2dWorld,
				mainCamera.combined.cpy().scale(
						PhysicsSystem.BOX_TO_WORLD, 
						PhysicsSystem.BOX_TO_WORLD, 
						1));
	}
}
