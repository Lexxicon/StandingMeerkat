package com.lexxiconstudios.vestibule.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.IntervalEntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.lexxiconstudios.vestibule.core.component.PhysicsBody;

import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;

public class PhysicsSystem extends IntervalEntityProcessingSystem {

	public static final float PHYSICS_TICK_RATE = 1 / 300f;
	public static final int VELOCITY_ITERATIONS = 6;
	public static final int POSITION_ITERATIONS = 2;
	public static final float BOX_TO_WORLD = 32;
	public static final float WORLD_TO_BOX = 1f / BOX_TO_WORLD;

	private ComponentMapper<PhysicsBody> bodyMapper;
	private ComponentMapper<Pos> positionMapper;
	private ComponentMapper<Angle> angleMapper;;
	@Wire
	private World box2dWorld;

	private float accDelta;
	private long stepCount;

	public PhysicsSystem() {
		super(Aspect.all(PhysicsBody.class, Pos.class), PHYSICS_TICK_RATE);
	}

	@Override
	protected void begin() {
		super.begin();
		float actualDelta = getIntervalDelta() + accDelta;
		while ((actualDelta -= PHYSICS_TICK_RATE) > PHYSICS_TICK_RATE) {
			box2dWorld.step(PHYSICS_TICK_RATE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			stepCount++;
		}
		accDelta = actualDelta;
	}

	public long getStepCount() {
		return stepCount;
	}

	@Override
	protected void end() {
		super.end();
	}

	@Override
	protected void process(Entity e) {
		PhysicsBody body = bodyMapper.get(e);
		Pos p = positionMapper.get(e);
		Angle a = angleMapper.getSafe(e, null);
		if(a != null){
			a.rotation = (MathUtils.radiansToDegrees * body.getB2dBody().getAngle());
		}
		p.set(body.getB2dBody().getPosition().cpy().scl(BOX_TO_WORLD));

	}

}
