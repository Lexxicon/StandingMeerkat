package com.lexxiconstudios.vestibule.core.system.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.lexxiconstudios.vestibule.core.component.PhysicsBody;
import com.lexxiconstudios.vestibule.core.component.PhysicsForce;

import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;

public class ForceApplierSystem extends DeferredEntityProcessingSystem {

	ComponentMapper<PhysicsBody> bodyMapper;
	ComponentMapper<PhysicsForce> forceMapper;

	long timestamp = System.currentTimeMillis();
	Vector2 cache = new Vector2();
	
	public ForceApplierSystem(PhysicsSystem principal) {
		super(Aspect.all(PhysicsBody.class, PhysicsForce.class), principal);
		principal.addPre(this);
	}

	/** @inheritDoc */
	@Override
	protected final void processSystem() {
		IntBag actives = subscription.getEntities();
		int[] ids = actives.getData();
		for (int i = 0, s = actives.size(); s > i; i++) {
			process(ids[i]);
		}
	}

	@Override
	protected void process(int e) {
		if (!forceMapper.get(e).force.isZero()) {
			bodyMapper.get(e).getB2dBody().setLinearVelocity(forceMapper.get(e).force);
			cache.add(forceMapper.get(e).force);
			forceMapper.get(e).force.setZero();
		}
		
		if(System.currentTimeMillis() - timestamp > 1000){
			System.out.println(cache);
			cache.setZero();
			timestamp = System.currentTimeMillis();
		}
	}

}
