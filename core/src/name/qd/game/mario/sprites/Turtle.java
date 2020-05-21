package name.qd.game.mario.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import name.qd.game.mario.MarioDemo;

public class Turtle extends Enemy {
    private enum State {WALKING, SHELL}
    private State currentState;
    private State previousState;
    private float stateTime;
    private Animation walking;
    private TextureRegion shell;
    private AssetManager assetManager;

    private boolean isReadyToDestroy;
    private boolean isDestroyed;

    public Turtle(World world, float x, float y, AssetManager assetManager) {
        super(new TextureRegion(assetManager.get("MarioEnemies.png", Texture.class)), world, x, y);
        this.assetManager = assetManager;
        Array<TextureRegion> frames = new Array<>();
        frames.add(new TextureRegion(getTexture(), getTextureX(6), 8, 16, 24));
        frames.add(new TextureRegion(getTexture(), getTextureX(7), 8, 16, 24));
        walking = new Animation(0.2f, frames);
        shell = new TextureRegion(getTexture(), getTextureX(10), 8, 16, 24);

        setBounds(getX(), getY(), 16 / MarioDemo.PIXEL_PER_METER, 24 / MarioDemo.PIXEL_PER_METER);
        currentState = State.WALKING;
        previousState = State.WALKING;
    }

    @Override
    public void update(float deltaTime) {
        setRegion(getFrame(deltaTime));

        if(currentState == State.SHELL && stateTime > 5) {
            currentState = State.WALKING;
            velocity.x = 1;
        }

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 8 / MarioDemo.PIXEL_PER_METER);
        body.setLinearVelocity(velocity);
    }

    private TextureRegion getFrame(float deltaTime) {
        TextureRegion region;

        switch(currentState) {
            case SHELL:
                region = shell;
                break;
            case WALKING:
            default:
                region = (TextureRegion) walking.getKeyFrame(stateTime, true);
                break;
        }

        if(velocity.x > 0 && !region.isFlipX()) {
            region.flip(true, false);
        }
        if(velocity.x < 0 && region.isFlipX()) {
            region.flip(true, false);
        }

        stateTime = currentState == previousState ? stateTime + deltaTime : 0;
        previousState = currentState;

        return region;
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
        if(currentState == State.WALKING) {
            currentState = State.SHELL;
            velocity.x = 0;
        }
    }
}
