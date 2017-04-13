package com.lexxiconstudios.vestibule.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lexxiconstudios.vestibule.core.component.PlayerControlled;
import com.lexxiconstudios.vestibule.core.exceptions.MultiException;
import com.lexxiconstudios.vestibule.core.factories.BaseEntFac;
import com.lexxiconstudios.vestibule.core.system.PlayerMovementSystem;
import com.lexxiconstudios.vestibule.core.system.camera.LXCameraSystem;
import com.lexxiconstudios.vestibule.core.system.physics.ForceApplierSystem;
import com.lexxiconstudios.vestibule.core.system.physics.PhysicsSystem;
import com.lexxiconstudios.vestibule.core.system.physics.PositionSynchSystem;
import com.lexxiconstudios.vestibule.core.system.rendering.DebugPhysicsRenderer;
import com.lexxiconstudios.vestibule.core.system.rendering.ParticleRenderSystem;
import com.lexxiconstudios.vestibule.core.system.rendering.RenderingSystem;

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
		AssetDescriptor<Texture> tex = new AssetDescriptor<>("64_32.png",
				Texture.class);
		AssetDescriptor<ParticleEffect> pef = new AssetDescriptor<>(
				"effects/defaultFire.p", ParticleEffect.class);

		assetManager = new AssetManager();
		disposableResources.add(assetManager);
		assetManager.load(pef);
		assetManager.load(tex);
		assetManager.finishLoading();

		batch = new SpriteBatch();
		disposableResources.add(batch);
		com.badlogic.gdx.physics.box2d.World b2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.80f),
				true);
		disposableResources.add(b2dWorld);

		WorldConfiguration wcfg = new WorldConfigurationBuilder()
				.with(new EntityLinkManager())
				.with(buildCameraSystems())
				.with(new PlayerMovementSystem())
				.with(buildPhysicsSystems())
				.with(buildRenderingSystems()).build();

		wcfg.register(b2dWorld);
		wcfg.register(batch);
		wcfg.register(assetManager);

		world = new World(wcfg);
		BaseEntFac entFac = new BaseEntFac(assetManager, world);
		entFac.makeCamera();
		int id = entFac.makeThing(tex, -1, 0);
		entFac.makeThing(tex, -2, 0);
		entFac.makeThing(tex, 0, 0);
		world.edit(id).add(new PlayerControlled());
//		entFac.makeParticleEffect(id, pef, 1, 1, 0, 1, true);

		entFac.makeWall(-3f, -3f, 6, .1f);
		entFac.makeWall(3f, -3f, .1f, 6);
		entFac.makeWall(-3f, -3f, .1f, 6);
		entFac.makeWall(-3f, 3f, 6, .1f);
	}

	private BaseSystem[] buildCameraSystems() {
		return new BaseSystem[] { new LXCameraSystem(1),
				new EntityCameraSystem() };
	}

	private BaseSystem[] buildPhysicsSystems() {
		PhysicsSystem physicsSystem = new PhysicsSystem();
		return new BaseSystem[] {
				physicsSystem,
				new PositionSynchSystem(physicsSystem),
				new ForceApplierSystem(physicsSystem) };
	}

	private BaseSystem[] buildRenderingSystems() {
		RenderBatchingSystem renderBatcher = new RenderBatchingSystem();
		return new BaseSystem[] {
				new ClearScreenSystem(),
				renderBatcher,
				new RenderingSystem(renderBatcher),
				new ParticleRenderSystem(renderBatcher),
				new DebugPhysicsRenderer() };
	}

	@Override
	public void resize(int width, int height) {
		world.getSystem(LXCameraSystem.class).resize(width, height);
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
				Gdx.app.getApplicationLogger().error("SHUTDOWN",
						"Exception while disposing " + d, t);
			}
		}
		try {
			world.dispose();
		} catch (Throwable t) {
			exceptions.add(t);
			Gdx.app.getApplicationLogger().error("SHUTDOWN",
					"Exception while disposing Artemis", t);
		}
		if (exceptions.size() > 0) {
			throw new MultiException(exceptions);
		}
	}
}
