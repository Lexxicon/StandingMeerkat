package com.lexxicon.vestibule;

import com.artemis.World;

import processing.core.PApplet;

/**
 * Hello world!
 *
 */
public class App extends PApplet {
	public static void main(String[] args) {
		PApplet.main(App.class);
	}

	World world;

	@Override
	public void settings() {
		size(1200, 800);
	}

	@Override
	public void setup() {
	}

	@Override
	public void draw() {
		background(0);
		System.out.println(width);
	}
}
