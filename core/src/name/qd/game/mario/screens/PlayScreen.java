package name.qd.game.mario.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import name.qd.game.mario.MarioDemo;
import name.qd.game.mario.scenes.Hud;

public class PlayScreen implements Screen {
    private MarioDemo game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    public PlayScreen(MarioDemo game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(MarioDemo.VIRTUAL_WIDTH, MarioDemo.VIRTUAL_HEIGHT, camera);
        hud = new Hud(game.spriteBatch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tilemap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        camera.position.set(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2, 0);
    }

    @Override
    public void show() {

    }

    private void handleInput(float deltaTime) {
        if(Gdx.input.isTouched()) {
            camera.position.x += 100 * deltaTime;
        }
    }

    private void update(float deltaTime) {
        handleInput(deltaTime);
        camera.update();

        mapRenderer.setView(camera);
    }

    @Override
    public void render(float deltaTime) {
        update(deltaTime);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

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

    }
}
