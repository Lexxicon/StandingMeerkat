package com.lexxiconstudios.vestibule.core.factories;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.lexxiconstudios.vestibule.core.component.GravitySource;
import com.lexxiconstudios.vestibule.core.component.LXSprite;
import com.lexxiconstudios.vestibule.core.component.Offset;
import com.lexxiconstudios.vestibule.core.component.ParticleFXComponent;
import com.lexxiconstudios.vestibule.core.component.PhysicsBody;
import com.lexxiconstudios.vestibule.core.component.PhysicsForce;
import com.lexxiconstudios.vestibule.core.component.markers.MouseComponent;

import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.camera.Camera;
import net.mostlyoriginal.api.component.graphics.Renderable;

public class BaseEntFac {

	private ComponentMapper<Pos> posMapper;
	private ComponentMapper<Angle> angleMapper;
	private ComponentMapper<LXSprite> spriteMapper;
	private ComponentMapper<PhysicsBody> physBodyMapper;
	private ComponentMapper<ParticleFXComponent> pfxMapper;
	private ComponentMapper<Offset> offsetMapper;
	private ComponentMapper<GravitySource> gravitySourceMapper;

	@Wire
	private com.badlogic.gdx.physics.box2d.World b2dWorld;
	@Wire
	private AssetManager am;
	private World world;

	private final Archetype wallType;
	private final Archetype cameraArchtype;
	private final Archetype thingArchtype;
	private final Archetype particleFXArchtype;
	private final Archetype mouseCursor;
	private final Archetype planetArchtype;

	private Body global;

	public BaseEntFac(AssetManager am, World world) {
		world.inject(this);

		this.world = world;
		this.cameraArchtype = buildCamArch();
		this.thingArchtype = buildThingArch();
		this.particleFXArchtype = buildParticleFX();
		this.wallType = buildWallArch();
		this.global = b2dWorld.createBody(new BodyDef());
		this.mouseCursor = buildMouseArch();
		this.planetArchtype = buildPlanetArch();
	}

	private Archetype buildMouseArch() {
		return new ArchetypeBuilder()
				.add(Pos.class)
				.add(LXSprite.class)
				.add(Renderable.class)
				.add(MouseComponent.class)
				.add(Offset.class)
				.build(world);
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
				.add(PhysicsForce.class).add(PhysicsBody.class).build(world);
	}

	private Archetype buildCamArch() {
		return new ArchetypeBuilder().add(Pos.class).add(Camera.class).build(world);
	}
	
	private Archetype buildPlanetArch(){
		return new ArchetypeBuilder().add(Pos.class).add(PhysicsBody.class).add(GravitySource.class).build(world);
	}

	public int makeCamera() {
		return world.create(cameraArchtype);
	}
	
	public int makePlanet(float x, float y, float r, float density){
		int eID = world.create(planetArchtype);
		posMapper.get(eID).set(x, y);
		
		BodyDef bodyDef = new BodyDef();
		// Set our body's starting position in the world
		bodyDef.position.set(x, y);
		com.badlogic.gdx.physics.box2d.World b2d = world.getRegistered(com.badlogic.gdx.physics.box2d.World.class);
		// Create our body in the world using our body definition
		Body body = b2d.createBody(bodyDef);
		CircleShape circle = new CircleShape();
		circle.setRadius(r);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.friction = 0.1f;

		// Create our fixture and attach it to the body
		body.createFixture(fixtureDef);

		physBodyMapper.get(eID).setB2dBody(body);
		
		gravitySourceMapper.get(eID).originEntity = eID;
		gravitySourceMapper.get(eID).effectiveGravity = r * density;
		gravitySourceMapper.get(eID).sphereOfInfluence = r * density * 10;
		return eID;
	}

	public int makeMouseCursor(TextureAtlas als) {
		int eID = world.create(mouseCursor);
		LXSprite s = spriteMapper.get(eID);
		s.set(als.createSprite("sparkPart"));
		s.get().setSize(s.get().getWidth()/32f, s.get().getHeight()/32f);
		offsetMapper.get(eID).xy.sub(s.get().getWidth()/2f, s.get().getHeight()/2f);
		return eID;
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

	public int makeWall(float x, float y, float w, float h) {
		int entityID = world.create(wallType);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		// Set our body's starting position in the world
		bodyDef.position.set(x, y);
		com.badlogic.gdx.physics.box2d.World b2d = world.getRegistered(com.badlogic.gdx.physics.box2d.World.class);
		// Create our body in the world using our body definition
		Body body = b2d.createBody(bodyDef);

		PolygonShape poly = new PolygonShape();
		poly.set(new float[] { 0, 0, w, 0, w, h, 0, h });

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = poly;
		fixtureDef.friction = 0.1f;

		// Create our fixture and attach it to the body
		body.createFixture(fixtureDef);

		physBodyMapper.get(entityID).setB2dBody(body);
		return entityID;
	}

	public int makeThing(AssetDescriptor<Texture> texture, float x, float y) {
		int entityID = world.create(thingArchtype);
		spriteMapper.get(entityID).set(new Sprite(am.get(texture), 0, 0, 64, 32));
		spriteMapper.get(entityID).get().setSize(2, 1);
		spriteMapper.get(entityID).get().setOrigin(0, 0);
		posMapper.get(entityID).set(x, y);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(x, y);

		// Create our body in the world using our body definition
		Body body = b2dWorld.createBody(bodyDef);

		PolygonShape poly = new PolygonShape();
		poly.set(new float[] { 
				0,  1f,
				2f, 1f,
				2f, 0.875f,
				1.5625f, 0.4375f,
				0.53125f,0.4375f, 
				0, 0.78125f
				});

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = poly;
		fixtureDef.density = 1f;
		fixtureDef.friction = .1f;
		fixtureDef.restitution = 0.3f; // Make it bounce a little bit
		
		// Create our fixture and attach it to the body
		body.createFixture(fixtureDef);

		physBodyMapper.get(entityID).setB2dBody(body);
//		FrictionJointDef fricJointDef = new FrictionJointDef();
//		fricJointDef.initialize(body, global, body.getWorldCenter().cpy().sub(0, .01f));
//		fricJointDef.maxForce = 10;
//		fricJointDef.maxTorque = 1;
//		b2dWorld.createJoint(fricJointDef);

		return entityID;
	}

}
