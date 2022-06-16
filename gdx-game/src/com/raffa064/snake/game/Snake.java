package com.raffa064.snake.game;

import java.util.List;
import java.util.ArrayList;
import com.raffa064.snake.game.Snake.SnakePart;

public class Snake {
	public static final int LEFT = 0b0001;  //1
	public static final int RIGHT = 0b0010; //2
	public static final int DOWN = 0b01000; //8
	public static final int UP = 0b10000;   //16
	
    private List<SnakePart> body = new ArrayList<>();
	private int lastWalkedDirection, walkingDirection;
	private boolean isDead;
	
	//OBS: a direçao deve equivaler a uma das constantes de direçao.
	public Snake(int x, int y, int initialSize, int direction) {
		int directionX = directionX(direction);
		int directionY = directionY(direction);
		lastWalkedDirection = walkingDirection = direction;
		for (int i = 0; i < initialSize; i++) {
			body.add(new SnakePart(x - (i * directionX), y - (i * directionY)));
		}
	}

	public void setIsDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isDead() {
		return isDead;
	}

	public void increaseSize() {
		Snake.SnakePart lastPart = body.get(body.size() - 1);
		body.add(new SnakePart(lastPart.getX(), lastPart.getY()));
	}

	public void setWalkingDirection(int walkingDirection) {
		boolean isInverse = (this.lastWalkedDirection == walkingDirection * 2) || (this.lastWalkedDirection == walkingDirection / 2); 
		if (isInverse) return; //bloquear direcoes inversas
		this.walkingDirection = walkingDirection;
	}

	public int getWalkingDirection() {
		return walkingDirection;
	}
	
	public SnakePart getHead() {
		return body.get(0);
	}

	public List<SnakePart> getBody() {
		return body;
	}

	public int directionX(int direction) {
		return ((direction & 0b0010) >> 1) + -(direction & 0b0001);
	}
	
	public int directionY(int direction) {
		direction /= 2;
		return ((direction & 0b1000) >> 3) + -((direction & 0b0100) >> 2);
	}
	
	//Andar para frente
	public void goForward(Map map) {
		int directionX = directionX(walkingDirection);
		int directionY = directionY(walkingDirection);
		lastWalkedDirection = walkingDirection;
		for (int i = body.size()-1; i >= 0; i--) {
			SnakePart part = body.get(i);
			SnakePart nextPart = body.get(Math.min(body.size()-1, i+1));
			nextPart.set(part.getX(), part.getY());
			int x = (part.getX() + directionX) % map.getWidth();
			int y = (part.getY() + directionY) % map.getHeight();
			if (x < 0) {
				x = map.getWidth() + x;
			}
			if (y < 0) {
				y = map.getHeight() + y;
			}
			part.set(x, y);
		}
		SnakePart head = getHead();
		for (SnakePart part : body) { //Head collision with obstacles and other parts
			if ((map.get(part.getX(), part.getY()) != 0) || (part != head && head.overlaps(part))) {
				setIsDead(true);
				break;
			}
		}
	}
	
	public static class SnakePart {
		private int x, y;

		public SnakePart(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public boolean overlaps(SnakePart part) {
			return x == part.x && y == part.y;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getX() {
			return x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getY() {
			return y;
		}
		
		public void set(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
