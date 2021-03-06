package name.qd.game.mario.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite {
    protected World world;
    public Body body;
    protected Vector2 velocity;

    public Enemy(TextureRegion textureRegion, World world, float x, float y) {
        super(textureRegion);
        this.world = world;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, -0.5f);
        body.setActive(false);
    }

    public void reverseVelocity(boolean x, boolean y) {
        if(x) {
            velocity.x = -velocity.x;
        }
        if(y) {
            velocity.y = -velocity.y;
        }
    }

    protected int getTextureX(int i) {
        return 0 + (i * 16);
    }

    public abstract void update(float deltaTime);
    protected abstract void defineEnemy();
    public abstract void hitOnHead(Mario mario);
    public abstract void onEnemyHit(Enemy enemy);
    public abstract void killed();
}
