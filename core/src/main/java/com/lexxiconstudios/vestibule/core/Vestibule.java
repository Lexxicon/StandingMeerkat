package com.lexxiconstudios.vestibule.core;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lexxiconstudios.vestibule.core.system.PhysicsSystem;
import com.lexxiconstudios.vestibule.core.system.RenderingSystem;

public class Vestibule implements ApplicationListener {
	Texture texture;
	SpriteBatch batch;
	float elapsed;
	ParticleEffect peff;

	World world;

	@Override
	public void create() {
		Box2D.init();

		texture = new Texture(Gdx.files.internal("libgdx-logo.png"));
		batch = new SpriteBatch();
		peff = new ParticleEffect();
		peff.load(Gdx.files.internal("effects/defaultFire.p"), Gdx.files.internal("effects"));
		peff.setPosition(50, 50);
		
		Viewport vp = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Camera mainCam = vp.getCamera();
		System.out.println(mainCam);
		
		WorldConfiguration wcfg = new WorldConfigurationBuilder()
				.with(
						new PhysicsSystem(), 
						new RenderingSystem())
				.build();
		
		wcfg.register(new com.badlogic.gdx.physics.box2d.World(new Vector2(), true));
		wcfg.register(batch);
		wcfg.register(mainCam);
		
		world = new World(wcfg);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && peff.isComplete()) {
			System.out.println("click");
			peff.start();
		}
		
		elapsed += Gdx.graphics.getDeltaTime();
		world.delta = Gdx.graphics.getDeltaTime();

		batch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		world.process();
		peff.setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		peff.draw(batch);
		peff.update(Gdx.graphics.getDeltaTime());
		batch.end();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
