package name.qd.game.mario.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite {
    protected World world;
    protected Body body;

    public Enemy(TextureRegion textureRegion, World world, float x, float y) {
        super(textureRegion);
        this.world = world;
        setPosition(x, y);
        defineEnemy();
    }

    protected abstract void defineEnemy();
    public abstract void hitOnHead();
}
