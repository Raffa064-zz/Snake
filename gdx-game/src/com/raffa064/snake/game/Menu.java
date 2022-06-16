package com.raffa064.snake.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu {
	private Runnable onToggleSoundState = null;
	private Runnable onChangePalette = null;
	private Runnable onToggleGameState = null;
	private Texture play, pause, palette, soundOn, soundOff;
	private static boolean playingSound = true, playingGame = true;

	public void setOnToggleSoundState(Runnable onToggleSoundState) {
		this.onToggleSoundState = onToggleSoundState;
	}

	public Runnable getOnToggleSoundState() {
		return onToggleSoundState;
	}

	public void setOnChangePalette(Runnable onChangePalette) {
		this.onChangePalette = onChangePalette;
	}

	public Runnable getOnChangePalette() {
		return onChangePalette;
	}

	public void setOnToggleGameState(Runnable onToggleGameState) {
		this.onToggleGameState = onToggleGameState;
	}

	public Runnable getOnToggleGameState() {
		return onToggleGameState;
	}

	public void loadAssets() {
		play = new Texture("play.png");
		pause = new Texture("pause.png");
		palette = new Texture("changePalette.png");
		soundOn = new Texture("soundOn.png");
		soundOff = new Texture("soundOff.png");
	}

	public void render(SpriteBatch batch,float width, float height, float size) {
		Object[][] options = {
			{palette, onChangePalette},
			{isPlayingSound()? soundOn : soundOff, onToggleSoundState},
			{isPlayingGame()? pause : play, onToggleGameState}
		};
		for (int i = 0; i < options.length; i++) {
			float optionX = width - size * (i+1) + 10;
			float optionY = height - size + 10;
			float optionSize = size - 20;
			batch.draw((Texture)options[i][0], optionX, optionY, optionSize, optionSize);
			if (Gdx.input.justTouched()) {
				float x = Gdx.input.getX();
				float y = height - Gdx.input.getY();
				if (x > optionX && x < optionX + optionSize && y > optionY && y < optionY + optionSize) {
					((Runnable)options[i][1]).run();
				}
			}
		}
	}
	
	public void setPlayingSound(boolean playingSound) {
		this.playingSound = playingSound;
	}

	public boolean isPlayingSound() {
		return playingSound;
	}

	public void setPlayingGame(boolean playingGame) {
		this.playingGame = playingGame;
	}

	public boolean isPlayingGame() {
		return playingGame;
	}
	
	public void dispose() {
		for (Texture tex : new Texture[]{play, pause, palette, soundOn, soundOff}) {
			tex.dispose();
		}
	}
}
