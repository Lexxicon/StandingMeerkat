package com.lexxiconstudios.vestibule.core.system;

import com.artemis.Aspect.Builder;

import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;

public class HighlightSystem extends DeferredEntityProcessingSystem{


	public HighlightSystem(Builder aspect, EntityProcessPrincipal principal) {
		super(aspect, principal);
	}

	@Override
	protected void process(int e) {
		
	}

}
