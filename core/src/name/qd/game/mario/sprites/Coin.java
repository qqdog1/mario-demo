package name.qd.game.mario.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import name.qd.game.mario.MarioDemo;

public class Coin extends InteractiveTileObject {
    private AssetManager assetManager;

    public Coin(World world, TiledMap map, Rectangle bounds, AssetManager assetManager) {
        super(world, map, bounds);
        this.assetManager = assetManager;
        fixture.setUserData(this);
        setCategoryFilter(MarioDemo.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "on hit");
        setCategoryFilter(MarioDemo.DESTROYED_BIT);
        getCell().setTile(null);
    }
}
