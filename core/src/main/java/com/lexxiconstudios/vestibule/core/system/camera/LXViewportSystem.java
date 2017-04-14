package com.lexxiconstudios.vestibule.core.system.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.mostlyoriginal.api.system.camera.CameraSystem;

public class LXViewportSystem extends CameraSystem {

	private final Color backgroundColor;

	public Viewport viewport;
	public Viewport guiViewport;

	public ShapeRenderer r;

	public LXViewportSystem(float w, float h) {
		super(w, h);
		r = new ShapeRenderer();
		backgroundColor = new Color(.1f, .1f, .1f, 1);
	}

	protected void setupViewport(float width, float height) {
		super.setupViewport(width, height);
		viewport = cfgViewport(new ExtendViewport(width, height, camera));
		guiViewport = cfgViewport(new ExtendViewport(width, height, guiCamera));
	}

	private Viewport cfgViewport(Viewport vp) {
		vp.setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		vp.apply();
		return vp;
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
		guiViewport.update(width, height, true);
	}

	@Override
	protected void processSystem() {
		r.begin(ShapeType.Filled);
		r.setProjectionMatrix(camera.combined);
		r.setColor(backgroundColor);
		r.rect(camera.position.x - camera.viewportWidth / 2f, camera.position.y - camera.viewportHeight / 2f,
				viewport.getWorldWidth(), viewport.getWorldHeight());
		r.end();
	}
}
