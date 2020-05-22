package name.qd.game.mario.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import name.qd.game.mario.MarioDemo;

public class Turtle extends Enemy {
    public static final int KICK_LEFT_SPEED = -2;
    public static final int KICK_RIGHT_SPEED = 2;

    public enum State {WALKING, STANDING_SHELL, MOVING_SHELL, DEAD}
    private State currentState;
    private State previousState;
    private float stateTime;
    private Animation walking;
    private TextureRegion shell;
    private AssetManager assetManager;
    private float rotationDegrees;

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
        rotationDegrees = 0;

        setBounds(getX(), getY(), 16 / MarioDemo.PIXEL_PER_METER, 24 / MarioDemo.PIXEL_PER_METER);
        currentState = State.WALKING;
        previousState = State.WALKING;
    }

    @Override
    public void update(float deltaTime) {
        setRegion(getFrame(deltaTime));

        if(currentState == State.STANDING_SHELL && stateTime > 5) {
            currentState = State.WALKING;
            velocity.x = 1;
        }

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 8 / MarioDemo.PIXEL_PER_METER);

        if(currentState == State.DEAD) {
            rotationDegrees += 3;
            rotate(rotationDegrees);
            if(stateTime > 5 && !isDestroyed) {
                world.destroyBody(body);
                isDestroyed = true;
            }
        } else {
            body.setLinearVelocity(velocity);
        }
    }

    private TextureRegion getFrame(float deltaTime) {
        TextureRegion region;

        switch(currentState) {
            case MOVING_SHELL:
            case STANDING_SHELL:
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
        fixtureDef.restitution = 1.5f;
        fixtureDef.filter.categoryBits = MarioDemo.ENEMY_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void hitOnHead(Mario mario) {
        if(currentState != State.STANDING_SHELL) {
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        } else {
            int speed = mario.getX() <= this.getX() ? KICK_RIGHT_SPEED : KICK_LEFT_SPEED;
            kick(speed);
        }
    }

    public void kick(int speed) {
        velocity.x = speed;
        currentState = State.MOVING_SHELL;
    }

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void onEnemyHit(Enemy enemy) {
        if(enemy instanceof Turtle) {
            if(((Turtle) enemy).getCurrentState() == State.MOVING_SHELL) {
                killed();
            } else if(currentState == State.MOVING_SHELL) {
                return;
            } else {
                reverseVelocity(true, false);
            }
        } else if(currentState == State.WALKING) {
            reverseVelocity(true, false);
        }
    }

    @Override
    public void killed() {
        currentState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = MarioDemo.NOTHING_BIT;
        for(Fixture fixture : body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
        body.applyLinearImpulse(new Vector2(0, 8f), body.getWorldCenter(), true);
    }
}
