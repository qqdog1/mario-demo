package name.qd.game.mario.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import name.qd.game.mario.MarioDemo;
import name.qd.game.mario.sprites.Mario;

public abstract class Item extends Sprite {
    protected World world;
    protected Vector2 velocity;
    protected Body body;

    private boolean isReadyToDestroy;
    private boolean isDestroyed;

    public Item(World world, float x, float y) {
        this.world = world;
        setPosition(x, y);
        setBounds(getX(), getY(), 16 / MarioDemo.PIXEL_PER_METER, 16 / MarioDemo.PIXEL_PER_METER);
        defineItem();
        isReadyToDestroy = false;
        isDestroyed = false;
    }

    public abstract void defineItem();
    public abstract void use(Mario mario);

    public void update(float deltaTime) {
        if(isReadyToDestroy && !isDestroyed) {
            world.destroyBody(body);
            isDestroyed = true;
        }
    }

    public void draw(Batch batch) {
        if(!isDestroyed) {
            super.draw(batch);
        }
    }

    public void reverseVelocity(boolean x, boolean y) {
        if(x) {
            velocity.x = -velocity.x;
        }
        if(y) {
            velocity.y = -velocity.y;
        }
    }

    public void destory() {
        isReadyToDestroy = true;
    }
}
