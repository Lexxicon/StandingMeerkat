package com.lexxiconstudios.vestibule.core.system;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class DebugPhysicsRenderer extends BaseSystem {

	private Box2DDebugRenderer debug = new Box2DDebugRenderer();
	@Wire
	private OrthographicCamera mainCamera;
	@Wire
	private World box2dWorld;
	private boolean enable = false;
	
	public void setEnable(boolean enable){
		this.enable = enable;
	}
	
	@Override
	protected boolean checkProcessing() {
		return enable;
	}
	
	@Override
	protected void processSystem() {
		Matrix4 m4 = new Matrix4(mainCamera.combined);
		debug.render(box2dWorld, m4.scl(5f));
	}
}
