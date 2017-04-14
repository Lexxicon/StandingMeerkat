package com.lexxiconstudios.vestibule.core.system.camera;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.camera.Camera;

public class LXEntityCameraSystem extends IteratingSystem {

	private ComponentMapper<Pos> pm;
	private LXViewportSystem cameraSystem;

	public LXEntityCameraSystem() {
		super(Aspect.all(Pos.class, Camera.class));
	}

	@Override
	protected void process(int e) {
		final Pos pos = pm.get(e);
		cameraSystem.camera.position.x = pos.xy.x;
		cameraSystem.camera.position.y = pos.xy.y;
		cameraSystem.camera.update();
	}
}