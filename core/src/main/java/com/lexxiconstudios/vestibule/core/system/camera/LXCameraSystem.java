package com.lexxiconstudios.vestibule.core.system.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.mostlyoriginal.api.system.camera.CameraSystem;

public class LXCameraSystem extends CameraSystem {
	public Viewport viewport;
	public Viewport guiViewport;

	public LXCameraSystem(float zoom) {
		super(1, 1);
	}

	protected void setupViewport(float width, float height) {
		super.setupViewport(width, height);
		viewport = new FitViewport(10, 10, camera);
		viewport.setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		viewport.apply();
		
		guiViewport = new FitViewport(10, 10, guiCamera);
		viewport.setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		viewport.apply();
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
		guiViewport.update(width, height, true);
	}
}
