package name.qd.game.mario.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import name.qd.game.mario.MarioDemo;

public class CoinBrick extends InteractiveTileObject {
    public CoinBrick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MarioDemo.COINBRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin Brick", "on hit");
    }
}
