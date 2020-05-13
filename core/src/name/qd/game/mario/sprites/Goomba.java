package name.qd.game.mario.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import name.qd.game.mario.MarioDemo;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation animation;
    private Array<TextureRegion> frames;

    private boolean isReadyToDestroy;
    private boolean isDestroyed;

    public Goomba(World world, float x, float y) {
        super(new TextureRegion(new Texture("MarioEnemies.png")), world, x, y);
        frames = new Array<>();
        for(int i = 0; i < 2 ; i++) {
            frames.add(new TextureRegion(getTexture(), getTextureX(i), getTextureY(i), 16, 16));
        }
        animation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / MarioDemo.PIXEL_PER_METER, 16 / MarioDemo.PIXEL_PER_METER);
        isReadyToDestroy = false;
        isDestroyed = false;
    }

    @Override
    public void update(float deltaTime) {
        stateTime += deltaTime;

        if(isReadyToDestroy && !isDestroyed) {
            world.destroyBody(body);
            isDestroyed = true;
            setRegion(new TextureRegion(getTexture(), getTextureX(2), getTextureY(2), 16, 16));
            stateTime = 0;
        } else if(!isDestroyed) {
            body.setLinearVelocity(velocity);
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) animation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    public void draw(Batch batch) {
        if(!isDestroyed || stateTime < 1) {
            super.draw(batch);
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / MarioDemo.PIXEL_PER_METER);
        fixtureDef.filter.categoryBits = MarioDemo.ENEMY_BIT;
        fixtureDef.filter.maskBits = MarioDemo.GROUND_BIT | MarioDemo.COIN_BIT | MarioDemo.BRICK_BIT | MarioDemo.COINBRICK_BIT
        | MarioDemo.OBJECT_BIT | MarioDemo.ENEMY_BIT | MarioDemo.MARIO_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vectors = new Vector2[4];
        vectors[0] = new Vector2(-5, 8).scl(1 / MarioDemo.PIXEL_PER_METER);
        vectors[1] = new Vector2(5, 8).scl(1 / MarioDemo.PIXEL_PER_METER);
        vectors[2] = new Vector2(-3, 3).scl(1 / MarioDemo.PIXEL_PER_METER);
        vectors[3] = new Vector2(3, 3).scl(1 / MarioDemo.PIXEL_PER_METER);
        head.set(vectors);

        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = MarioDemo.ENEMY_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void hitOnHead() {
        isReadyToDestroy = true;
    }

    private int getTextureX(int i) {
        return 0 + (i * 16);
    }

    private int getTextureY(int i) {
        return 16;
    }
}
