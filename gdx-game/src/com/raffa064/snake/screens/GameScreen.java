package com.raffa064.snake.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.raffa064.snake.game.Apple;
import com.raffa064.snake.game.Map;
import com.raffa064.snake.game.Match;
import com.raffa064.snake.game.Snake;
import java.util.List;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import com.raffa064.snake.game.Menu;
import com.badlogic.gdx.graphics.Texture;
import com.raffa064.snake.MyGdxGame;

public class GameScreen extends ScreenAdapter implements InputProcessor {
	private MyGdxGame game;
	private int width;
	private int height;
	private Music music;
	private ShapeRenderer shape;
	private SpriteBatch batch;
	private Menu menu;
	private float menuSize;
	private Texture replay;
	private Match match = new Match();
	private Vector2 lastTouch = new Vector2();

	public GameScreen(MyGdxGame game) {
		this.game = game;
	}
	
  	@Override
	public void show() {
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.play();
		shape = new ShapeRenderer();
		batch = new SpriteBatch();
		menu = new Menu();
		menu.setOnChangePalette(new Runnable() {
			@Override
			public void run() {
				match.targetHue = (float) (Math.random() * 360);
			}
		});
		menu.setOnToggleSoundState(new Runnable() {
			@Override
			public void run() {
				menu.setPlayingSound(!menu.isPlayingSound());
			}
		});
		menu.setOnToggleGameState(new Runnable() {
			@Override
			public void run() {
				menu.setPlayingGame(!menu.isPlayingGame());
			}
		});
		menu.loadAssets();
		replay = new Texture("replay.png");
        match.map = new Map(32, 32);
		int snakeX = 16, snakeY = 31;
		switch((int)Math.floor(Math.random() * 4)) {
			case 0: 
				match.map.addObstacles(
					0, 15, 8, 2,
					24, 15, 8, 2
				); 
			break;
			case 1: 
				match.map.addObstacles(
					8, 8, 16, 16
				); 
			break;
			case 2: 
				match.map.addObstacles(
					0, 0, 4, 4,
					28, 0, 4, 4,
					0, 28, 4, 4,
					28, 28, 4, 4,
					14, 14, 4, 4
				); 
			break;
			case 3: 
				match.map.addObstacles(
					0, 0, 32, 32,
					1, 1, 30, 30,
					14, 14, 4, 4,
					14, 15, 4, 2
				); 
				snakeX = 16;
				snakeY = 3;
			break;
		}
		match.snake = new Snake(snakeX, snakeY, 3, match.snake.RIGHT);
		match.summonApple();
		match.palette.set(match.targetHue = (float) (Math.random() * 360));
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		menuSize = Math.min(90, Math.max(50, height/100*10)); //Tamanho do menu de 50 a 90 pixels, lembrando q o menu vai ter 10px de margem de cada lado
		workaroundControls(); //Tambem conhecido como gambiarra
		
		if (menu.isPlayingGame()) match.update(delta);
		
		shape.begin(ShapeRenderer.ShapeType.Filled);
		
		shape.setColor(match.palette.bg);
		shape.rect(0, 0, width, height);
		
		for (int i = 0; i < match.map.getHeight(); i++) {
			for (int j = 0; j < match.map.getWidth(); j++) {
				int tileType = match.map.get(j, i);
				shape.setColor(match.palette.mapGridBg);
				drawOnGrid(j, i, 0);
				if (tileType == 0) {
					shape.setColor(match.palette.mapBg);
					drawOnGrid(j, i, 5);
				} else {
					shape.setColor(match.palette.obstacle);
					drawOnGrid(j, i, 0);
				}
			}
		}
		
		List<Snake.SnakePart> snakeBody = match.snake.getBody();
		for (int i = snakeBody.size()-1; i >= 0; i--) {
			Snake.SnakePart part = snakeBody.get(i);
			shape.setColor(match.palette.snakeOutline);
			drawOnGrid(part.getX(), part.getY(), 0);
			shape.setColor(new Color[]{
				match.palette.snake0,
				match.palette.snake1,
				match.palette.snake2
			}[i % 3]);
			drawOnGrid(part.getX(), part.getY(), 10);
		}
		
		Snake.SnakePart head = match.snake.getHead();
		shape.setColor(Color.WHITE);
		drawOnGrid(head.getX(), head.getY(), 30);
		shape.setColor(Color.BLACK);
		int direction = match.snake.getWalkingDirection();
		drawOnGrid(head.getX() + match.snake.directionX(direction) * 0.125f, head.getY()  + match.snake.directionY(direction) * 0.125f, 40);
		
		for (int i = 0; i < match.apples.size(); i++) {
			Apple apple = match.apples.get(i);
			shape.setColor(match.palette.apple);
			drawOnGrid(apple.getX(), apple.getY(), 20);
		}
		shape.end();
		
		batch.begin();
		batch.setColor(match.palette.ui);
		menu.render(batch, width, height, menuSize);
		if (match.snake.isDead()) {
			float s = Math.min(width, height) / 3f;
			float replayX = width / 2 - s / 2;
			float replayY = height / 2 - s / 2;
			batch.draw(replay, replayX, replayY, s, s);
			if (Gdx.input.justTouched()) {
				float x = Gdx.input.getX();
				float y = height - Gdx.input.getY();
				if (x > replayX && x < replayY + s && y > replayY && y < replayY + s) {
					game.setScreen(new GameScreen(game));
				}
			}
		}
		batch.end();
		
		if (menu.isPlayingSound()) {
			music.play();
		} else {
			music.pause();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		music.pause();
		music.stop();
		music.dispose();
		shape.dispose();
		batch.dispose();
		menu.dispose();
		replay.dispose();
	}

	private void drawOnGrid(float x, float y, float marginPercent) {
		int min = Math.min(width, (int)(height - menuSize)) - 20; //10 pixels de margem para cada lado
		float marginX = (width / 2f) - (min / 2f);
		float marginY = (height / 2f) - (min / 2f);
		float tileSize = min / Math.min(match.map.getWidth(), match.map.getHeight()); 	
		float margin = tileSize / 100 * marginPercent;
		shape.rect(
			marginX + x * tileSize + margin,
			marginY + y * tileSize + margin,
			tileSize - margin * 2, 
			tileSize - margin * 2
		);
	}

	private void workaroundControls() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			match.snake.setWalkingDirection(match.snake.LEFT);
		} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			match.snake.setWalkingDirection(match.snake.RIGHT);
		} else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			match.snake.setWalkingDirection(match.snake.DOWN);
	  	} else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			match.snake.setWalkingDirection(match.snake.UP);
		}
	}
	
	@Override
	public boolean keyDown(int p1) {
		return false;
	}

	@Override
	public boolean keyUp(int p1) {
		return false;
	}

	@Override
	public boolean keyTyped(char p1) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int p3, int p4) {
		lastTouch.set(x, y);
		return false;
	}

	@Override
	public boolean touchUp(int p1, int p2, int p3, int p4) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int p3) {
		if (Math.abs(x - lastTouch.x) > Math.abs(y - lastTouch.y)) {
			match.snake.setWalkingDirection(x - lastTouch.x < 0 ? Snake.LEFT : Snake.RIGHT);
		} else {
			match.snake.setWalkingDirection(y - lastTouch.y < 0 ? Snake.UP : Snake.DOWN);
		}
		lastTouch.set(x, y);
		return false;
	}

	@Override
	public boolean mouseMoved(int p1, int p2) {
		return false;
	}

	@Override
	public boolean scrolled(float p1, float p2) {
		return false;
	}

}
