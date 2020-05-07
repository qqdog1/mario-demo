package name.qd.game.mario;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import name.qd.game.mario.screens.PlayScreen;

public class MarioDemo extends Game {
	public static final int VIRTUAL_WIDTH = 400;
	public static final int VIRTUAL_HEIGHT = 256;
	public static final float PIXEL_PER_METER = 100;

	public static final short DEFAULT_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short COINBRICK_BIT = 16;
	public static final short DESTROYED_BIT = 32;

	public SpriteBatch spriteBatch;

	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		spriteBatch.dispose();
	}
}