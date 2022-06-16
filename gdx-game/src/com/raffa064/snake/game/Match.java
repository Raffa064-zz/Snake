package com.raffa064.snake.game;

import java.util.ArrayList;
import java.util.List;

public class Match {
    public Map map;
	public Snake snake;
	public float timer;
	public float speed = 0.15f; //em segundos
	public int points;
	public List<Apple> apples = new ArrayList<>();
	public Palette palette = new Palette();
	public float targetHue, transition = 0.02f;
	
	public void update(float delta) {
		palette.set(palette.currentHue + ((targetHue - palette.currentHue) * transition));
		if (snake.isDead()) {
			targetHue = 360;
			return;
		}
		for (int i = 0; i < apples.size(); i++) {
			Apple apple = apples.get(i);
			if (snake.getHead().getX() == apple.getX() && snake.getHead().getY() == apple.getY()) {
				apples.remove(apple);
				points++;
				snake.increaseSize();
				summonApple();
				targetHue = (float) (Math.random() * 360);
				i--;
			}
		}
		timer += delta;
		if (timer >= speed) {
			timer = 0;
			snake.goForward(map);
		}
	}

	public void summonApple() {
		int x, y;
		do {
			x = (int)(Math.random() * map.getWidth());
			y = (int)(Math.random() * map.getHeight());
		} while(map.get(x, y) == 1);
		apples.add(new Apple(x, y));
	}
}
