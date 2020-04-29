package name.qd.game.mario.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import name.qd.game.mario.MarioDemo;
import name.qd.game.mario.scenes.Hud;

public class PlayScreen implements Screen {
    private MarioDemo game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Hud hud;

    public PlayScreen(MarioDemo game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(MarioDemo.VIRTUAL_WIDTH, MarioDemo.VIRTUAL_HEIGHT, camera);
        hud = new Hud(game.spriteBatch);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.spriteBatch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
