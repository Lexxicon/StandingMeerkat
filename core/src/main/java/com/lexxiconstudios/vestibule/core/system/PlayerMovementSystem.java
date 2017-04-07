package com.lexxiconstudios.vestibule.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.lexxiconstudios.vestibule.core.component.PhysicsBody;
import com.lexxiconstudios.vestibule.core.component.PlayerControlled;

public class PlayerMovementSystem extends IteratingSystem {

	ComponentMapper<PhysicsBody> bodyMapper;

	public PlayerMovementSystem() {
		super(Aspect.all(PlayerControlled.class, PhysicsBody.class));
	}

	@Override
	protected void process(int entityId) {
		float xVel = 0, yVel = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			yVel += 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			xVel -= 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			yVel -= 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			xVel += 1;
		}

		bodyMapper.get(entityId).getB2dBody().setLinearVelocity(new Vector2(xVel, yVel).nor().scl(2.5f));
	}

}
