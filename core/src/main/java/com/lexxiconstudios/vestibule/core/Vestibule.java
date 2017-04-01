package com.lexxiconstudios.vestibule.core;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lexxiconstudios.vestibule.core.factories.BaseEntFac;
import com.lexxiconstudios.vestibule.core.system.DebugPhysicsRenderer;
import com.lexxiconstudios.vestibule.core.system.PhysicsSystem;
import com.lexxiconstudios.vestibule.core.system.RenderingSystem;

public class Vestibule implements ApplicationListener {
	Texture texture;
	SpriteBatch batch;
	float elapsed;
	ParticleEffect peff;

	World world;
	Viewport mainViewPort;
	
	@Override
	public void create() {
		Box2D.init();

		texture = new Texture(Gdx.files.internal("libgdx-logo.png"));
		batch = new SpriteBatch();
		peff = new ParticleEffect();
		peff.load(Gdx.files.internal("effects/defaultFire.p"), Gdx.files.internal("effects"));
		peff.setPosition(50, 50);

		OrthographicCamera mainCam = new OrthographicCamera(200, 200);
		mainViewPort = new FitViewport(100, 100, mainCam);
		
		WorldConfiguration wcfg = new WorldConfigurationBuilder()
				.with(
						new PhysicsSystem(),
						new RenderingSystem(),
						new DebugPhysicsRenderer())
				.build();
		
		wcfg.register(new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -10), true));
		wcfg.register(batch);
		wcfg.register(mainCam);
		
		world = new World(wcfg);
		world.getSystem(DebugPhysicsRenderer.class).setEnable(true);
		
		new BaseEntFac().makeThing(world, texture);
	}

	@Override
	public void resize(int width, int height) {
		mainViewPort.update(width, height, true);
	}

	@Override
	public void render() {
		elapsed += Gdx.graphics.getDeltaTime();
		world.delta = Gdx.graphics.getDeltaTime();
		mainViewPort.apply();
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		world.process();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		world.getRegistered(com.badlogic.gdx.physics.box2d.World.class).dispose();
		world.dispose();
	}
}
