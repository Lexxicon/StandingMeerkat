package com.lexxiconstudios.vestibule.core.system;

import com.artemis.Aspect;
import com.artemis.Aspect.Builder;
import com.lexxiconstudios.vestibule.core.component.GravitySource;

import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;

public class GravityKeeperSystem extends DeferredEntityProcessingSystem{

	public GravityKeeperSystem(EntityProcessPrincipal principal) {
		super(Aspect.all(GravitySource.class), principal);
	}

	@Override
	protected void process(int e) {
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}