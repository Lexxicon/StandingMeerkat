package com.lexxicon.vestibule.system;

import java.util.concurrent.TimeUnit;

import org.jbox2d.dynamics.World;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.artemis.systems.IntervalEntitySystem;

public class PhysicsSystem extends IntervalEntitySystem {

	static final long physUpdateRateMS = TimeUnit.SECONDS.toMillis(1) / 60;
	static final float physUpdateRateSec = physUpdateRateMS / 1000f;

	final int velocityIterations = 6;
	final int positionIterations = 2;
	
	@Wire
	private World physicsWorld;

	long count;
	
	public PhysicsSystem() {
		super(Aspect.all(), physUpdateRateSec);
	}

	public long getCount() {
		return count;
	}
	
	@Override
	protected void processSystem() {
		physicsWorld.step(physUpdateRateSec, velocityIterations, positionIterations);
		count++;
	}

}
