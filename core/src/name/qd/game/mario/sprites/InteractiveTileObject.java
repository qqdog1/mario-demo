package name.qd.game.mario.sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import name.qd.game.mario.MarioDemo;
import name.qd.game.mario.screens.PlayScreen;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    protected Fixture fixture;

    protected MapObject mapObject;
    protected PlayScreen screen;

    public InteractiveTileObject(PlayScreen screen, World world, TiledMap map, MapObject mapObject) {
        this.world = world;
        this.map = map;
        this.bounds = ((RectangleMapObject) mapObject).getRectangle();
        this.screen = screen;
        this.mapObject = mapObject;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((bounds.getX() + bounds.getWidth() / 2) / MarioDemo.PIXEL_PER_METER, (bounds.getY() + bounds.getHeight() / 2) / MarioDemo.PIXEL_PER_METER);
        body = world.createBody(bodyDef);

        shape.setAsBox(bounds.getWidth() / 2 / MarioDemo.PIXEL_PER_METER, bounds.getHeight() / 2 / MarioDemo.PIXEL_PER_METER);
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("graphics");
        return layer.getCell((int)(body.getPosition().x * MarioDemo.PIXEL_PER_METER / 16), (int)(body.getPosition().y * MarioDemo.PIXEL_PER_METER / 16));
    }

    public abstract void onHeadHit(Mario mario);
}
