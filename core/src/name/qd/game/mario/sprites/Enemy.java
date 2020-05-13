package name.qd.game.mario.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite {
    protected World world;
    protected Body body;
    protected Vector2 velocity;

    public Enemy(TextureRegion textureRegion, World world, float x, float y) {
        super(textureRegion);
        this.world = world;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, 0);
    }

    public void reverseVelocity(boolean x, boolean y) {
        if(x) {
            velocity.x = -velocity.x;
        }
        if(y) {
            velocity.y = -velocity.y;
        }
    }

    protected abstract void defineEnemy();
    public abstract void hitOnHead();
}
