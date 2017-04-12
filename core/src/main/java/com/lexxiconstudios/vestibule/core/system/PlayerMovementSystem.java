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
		
		bodyMapper.get(entityId).getB2dBody().applyForceToCenter(dir.nor().scl(2.5f), true);
	}

}
