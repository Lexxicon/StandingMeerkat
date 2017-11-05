package com.lexxiconstudios.vestibule.core.system.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.SkipWire;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.lexxiconstudios.vestibule.core.component.GravitySource;
import com.lexxiconstudios.vestibule.core.component.PhysicsForce;
import com.lexxiconstudios.vestibule.core.component.markers.AffectedByGravity;
import com.lexxiconstudios.vestibule.core.system.GravityKeeperSystem;
import com.lexxiconstudios.vestibule.core.system.LockStepSystem;

import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;

public class GravitySystem extends LockStepSystem {

	GravityKeeperSystem gravitySourceKeeper;

	ComponentMapper<PhysicsForce> forceMapper;
	ComponentMapper<Pos> positionMapper;
	ComponentMapper<Angle> angleMapper;
	ComponentMapper<GravitySource> gravitySourceMapper;
	
	public GravitySystem(PhysicsSystem principal) {
		super(Aspect.all(PhysicsForce.class, AffectedByGravity.class), principal);
		principal.addPre(this);
	}	
	
	@Override
	protected void dispose() {
		super.dispose();
		gravitySourceKeeper.dispose();
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
		IntBag gravitySources = gravitySourceKeeper.getEntityIds();
		Pos targetPos = positionMapper.get(e);
		PhysicsForce targetVel = forceMapper.get(e);
		for(int i = 0; i < gravitySources.size(); i++){
			int gravSoureEntID = gravitySources.get(i);
			GravitySource gSrc = gravitySourceMapper.get(gravSoureEntID);	
			Vector2 gravOrigin = gSrc.getOrigin(positionMapper, angleMapper);
			float dist = targetPos.xy.dst(gravOrigin);
			if(dist < gSrc.sphereOfInfluence){
				targetVel.force.add(calcGravity(targetPos, dist, gravOrigin, gSrc));
			}
		}
	}
	
	protected Vector2 calcGravity(Pos targetPos, float dist, Vector2 gravOrign, GravitySource gravSrc){
		Vector2 out = gravOrign.cpy().sub(targetPos.xy);
		float vecSum = Math.abs(out.x)+Math.abs(out.y);
		out.scl((1/vecSum) * gravSrc.effectiveGravity / dist);
		return out;
	}
}
