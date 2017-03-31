package com.lexxiconstudios.vestibule.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.lexxiconstudios.vestibule.core.Vestibule;

public class VestibuleDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Vestibule(), config);
	}
}
