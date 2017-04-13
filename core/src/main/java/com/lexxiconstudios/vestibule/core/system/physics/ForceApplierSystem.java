package com.lexxiconstudios.vestibule.core.system.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.physics.box2d.Body;
import com.lexxiconstudios.vestibule.core.component.PhysicsBody;
import com.lexxiconstudios.vestibule.core.component.PhysicsForce;
import com.lexxiconstudios.vestibule.core.system.LockStepSystem;

public class ForceApplierSystem extends LockStepSystem {

	ComponentMapper<PhysicsBody> bodyMapper;
	ComponentMapper<PhysicsForce> forceMapper;

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
			Body b = bodyMapper.get(e).getB2dBody();
			b.applyForceToCenter(forceMapper.get(e).force, true);
			forceMapper.get(e).force.setZero();
		}
	}

}
