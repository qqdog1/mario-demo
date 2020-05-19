package name.qd.game.mario.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

import name.qd.game.mario.MarioDemo;
import name.qd.game.mario.items.Item;
import name.qd.game.mario.items.ItemDef;
import name.qd.game.mario.items.TurtleShell;
import name.qd.game.mario.listener.WorldContactListener;
import name.qd.game.mario.scenes.Hud;
import name.qd.game.mario.sprites.Brick;
import name.qd.game.mario.sprites.Coin;
import name.qd.game.mario.sprites.CoinBrick;
import name.qd.game.mario.sprites.Enemy;
import name.qd.game.mario.sprites.Goomba;
import name.qd.game.mario.sprites.Mario;

public class PlayScreen implements Screen {
    private MarioDemo game;
    private AssetManager assetManager;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Mario mario;
    private Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    private Array<Goomba> goombas;

    public PlayScreen(MarioDemo game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        camera = new OrthographicCamera();
        viewport = new FitViewport(MarioDemo.VIRTUAL_WIDTH / MarioDemo.PIXEL_PER_METER, MarioDemo.VIRTUAL_HEIGHT / MarioDemo.PIXEL_PER_METER, camera);
        hud = new Hud(game.spriteBatch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tilemap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MarioDemo.PIXEL_PER_METER);

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        box2DDebugRenderer = new Box2DDebugRenderer();
        mario = new Mario(world, assetManager);
        goombas = new Array<>();

        world.setContactListener(new WorldContactListener());

        music = assetManager.get("audio/music/MarioBros.mp3", Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<>();
        itemsToSpawn = new LinkedBlockingQueue<>();

        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        setFixture(map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class), bodyDef, polygonShape, fixtureDef);
        setPipeFixture(map.getLayers().get("pipes").getObjects().getByType(RectangleMapObject.class), bodyDef, polygonShape, fixtureDef);
        setCoinFixture(map.getLayers().get("coins").getObjects().getByType(RectangleMapObject.class), bodyDef, polygonShape, fixtureDef);
        setBrickFixture(map.getLayers().get("bricks").getObjects().getByType(RectangleMapObject.class), bodyDef, polygonShape, fixtureDef);
        setCoinBrickFixture(map.getLayers().get("coinbricks").getObjects().getByType(RectangleMapObject.class), bodyDef, polygonShape, fixtureDef);
        setGoombaFixture(map.getLayers().get("goombas").getObjects().getByType(RectangleMapObject.class));
    }

    public void spawnItem(ItemDef itemDef) {
        itemsToSpawn.add(itemDef);
    }

    public void handleSpawningItems() {
        if(!itemsToSpawn.isEmpty()) {
            ItemDef itemDef = itemsToSpawn.poll();
            if(itemDef.type == TurtleShell.class) {
                items.add(new TurtleShell(world, itemDef.position.x, itemDef.position.y));
            }
        }
    }

    private void setGoombaFixture(Array<RectangleMapObject> array) {
        for(MapObject mapObject : array) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            goombas.add(new Goomba(world, rectangle.getX() / MarioDemo.PIXEL_PER_METER, rectangle.getY() / MarioDemo.PIXEL_PER_METER, assetManager));
        }
    }

    private void setFixture(Array<RectangleMapObject> array, BodyDef bodyDef, PolygonShape polygonShape, FixtureDef fixtureDef) {
        Body body;
        for(MapObject mapObject : array) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            bodyDef.position.set((rectangle.getX() + (rectangle.getWidth() / 2)) / MarioDemo.PIXEL_PER_METER, (rectangle.getY() + (rectangle.getHeight() / 2)) / MarioDemo.PIXEL_PER_METER);

            body = world.createBody(bodyDef);

            polygonShape.setAsBox(rectangle.getWidth() / 2 / MarioDemo.PIXEL_PER_METER, rectangle.getHeight() / 2 / MarioDemo.PIXEL_PER_METER);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }
    }

    private void setPipeFixture(Array<RectangleMapObject> array, BodyDef bodyDef, PolygonShape polygonShape, FixtureDef fixtureDef) {
        Body body;
        for(MapObject mapObject : array) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            bodyDef.position.set((rectangle.getX() + (rectangle.getWidth() / 2)) / MarioDemo.PIXEL_PER_METER, (rectangle.getY() + (rectangle.getHeight() / 2)) / MarioDemo.PIXEL_PER_METER);

            body = world.createBody(bodyDef);

            polygonShape.setAsBox(rectangle.getWidth() / 2 / MarioDemo.PIXEL_PER_METER, rectangle.getHeight() / 2 / MarioDemo.PIXEL_PER_METER);
            fixtureDef.shape = polygonShape;
            fixtureDef.filter.categoryBits = MarioDemo.OBJECT_BIT;
            body.createFixture(fixtureDef);
        }
    }

    private void setCoinFixture(Array<RectangleMapObject> array, BodyDef bodyDef, PolygonShape polygonShape, FixtureDef fixtureDef) {
        Body body;
        for(MapObject mapObject : array) {
            new Coin(this, world, map, mapObject, assetManager);
        }
    }

    private void setBrickFixture(Array<RectangleMapObject> array, BodyDef bodyDef, PolygonShape polygonShape, FixtureDef fixtureDef) {
        Body body;
        for(MapObject mapObject : array) {
            new Brick(this, world, map, mapObject, hud, assetManager);
        }
    }

    private void setCoinBrickFixture(Array<RectangleMapObject> array, BodyDef bodyDef, PolygonShape polygonShape, FixtureDef fixtureDef) {
        Body body;
        for(MapObject mapObject : array) {
            new CoinBrick(this, world, map, mapObject, hud, assetManager);
        }
    }

    @Override
    public void show() {

    }

    private void handleInput(float deltaTime) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            mario.body.applyLinearImpulse(new Vector2(0, 4f), mario.body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && mario.body.getLinearVelocity().x <= 1) {
            mario.body.applyLinearImpulse(new Vector2(0.1f, 0), mario.body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && mario.body.getLinearVelocity().x >= -1) {
            mario.body.applyLinearImpulse(new Vector2(-0.1f, 0), mario.body.getWorldCenter(), true);
        }
    }

    private void update(float deltaTime) {
        handleInput(deltaTime);
        handleSpawningItems();

        world.step(1/60f, 6, 2);

        mario.update(deltaTime);
        for(Enemy enemy : goombas) {
            enemy.update(deltaTime);
            if(enemy.getX() < mario.getX() + 16*13/MarioDemo.PIXEL_PER_METER) {
                enemy.body.setActive(true);
            }
        }

        for(Item item : items) {
            item.update(deltaTime);
        }

        hud.update(deltaTime);

        camera.position.x = mario.body.getPosition().x;

        camera.update();

        mapRenderer.setView(camera);
    }

    @Override
    public void render(float deltaTime) {
        update(deltaTime);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

        box2DDebugRenderer.render(world, camera.combined);

        game.spriteBatch.setProjectionMatrix(camera.combined);
        game.spriteBatch.begin();
        mario.draw(game.spriteBatch);
        for(Enemy enemy : goombas) {
            enemy.draw(game.spriteBatch);
        }
        for(Item item : items) {
            item.draw(game.spriteBatch);
        }
        game.spriteBatch.end();

        game.spriteBatch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        hud.dispose();
        music.dispose();
    }
}
