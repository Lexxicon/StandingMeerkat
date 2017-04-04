package com.lexxiconstudios.vestibule.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lexxiconstudios.vestibule.core.exceptions.MultiException;
import com.lexxiconstudios.vestibule.core.factories.BaseEntFac;
import com.lexxiconstudios.vestibule.core.system.ClearScreenSystem;
import com.lexxiconstudios.vestibule.core.system.DebugPhysicsRenderer;
import com.lexxiconstudios.vestibule.core.system.ParticleRenderSystem;
import com.lexxiconstudios.vestibule.core.system.PhysicsSystem;
import com.lexxiconstudios.vestibule.core.system.RenderingSystem;
import com.lexxiconstudios.vestibule.core.util.DeltaProvider;

public class Vestibule implements ApplicationListener {
	{
		// Screw launch args
		System.setProperty("DebugPhysicsRenderer", "true");
	}
	Collection<Disposable> disposableResources = new ArrayList<>();
	AssetManager assetManager;

	Texture texture;
	SpriteBatch batch;
	float elapsed;

	World world;
	Viewport mainViewPort;
	Camera mainCamera;

	@Override
	public void create() {
		Box2D.init();
		assetManager = new AssetManager();
		disposableResources.add(assetManager);
		AssetDescriptor<Texture> tex = new AssetDescriptor<>("64_32.png", Texture.class);
		AssetDescriptor<ParticleEffect> pef = new AssetDescriptor<>("effects/defaultFire.p", ParticleEffect.class);
		assetManager.load(pef);
		assetManager.load(tex);
		assetManager.finishLoading();
		texture = assetManager.get(tex);
		batch = new SpriteBatch();
		disposableResources.add(batch);

		mainCamera = new OrthographicCamera();
		mainViewPort = new FitViewport(200, 200, mainCamera);
		mainViewPort.apply();

		WorldConfiguration wcfg = new WorldConfigurationBuilder().with(
				new ClearScreenSystem(), new PhysicsSystem(),
				new RenderingSystem(), new ParticleRenderSystem(), new DebugPhysicsRenderer()).build();
		com.badlogic.gdx.physics.box2d.World b2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0),
				true);
		wcfg.register(b2dWorld);
		disposableResources.add(b2dWorld);
		wcfg.register(batch);
		wcfg.register(mainViewPort.getCamera());
		wcfg.register(assetManager);
		wcfg.register(new DeltaProvider(() -> Gdx.graphics.getDeltaTime()));

		world = new World(wcfg);

		new BaseEntFac(assetManager).makeThing(world, tex, pef, -1, 0);
		new BaseEntFac(assetManager).makeThing(world, tex, pef, -1, 2.1f);
		new BaseEntFac(assetManager).makeThing(world, tex, pef, -1, -2.1f);
	}

	@Override
	public void resize(int width, int height) {
		mainViewPort.update(width, height);
	}

	@Override
	public void render() {
		elapsed += Gdx.graphics.getDeltaTime();
		world.delta = Gdx.graphics.getDeltaTime();
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
		List<Throwable> exceptions = new ArrayList<>();
		disposableResources.removeIf(o -> o == null);
		for (Disposable d : disposableResources) {
			try {
				d.dispose();
			} catch (Throwable t) {
				exceptions.add(t);
				Gdx.app.getApplicationLogger().error("SHUTDOWN", "Exception while disposing " + d, t);
			}
		}
		try {
			world.dispose();
		} catch (Throwable t) {
			exceptions.add(t);
			Gdx.app.getApplicationLogger().error("SHUTDOWN", "Exception while disposing Artemis", t);
		}
		if (exceptions.size() > 0) {
			throw new MultiException(exceptions);
		}
	}
}
