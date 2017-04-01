package com.lexxiconstudios.vestibule.core;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.EntityBuilder;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lexxiconstudios.vestibule.core.component.PhysicsBody;
import com.lexxiconstudios.vestibule.core.component.Position;
import com.lexxiconstudios.vestibule.core.component.Sprite;
import com.lexxiconstudios.vestibule.core.system.PhysicsSystem;
import com.lexxiconstudios.vestibule.core.system.RenderingSystem;

public class Vestibule implements ApplicationListener {
	Texture texture;
	SpriteBatch batch;
	float elapsed;
	ParticleEffect peff;

	World world;
	Position pos = new Position();
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
						new RenderingSystem())
				.build();
		
		wcfg.register(new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -10), true));
		wcfg.register(batch);
		wcfg.register(mainCam);
		
		world = new World(wcfg);
		Sprite sprite = new Sprite();
		sprite.setTexture(texture);
		sprite.init();
		PhysicsBody pb = new PhysicsBody();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(10, 40);
		com.badlogic.gdx.physics.box2d.World w = world.getRegistered(com.badlogic.gdx.physics.box2d.World.class);
		// Create our body in the world using our body definition
		Body body = w.createBody(bodyDef);

		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(6f);
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		body.createFixture(fixtureDef);
		
		pb.setB2dBody(body);
		new EntityBuilder(world).with(sprite, pos, pb).build();
	}

	@Override
	public void resize(int width, int height) {
		mainViewPort.update(width, height, true);
	}

	@Override
	public void render() {
		elapsed += Gdx.graphics.getDeltaTime();
		world.delta = Gdx.graphics.getDeltaTime();
		batch.begin();
		mainViewPort.apply();
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		world.process();
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
		world.getRegistered(com.badlogic.gdx.physics.box2d.World.class).dispose();
		world.dispose();
	}
}
