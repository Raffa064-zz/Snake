package com.raffa064.snake.game;

public class Map {
	private int width, height;
	private int[] map;

	public Map(int width, int height) {
		set(width, height);
	}

	public int get(int x, int y) {
		return map[y * width + x];
	}

	//chamado para atualizar a matriz do mapa (RESETA OS OBSTACULOS)
	private void update() {
		map = new int[width*height];
	}

	public void set(int width, int height) {
		this.width = width;
		this.height = height;
		update();
	}

	public void setWidth(int width) {
		this.width = width;
		update();
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
		update();
	}

	public int getHeight() {
		return height;
	}
	
	//adicionar/remover um obstaculo
	public void addObstacle(int x, int y, int w, int h) {
		for (int i = y; i < y+h; i++) {
			for (int j = x; j < x+w; j++) {
				map[i * width + j] ^= 1; //reverte o valor
			}
		}
	}
	
	//adicionar/remover varios obstaculos
	public void addObstacles(int... xywh) {
		for (int i = 0; i < xywh.length; i += 4) {
			addObstacle(xywh[i], xywh[i+1], xywh[i+2], xywh[i+3]);
		}
	}
}
