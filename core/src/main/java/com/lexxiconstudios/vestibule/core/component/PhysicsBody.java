package com.lexxiconstudios.vestibule.core.component;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class PhysicsBody extends Component {

	private Body b2dBody;

	public PhysicsBody() {
	}

	public void setB2dBody(Body b2dBody) {
		this.b2dBody = b2dBody;
	}

	public Body getB2dBody() {
		return b2dBody;
	}
}
