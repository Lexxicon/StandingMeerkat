package com.lexxiconstudios.vestibule.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.IntervalEntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.lexxiconstudios.vestibule.core.component.PhysicsBody;
import com.lexxiconstudios.vestibule.core.component.Position;

public class PhysicsSystem extends IntervalEntityProcessingSystem {

	public static final float PHYSICS_TICK_RATE = 1 / 300f;
	public static final int VELOCITY_ITERATIONS = 6;
	public static final int POSITION_ITERATIONS = 2;
	public static final float WORLD_SCALE = 10;

	private ComponentMapper<PhysicsBody> bodyMapper;
	private ComponentMapper<Position> positionMapper;
	@Wire
	private World box2dWorld;

	public PhysicsSystem() {
		super(Aspect.all(PhysicsBody.class, Position.class), PHYSICS_TICK_RATE);
	}

	@Override
	protected void begin() {
		super.begin();
		box2dWorld.step(PHYSICS_TICK_RATE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}

	@Override
	protected void end() {
		super.end();
	}

	@Override
	protected void process(Entity e) {
		PhysicsBody body = bodyMapper.get(e);
		Position p = positionMapper.get(e);
		p.setRotation(MathUtils.radiansToDegrees * body.getB2dBody().getAngle());
		p.setX(body.getB2dBody().getPosition().x * WORLD_SCALE);
		p.setY(body.getB2dBody().getPosition().y * WORLD_SCALE);
	}

}
