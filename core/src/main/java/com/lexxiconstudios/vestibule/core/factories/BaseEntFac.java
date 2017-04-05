package com.lexxiconstudios.vestibule.core.factories;

import com.artemis.Entity;
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
import com.lexxiconstudios.vestibule.core.component.ParticleEffComponent;
import com.lexxiconstudios.vestibule.core.component.PhysicsBody;

import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.camera.Camera;
import net.mostlyoriginal.api.component.graphics.Renderable;

public class BaseEntFac {
	AssetManager am;
	
	public BaseEntFac(AssetManager am) {
		this.am = am;
	}
	
	public int makeCamera(World world){
		int id = world.create();
		world.edit(id).create(Pos.class);
		world.edit(id).create(Camera.class);
		return id;
	}
	
	public Entity makeThing(World world, AssetDescriptor<Texture> texture, AssetDescriptor<ParticleEffect> pef, float x, float y) {
		int entityID = world.create();
		world.edit(entityID).create(Renderable.class);
		world.edit(entityID).create(Pos.class);
		world.edit(entityID).create(Angle.class);
		
		LXSprite sprite = world.edit(entityID).create(LXSprite.class);
		sprite.set(new Sprite(am.get(texture), 0, 0, 64, 32));
		
		PhysicsBody pb = world.edit(entityID).create(PhysicsBody.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(x, y);
		com.badlogic.gdx.physics.box2d.World w = world.getRegistered(com.badlogic.gdx.physics.box2d.World.class);
		// Create our body in the world using our body definition
		Body body = w.createBody(bodyDef);

		PolygonShape poly = new PolygonShape();
		poly.set(new float[] { 
				0, 0, 
				2f, 0, 
				2f, 1f,
				0, 1f });
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = poly;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		body.createFixture(fixtureDef);
		body.applyTorque(10, true);
		pb.setB2dBody(body);
		
//		ParticleEffect peff = am.get(pef);
//		ParticleEffComponent pefc = world.edit(entityID).create(ParticleEffComponent.class);
//		pefc.setOffset(new Pos());
//		pefc.setParticleEffect(peff);
		return null;
	}

}
