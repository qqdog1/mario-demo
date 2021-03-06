package name.qd.game.mario.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import name.qd.game.mario.MarioDemo;
import name.qd.game.mario.screens.PlayScreen;

public class Coin extends InteractiveTileObject {
    private AssetManager assetManager;

    public Coin(PlayScreen screen, World world, TiledMap map, MapObject mapObject, AssetManager assetManager) {
        super(screen, world, map, mapObject);
        this.assetManager = assetManager;
        fixture.setUserData(this);
        setCategoryFilter(MarioDemo.COIN_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Coin", "on hit");
        setCategoryFilter(MarioDemo.DESTROYED_BIT);
        getCell().setTile(null);
    }
}
