package com.lexxiconstudios.vestibule.core.system.physics;

import java.util.ArrayList;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.artemis.systems.IntervalSystem;
import com.badlogic.gdx.physics.box2d.World;

import net.mostlyoriginal.api.system.delegate.EntityProcessAgent;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;

public class PhysicsSystem extends IntervalSystem implements EntityProcessPrincipal {

	public static final float PHYSICS_TICK_RATE = 1 / 10f;
	public static final int VELOCITY_ITERATIONS = 8;
	public static final int POSITION_ITERATIONS = 3;

	private ArrayList<BaseSystem> preSystems = new ArrayList<>();
	private ArrayList<BaseSystem> postSystems = new ArrayList<>();

	@Wire
	private World box2dWorld;

	private float accDelta;
	private long stepCount;

	long startTime;
	long pStp;
	long timestamp = System.currentTimeMillis();

	public PhysicsSystem() {
		super(Aspect.all(), PHYSICS_TICK_RATE);
		startTime = System.currentTimeMillis();
	}

	public long getStepCount() {
		return stepCount;
	}

	public void addPre(BaseSystem system) {
		if (!preSystems.contains(system)) {
			preSystems.add(system);
		}
	}

	public void addPost(BaseSystem system) {
		if (!postSystems.contains(system)) {
			postSystems.add(system);
		}
	}

	public void removePre(BaseSystem system) {
		preSystems.remove(system);
	}

	public void removePost(BaseSystem system) {
		postSystems.remove(system);
	}

	@Override
	protected void processSystem() {
		float actualDelta = getIntervalDelta() + accDelta;
		while (actualDelta > PHYSICS_TICK_RATE) {
			for (int i = 0; i < preSystems.size(); i++) {
				preSystems.get(i).process();
			}
			box2dWorld.step(PHYSICS_TICK_RATE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			for (int i = 0; i < postSystems.size(); i++) {
				postSystems.get(i).process();
			}
			
			stepCount++;
			actualDelta -= PHYSICS_TICK_RATE;
		}
		long ts = System.currentTimeMillis() ;
		if (ts - timestamp >= 1000) {
			System.out.println((stepCount - pStp) );
			pStp = stepCount;
			timestamp = ts;
		}
		accDelta = actualDelta;
	}

	@Override
	public void registerAgent(int entityId, EntityProcessAgent agent) {

	}

	@Override
	public void unregisterAgent(int entityId, EntityProcessAgent agent) {

	}
}
