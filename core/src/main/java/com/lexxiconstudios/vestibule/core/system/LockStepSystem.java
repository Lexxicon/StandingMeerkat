package com.lexxiconstudios.vestibule.core.system;

import com.artemis.Aspect.Builder;
import com.lexxiconstudios.vestibule.core.system.physics.PhysicsSystem;

import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;

public abstract class LockStepSystem extends DeferredEntityProcessingSystem {

	public LockStepSystem(Builder aspect, PhysicsSystem principal) {
		super(aspect, principal);
	}
	
	public float getDelta(){
		return PhysicsSystem.PHYSICS_TICK_RATE;
	}
}
