package com.lexxicon.vestibule;

import java.time.Duration;
import java.time.LocalDateTime;

import org.jbox2d.common.Vec2;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.lexxicon.vestibule.system.PhysicsSystem;

public class GameWorld {
	org.jbox2d.dynamics.World physicsWorld;
	com.artemis.World esWorld;

	long lastTime;
	PhysicsSystem physicsSystem;

	public static void main(String[] args) throws InterruptedException {
		GameWorld gw = new GameWorld();
		gw.setup();
		LocalDateTime DT = LocalDateTime.now();

		for (int i = 0; i < 100_000; i++) {
			gw.update();
		}
		System.out.println(gw.physicsSystem.getCount());
		System.out.println(Duration.between(DT, LocalDateTime.now()));
	}

	public void setup() {
		physicsWorld = new org.jbox2d.dynamics.World(new Vec2());
		lastTime = System.currentTimeMillis();
		physicsSystem = new PhysicsSystem();
		WorldConfiguration cfg = new WorldConfigurationBuilder().with(physicsSystem).build().register(physicsWorld);
		esWorld = new com.artemis.World(cfg);
	}

	public void update() throws InterruptedException {
		long t = System.currentTimeMillis();
		esWorld.setDelta((float) (t - lastTime) / 1_000f);
		esWorld.process();
		lastTime = t;
	}
}
