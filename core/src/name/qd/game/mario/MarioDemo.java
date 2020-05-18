package name.qd.game.mario;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import name.qd.game.mario.screens.PlayScreen;

public class MarioDemo extends Game {
	public static final int VIRTUAL_WIDTH = 400;
	public static final int VIRTUAL_HEIGHT = 256;
	public static final float PIXEL_PER_METER = 100;

	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short COINBRICK_BIT = 16;
	public static final short DESTROYED_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short OBJECT_BIT = 128;
	public static final short ENEMY_HEAD_BIT = 256;
	public static final short ITEM_BIT = 512;

	private AssetManager assetManager;
	public SpriteBatch spriteBatch;

	@Override
	public void create () {
		spriteBatch = new SpriteBatch();

		assetManager = new AssetManager();
		assetManager.load("audio/music/MarioBros.mp3", Music.class);
		assetManager.load("audio/sound/smb_coin.wav", Sound.class);
		assetManager.load("audio/sound/smb_breakblock.wav", Sound.class);
		assetManager.load("audio/sound/smb_bump.wav", Sound.class);
		assetManager.load("audio/sound/smb_jump-small.wav", Sound.class);
		assetManager.finishLoading();

		setScreen(new PlayScreen(this, assetManager));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		spriteBatch.dispose();
		assetManager.dispose();
	}
}