package name.qd.game.mario.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import name.qd.game.mario.MarioDemo;
import name.qd.game.mario.scenes.Hud;

public class Brick extends InteractiveTileObject {
    private Hud hud;
    private Sound sound;

    public Brick(World world, TiledMap map, Rectangle bounds, Hud hud, AssetManager assetManager) {
        super(world, map, bounds);
        this.hud = hud;
        fixture.setUserData(this);
        setCategoryFilter(MarioDemo.BRICK_BIT);
        sound = assetManager.get("audio/sound/smb_breakblock.wav", Sound.class);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "on hit");
        setCategoryFilter(MarioDemo.DESTROYED_BIT);
        getCell().setTile(null);
        hud.addScore(100);
        sound.play();
    }
}
