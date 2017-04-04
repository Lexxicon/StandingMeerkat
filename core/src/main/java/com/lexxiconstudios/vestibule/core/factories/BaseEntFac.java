package com.lexxiconstudios.vestibule.core.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.EntityBuilder;
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
import com.lexxiconstudios.vestibule.core.component.Position;

public class BaseEntFac {
	AssetManager am;
	
	public BaseEntFac(AssetManager am) {
		this.am = am;
	}
	public Entity makeThing(World world, AssetDescriptor<Texture> texture, AssetDescriptor<ParticleEffect> pef, float x, float y) {

		LXSprite sprite = new LXSprite();
		sprite.set(new Sprite(am.get(texture), 0, 0, 64, 32));
		PhysicsBody pb = new PhysicsBody();
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

		Position pos = new Position();
		
		ParticleEffect peff = am.get(pef);
		ParticleEffComponent pefc = new ParticleEffComponent();
		pefc.setOffset(new Position());
		pefc.setParticleEffect(peff);
		
		return new EntityBuilder(world).with(sprite, pos, pefc, pb).build();
	}

}
