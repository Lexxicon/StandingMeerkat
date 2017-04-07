package com.lexxiconstudios.vestibule.core.factories;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.lexxiconstudios.vestibule.core.component.LXSprite;
import com.lexxiconstudios.vestibule.core.component.Offset;
import com.lexxiconstudios.vestibule.core.component.ParticleFXComponent;
import com.lexxiconstudios.vestibule.core.component.PhysicsBody;

import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.camera.Camera;
import net.mostlyoriginal.api.component.graphics.Renderable;

public class BaseEntFac {
	private final AssetManager am;

	private final World world;

	private ComponentMapper<Pos> posMapper;
	private ComponentMapper<Angle> angleMapper;
	private ComponentMapper<LXSprite> spriteMapper;
	private ComponentMapper<PhysicsBody> physBodyMapper;
	private ComponentMapper<ParticleFXComponent> pfxMapper;
	private ComponentMapper<Offset> offsetMapper;

	private final Archetype wallType;
	private final Archetype cameraArchtype;
	private final Archetype thingArchtype;
	private final Archetype particleFXArchtype;

	public BaseEntFac(AssetManager am, World world) {
		this.world = world;
		this.am = am;

		world.inject(this);

		this.cameraArchtype = buildCamArch();
		this.thingArchtype = buildThingArch();
		this.particleFXArchtype = buildParticleFX();
		this.wallType = buildWallArch();

	}

	private Archetype buildWallArch() {
		return new ArchetypeBuilder().add(Pos.class).add(PhysicsBody.class).build(world);
	}

	private Archetype buildParticleFX() {
		return new ArchetypeBuilder().add(Offset.class).add(Renderable.class).add(Angle.class)
				.add(ParticleFXComponent.class).build(world);
	}

	private Archetype buildThingArch() {
		return new ArchetypeBuilder().add(Pos.class).add(Renderable.class).add(Angle.class).add(LXSprite.class)
				.add(PhysicsBody.class).build(world);
	}

	private Archetype buildCamArch() {
		return new ArchetypeBuilder().add(Pos.class).add(Camera.class).build(world);
	}

	public int makeCamera() {
		return world.create(cameraArchtype);
	}

	public int makeParticleEffect(int parentId, AssetDescriptor<ParticleEffect> pef, float oX, float oY, float oRot,
			float scale, boolean loop) {
		int entityID = world.create(particleFXArchtype);
		ParticleFXComponent pfxc = pfxMapper.get(entityID);
		pfxc.setParentId(parentId);
		pfxc.setLoop(loop);
		pfxc.setParticleEffect(new ParticleEffect(am.get(pef)));
		pfxc.getParticleEffect().scaleEffect(scale);
		offsetMapper.get(entityID).set(oX, oY);
		angleMapper.get(entityID).rotation = oRot;
		return entityID;
	}

	public int makeWall(float x, float y, float w, float h){
		int entityID = world.create(wallType);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		// Set our body's starting position in the world
		bodyDef.position.set(x, y);
		com.badlogic.gdx.physics.box2d.World b2d = world.getRegistered(com.badlogic.gdx.physics.box2d.World.class);
		// Create our body in the world using our body definition
		Body body = b2d.createBody(bodyDef);

		PolygonShape poly = new PolygonShape();
		poly.set(new float[] { 0, 0,
				w, 0, 
				w, h, 
				0, h });

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = poly;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		body.createFixture(fixtureDef);

		physBodyMapper.get(entityID).setB2dBody(body);
		return entityID;
	}

	public int makeThing(AssetDescriptor<Texture> texture, AssetDescriptor<ParticleEffect> pef, float x, float y) {
		int entityID = world.create(thingArchtype);
		spriteMapper.get(entityID).set(new Sprite(am.get(texture), 0, 0, 64, 32));
		posMapper.get(entityID).set(x, y);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(x, y);
		com.badlogic.gdx.physics.box2d.World w = world.getRegistered(com.badlogic.gdx.physics.box2d.World.class);
		// Create our body in the world using our body definition
		Body body = w.createBody(bodyDef);

		PolygonShape poly = new PolygonShape();
		poly.set(new float[] { 0, 0, 2f, 0, 2f, 1f, 0, 1f });

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = poly;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		body.createFixture(fixtureDef);
		body.applyTorque(10, true);

		physBodyMapper.get(entityID).setB2dBody(body);
		return entityID;
	}

}
