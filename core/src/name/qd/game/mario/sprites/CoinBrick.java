package name.qd.game.mario.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import name.qd.game.mario.MarioDemo;
import name.qd.game.mario.items.ItemDef;
import name.qd.game.mario.items.TurtleShell;
import name.qd.game.mario.scenes.Hud;
import name.qd.game.mario.screens.PlayScreen;

public class CoinBrick extends InteractiveTileObject {
    private Hud hud;
    private TiledMapTileSet tileSet;
    private static final int BLANK_COIN = 28;
    private boolean isBreak = false;
    private Sound coinSound;
    private Sound bumpSound;
    private Sound spawnSound;

    public CoinBrick(PlayScreen screen, World world, TiledMap map, MapObject mapObject, Hud hud, AssetManager assetManager) {
        super(screen, world, map, mapObject);
        this.hud = hud;
        tileSet = map.getTileSets().getTileSet("NES - Super Mario Bros - Tileset");
        fixture.setUserData(this);
        setCategoryFilter(MarioDemo.COINBRICK_BIT);
        coinSound = assetManager.get("audio/sound/smb_coin.wav", Sound.class);
        bumpSound = assetManager.get("audio/sound/smb_bump.wav", Sound.class);
        spawnSound = assetManager.get("audio/sound/smb_powerup_appears.wav", Sound.class);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin Brick", "on hit");
        if(!isBreak) {
            hud.addScore(500);
            getCell().setTile(tileSet.getTile(BLANK_COIN));
            if(mapObject.getProperties().containsKey("turtleShell")) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioDemo.PIXEL_PER_METER), TurtleShell.class));
                spawnSound.play();
            } else {
                coinSound.play();
            }
            isBreak = true;
        } else {
            bumpSound.play();
        }
    }
}
