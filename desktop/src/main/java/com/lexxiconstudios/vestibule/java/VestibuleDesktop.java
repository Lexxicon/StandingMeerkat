package com.lexxiconstudios.vestibule.java;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.lexxiconstudios.vestibule.core.Vestibule;

public class VestibuleDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 0;
		config.backgroundFPS = 0;
		config.width = 1600;
		config.height = 900;
		config.vSyncEnabled = false;
		new LwjglApplication(new Vestibule(), config);
	}
}
