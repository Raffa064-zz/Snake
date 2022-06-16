package com.raffa064.snake;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.raffa064.snake.screens.GameScreen;

public class MyGdxGame extends Game {
	private AndroidOperations aOperations;
	private Screen lastScreen;
	
	public MyGdxGame(AndroidOperations androidOperations) {
		aOperations = androidOperations;
	}
	
	@Override
	public void create() {
		setScreen(new GameScreen(this));
	}

	@Override
	public void render() {        
		if (lastScreen != null) {
			lastScreen.dispose();
			lastScreen = null;
		}
	    Gdx.gl.glClearColor(1, 1, 1, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	@Override
	public void setScreen(Screen screen) {
		lastScreen = getScreen();
		super.setScreen(screen);
	}
}
