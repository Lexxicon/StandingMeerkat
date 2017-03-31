package com.lexxiconstudios.vestibule.html;

import com.lexxiconstudios.vestibule.core.Vestibule;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class VestibuleHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new Vestibule();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}

	@Override
	public ApplicationListener createApplicationListener() {
		return new Vestibule();
	}
}
