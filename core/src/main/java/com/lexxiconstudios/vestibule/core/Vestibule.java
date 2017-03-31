package com.lexxiconstudios.vestibule.core;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

public class Vestibule implements ApplicationListener {
	Texture texture;
	SpriteBatch batch;
	float elapsed;
	ParticleEffect peff;

	@Override
	public void create () {
		texture = new Texture(Gdx.files.internal("libgdx-logo.png"));
		batch = new SpriteBatch();
		peff = new ParticleEffect();
		System.out.println(Gdx.files.internal("effects").file().getAbsolutePath());
		peff.load(Gdx.files.internal("effects/defaultFire.p"), Gdx.files.internal("effects"));
		peff.setPosition(50, 50);
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {
		elapsed += Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		batch.begin();
		peff.draw(batch);
		peff.update(Gdx.graphics.getDeltaTime());
		peff.start();
//		batch.draw(texture, 100+100*(float)Math.cos(elapsed), 100+25*(float)Math.sin(elapsed));
		batch.end();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
