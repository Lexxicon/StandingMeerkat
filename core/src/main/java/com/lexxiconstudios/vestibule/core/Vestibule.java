package com.lexxiconstudios.vestibule.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lexxiconstudios.vestibule.core.exceptions.MultiException;
import com.lexxiconstudios.vestibule.core.factories.BaseEntFac;
import com.lexxiconstudios.vestibule.core.system.DebugPhysicsRenderer;
import com.lexxiconstudios.vestibule.core.system.ParticleRenderSystem;
import com.lexxiconstudios.vestibule.core.system.PhysicsSystem;
import com.lexxiconstudios.vestibule.core.system.RenderingSystem;

import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.camera.EntityCameraSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.api.system.render.ClearScreenSystem;

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

	Viewport viewport;

	@Override
	public void create() {
		Box2D.init();
		AssetDescriptor<Texture> tex = new AssetDescriptor<>("64_32.png", Texture.class);
		AssetDescriptor<ParticleEffect> pef = new AssetDescriptor<>("effects/defaultFire.p", ParticleEffect.class);
		
		assetManager = new AssetManager();
		disposableResources.add(assetManager);
		assetManager.load(pef);
		assetManager.load(tex);
		assetManager.finishLoading();
		
		batch = new SpriteBatch();
		disposableResources.add(batch);
		com.badlogic.gdx.physics.box2d.World b2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0),
				true);
		disposableResources.add(b2dWorld);

		WorldConfiguration wcfg = new WorldConfigurationBuilder().with(buildCameraSystems()).with(new PhysicsSystem())
				.with(buildRenderingSystems()).build();
		wcfg.register(b2dWorld);
		wcfg.register(batch);
		wcfg.register(assetManager);

		world = new World(wcfg);
		BaseEntFac entFac = new BaseEntFac(assetManager, world);
		entFac.makeCamera();
		int id = entFac.makeThing(tex, pef, -1, 0);
		entFac.makeThing(tex, pef, -1, 2.1f);
		entFac.makeThing(tex, pef, -1, -2.1f);
		entFac.makeParticleEffect(id, pef, 32, 32, 180, 1f, true);
		entFac.makeParticleEffect(id, pef, -1, 0, -1, .5f, true);
		viewport = new FitViewport(200, 200, world.getSystem(CameraSystem.class).camera);
	}

	private BaseSystem[] buildCameraSystems() {
		return new BaseSystem[] { new CameraSystem(1000, 1000), new EntityCameraSystem() };
	}

	private BaseSystem[] buildRenderingSystems() {
		RenderBatchingSystem renderBatcher = new RenderBatchingSystem();
		return new BaseSystem[] { new ClearScreenSystem(), renderBatcher, new RenderingSystem(renderBatcher),
				new ParticleRenderSystem(renderBatcher), new DebugPhysicsRenderer() };
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void render() {
		float delta = MathUtils.clamp(Gdx.graphics.getDeltaTime(), 0, 1 / 15f);
		elapsed += delta;
		world.delta = delta;
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
