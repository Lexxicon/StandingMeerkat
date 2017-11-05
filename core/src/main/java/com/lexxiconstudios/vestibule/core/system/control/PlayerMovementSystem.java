package com.lexxiconstudios.vestibule.core.system.control;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.lexxiconstudios.vestibule.core.component.PhysicsForce;
import com.lexxiconstudios.vestibule.core.component.markers.PlayerControlled;

public class PlayerMovementSystem extends IteratingSystem {

	ComponentMapper<PhysicsForce> moveMapper;

	public static final float NEWTONS_PER_SECOND = 10;
	public static final float NWT_P_MS = NEWTONS_PER_SECOND * 1000;
	
	public PlayerMovementSystem() {
		super(Aspect.all(PlayerControlled.class, PhysicsForce.class));
	}

	@Override
	protected void process(int entityId) {
		Vector2 dir = new Vector2();

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			dir.y += 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			dir.x -= 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			dir.y -= 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			dir.x += 1;
		}
		moveMapper.get(entityId).force.add(dir.nor().scl(NWT_P_MS * world.delta));
	}

}
