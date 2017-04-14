package com.lexxiconstudios.vestibule.core.system.control;

import org.lwjgl.input.Mouse;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Vector3;
import com.lexxiconstudios.vestibule.core.component.MouseComponent;
import com.lexxiconstudios.vestibule.core.system.camera.LXViewportSystem;

import net.mostlyoriginal.api.component.basic.Pos;

public class MouseCursorSystem extends IteratingSystem {

	ComponentMapper<Pos> pm;
	LXViewportSystem viewportSystem;
	Cursor empty;
	boolean captureCursor = true;

	public MouseCursorSystem() {
		super(Aspect.all(Pos.class, MouseComponent.class));
		empty = Gdx.graphics.newCursor(new Pixmap(1, 1, Format.RGBA8888), 0, 0);
	}
	@Override
	protected void dispose() {
		super.dispose();
		empty.dispose();
	}
	public void begin() {
		if (Mouse.isInsideWindow()) {
			Gdx.graphics.setCursor(empty);
		}
	}

	@Override
	protected void process(int entityId) {

		Vector3 v3 = viewportSystem.viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		pm.get(entityId).xy.set(v3.x, v3.y);
	}

}
