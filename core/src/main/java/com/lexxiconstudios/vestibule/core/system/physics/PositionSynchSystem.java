package com.lexxiconstudios.vestibule.core.system.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
import com.lexxiconstudios.vestibule.core.component.PhysicsBody;
import com.lexxiconstudios.vestibule.core.system.LockStepSystem;

import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;

public class PositionSynchSystem extends LockStepSystem {

	private ComponentMapper<PhysicsBody> bodyMapper;
	private ComponentMapper<Pos> positionMapper;
	private ComponentMapper<Angle> angleMapper;

	public PositionSynchSystem(PhysicsSystem principal) {
		super(Aspect.all(PhysicsBody.class, Pos.class), principal);
		principal.addPost(this);
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
		PhysicsBody body = bodyMapper.get(e);
		Pos p = positionMapper.get(e);
		Angle a = angleMapper.getSafe(e, null);
		if (a != null) {
			a.rotation = (MathUtils.radiansToDegrees * body.getB2dBody().getAngle());
		}
		p.set(body.getB2dBody().getPosition());

	}

}
