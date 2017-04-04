package com.lexxiconstudios.vestibule.core.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class LXSprite extends Component {
	private Sprite sprite;

	public Sprite get() {
		return sprite;
	}

	public void set(Sprite texture) {
		this.sprite = texture;
	}
}
