package com.lexxiconstudios.vestibule.core.component;

import com.artemis.Component;

public class Sprite extends Component {
	private com.badlogic.gdx.graphics.g2d.Sprite texture;

	public com.badlogic.gdx.graphics.g2d.Sprite getTexture() {
		return texture;
	}

	public void setTexture(com.badlogic.gdx.graphics.g2d.Sprite texture) {
		this.texture = texture;
	}
}
