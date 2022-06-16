package com.raffa064.snake.game;

import com.badlogic.gdx.graphics.Color;

public class Palette {
	public float currentHue;
	public Color bg;
	public Color mapBg;
	public Color mapGridBg;
	public Color apple;
	public Color obstacle;
	public Color snakeOutline;
	public Color snake0;
	public Color snake1;
	public Color snake2;
	public Color ui;
	
	public void set(float hue) {
		currentHue = hue;
		bg = hsv(hue, 0.2f, 1);
		mapBg = hsv(hue, 0.5f, 0.3f);
		mapGridBg = hsv(hue, 0.5f, 0.26f);
		apple = hsv(hue, 1, 1);
		obstacle = hsv((hue+45) % 360, 0.3f, 1);
		snakeOutline = hsv(hue, 0.5f, 0.9f);
		snake0 = hsv((hue+20) % 360, 0.8f, 1);
		snake1 = hsv((hue+40) % 360, 0.8f, 1);
		snake2 = hsv((hue+50) % 360, 0.8f, 1);
		ui = hsv(hue, 0.5f, 1);
	}
	
	private Color hsv(float h, float s, float v) {
		return new Color((android.graphics.Color.HSVToColor(1, new float[]{h, s, v}) << 8) | 0xff);
	}
}
