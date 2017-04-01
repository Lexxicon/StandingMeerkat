package com.lexxiconstudios.vestibule.core.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.EntityBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.lexxiconstudios.vestibule.core.component.PhysicsBody;
import com.lexxiconstudios.vestibule.core.component.Position;
import com.lexxiconstudios.vestibule.core.component.Sprite;

public class BaseEntFac {

	public Entity makeThing(World world, Texture texture){

		Sprite sprite = new Sprite();
		sprite.setTexture(texture);
		sprite.init();
		PhysicsBody pb = new PhysicsBody();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(10, 10);
		com.badlogic.gdx.physics.box2d.World w = world.getRegistered(com.badlogic.gdx.physics.box2d.World.class);
		// Create our body in the world using our body definition
		Body body = w.createBody(bodyDef);

		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(6f);
		
		PolygonShape poly = new PolygonShape();
		poly.set(new float[]{
				0,0,
				6,0,
				6,6,
				0,6
		});
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = poly;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		body.createFixture(fixtureDef);
		
		pb.setB2dBody(body);
		
		Position pos = new Position();
		
		return new EntityBuilder(world).with(sprite, pos, pb).build();
	}
	
}
