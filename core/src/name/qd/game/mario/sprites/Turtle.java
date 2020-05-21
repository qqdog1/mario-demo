package name.qd.game.mario.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class Turtle extends Enemy {
    private float stateTime;
    private Animation walking;
    private TextureRegion shell;
    private AssetManager assetManager;

    public Turtle(TextureRegion textureRegion, World world, float x, float y) {
        super(textureRegion, world, x, y);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    protected void defineEnemy() {

    }

    @Override
    public void hitOnHead() {

    }
}
