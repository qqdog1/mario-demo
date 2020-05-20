package name.qd.game.mario.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import name.qd.game.mario.MarioDemo;

public class Mario extends Sprite {
    public Body body;

    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD}
    public State currentState;
    private State previousState;

    private World world;

    private TextureRegion marioStand;
    private TextureRegion bigMarioStand;
    private TextureRegion marioJump;
    private TextureRegion bigMarioJump;
    private TextureRegion marioDead;

    private Animation runAnima;
    private Animation bigRunAnima;
    private Animation growMario;

    private float stateTimer;
    private boolean isRunningRight;
    private boolean isMarioBig;
    private boolean isRunGrowAnimation;
    private boolean isTimeToDefineBigMario;
    private boolean isTimeToRedefineMario;
    private boolean isMarioDead;

    private AssetManager assetManager;

    public Mario(World world, AssetManager assetManager) {
        super(new TextureRegion(new Texture("MarioBros.png")));
        this.world = world;
        this.assetManager = assetManager;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        isRunningRight = true;

        Array<TextureRegion> frames = new Array<>();
        for(int i = 1 ; i < 4 ; i++) {
            frames.add(new TextureRegion(getTexture(), getTextureX(i), 34, 16, 16));
        }
        runAnima = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 1 ; i < 4 ; i++) {
            frames.add(new TextureRegion(getTexture(), getTextureX(i), 1, 16, 32));
        }
        bigRunAnima = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(getTexture(), getTextureX(15), 1, 16, 32));
        frames.add(new TextureRegion(getTexture(), getTextureX(0), 1, 16, 32));
        frames.add(new TextureRegion(getTexture(), getTextureX(15), 1, 16, 32));
        frames.add(new TextureRegion(getTexture(), getTextureX(0), 1, 16, 32));
        growMario = new Animation(0.2f, frames);

        marioJump = new TextureRegion(getTexture(), getTextureX(5), 34, 16, 16);
        bigMarioJump = new TextureRegion(getTexture(), getTextureX(5), 1, 16, 32);

        marioStand = new TextureRegion(getTexture(), getTextureX(0), 34, 16, 16);
        bigMarioStand = new TextureRegion(getTexture(), getTextureX(0), 1, 16, 32);

        marioDead = new TextureRegion(getTexture(), getTextureX(6), 34, 16, 16);

        defineMario();

        setBounds(0, 0, 16 / MarioDemo.PIXEL_PER_METER, 16 / MarioDemo.PIXEL_PER_METER);
        setRegion(marioStand);
    }

    private int getTextureX(int i) {
        return 80 + (i * 17);
    }

    public void update(float deltaTime) {
        if(isMarioBig) {
            setPosition(body.getPosition().x - (getWidth() / 2), body.getPosition().y - (getHeight() / 2) - 8 / MarioDemo.PIXEL_PER_METER);
        } else {
            setPosition(body.getPosition().x - (getWidth() / 2), body.getPosition().y - (getHeight() / 2));
        }
        setRegion(getFrame(deltaTime));
        if(isTimeToDefineBigMario) {
            defineBigMario();
        }
        if(isTimeToRedefineMario) {
            redefineMario();
        }
    }

    public void hit() {
        if(isMarioBig) {
            isMarioBig = false;
            isTimeToRedefineMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() / 2);
            assetManager.get("audio/sound/smb_pipe.wav", Sound.class).play();
        } else {
            assetManager.get("audio/music/MarioBros.mp3", Music.class).stop();
            assetManager.get("audio/sound/smb_mariodie.wav", Sound.class).play();
            isMarioDead = true;
            Filter filter = new Filter();
            filter.maskBits = MarioDemo.NOTHING_BIT;
            for(Fixture fixture : body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
        }
    }

    public boolean isBig() {
        return isMarioBig;
    }

    public void grow() {
        isRunGrowAnimation = true;
        isMarioBig = true;
        isTimeToDefineBigMario = true;
        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        assetManager.get("audio/sound/smb_powerup.wav", Sound.class).play();
    }

    public boolean isDead() {
        return isMarioDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    private TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        TextureRegion region;
        switch(currentState) {
            case JUMPING:
                region = isMarioBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = isMarioBig ? (TextureRegion)bigRunAnima.getKeyFrame(stateTimer, true) : (TextureRegion)runAnima.getKeyFrame(stateTimer, true);
                break;
            case GROWING:
                region = (TextureRegion)growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer)) {
                    isRunGrowAnimation = false;
                }
                break;
            case DEAD:
                region = marioDead;
                break;
            case FALLING:
            case STANDING:
            default:
                region = isMarioBig ? bigMarioStand : marioStand;
                break;
        }

        if((body.getLinearVelocity().x < 0 || !isRunningRight) && !region.isFlipX()) {
            region.flip(true, false);
            isRunningRight = false;
        } else if((body.getLinearVelocity().x > 0 || isRunningRight) && region.isFlipX()) {
            region.flip(true, false);
            isRunningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        previousState = currentState;
        return region;
    }

    private State getState() {
        if(isMarioDead) {
            return State.DEAD;
        } else if(body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if(body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if(body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else if(isRunGrowAnimation) {
            return State.GROWING;
        } else {
            return State.STANDING;
        }
    }

    private void defineBigMario() {
        Vector2 currentPosition = body.getPosition();
        world.destroyBody(body);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currentPosition.add(0, 10 / MarioDemo.PIXEL_PER_METER));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / MarioDemo.PIXEL_PER_METER);
        fixtureDef.filter.categoryBits = MarioDemo.MARIO_BIT;
        fixtureDef.filter.maskBits = MarioDemo.GROUND_BIT | MarioDemo.COIN_BIT | MarioDemo.BRICK_BIT | MarioDemo.COINBRICK_BIT
                | MarioDemo.ENEMY_BIT | MarioDemo.OBJECT_BIT | MarioDemo.ENEMY_HEAD_BIT | MarioDemo.ITEM_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
        shape.setPosition(new Vector2(0, -16 / MarioDemo.PIXEL_PER_METER));
        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(new Vector2(-2 / MarioDemo.PIXEL_PER_METER, 6 / MarioDemo.PIXEL_PER_METER), new Vector2(2 / MarioDemo.PIXEL_PER_METER, 6 / MarioDemo.PIXEL_PER_METER));
        fixtureDef.shape = edgeShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = MarioDemo.MARIO_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        isTimeToDefineBigMario = false;
    }

    private void redefineMario() {
        Vector2 currentPosition = body.getPosition();
        world.destroyBody(body);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currentPosition.add(0, -10 / MarioDemo.PIXEL_PER_METER));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / MarioDemo.PIXEL_PER_METER);
        fixtureDef.filter.categoryBits = MarioDemo.MARIO_BIT;
        fixtureDef.filter.maskBits = MarioDemo.GROUND_BIT | MarioDemo.COIN_BIT | MarioDemo.BRICK_BIT | MarioDemo.COINBRICK_BIT
                | MarioDemo.ENEMY_BIT | MarioDemo.OBJECT_BIT | MarioDemo.ENEMY_HEAD_BIT | MarioDemo.ITEM_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(new Vector2(-2 / MarioDemo.PIXEL_PER_METER, 6 / MarioDemo.PIXEL_PER_METER), new Vector2(2 / MarioDemo.PIXEL_PER_METER, 6 / MarioDemo.PIXEL_PER_METER));
        fixtureDef.shape = edgeShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = MarioDemo.MARIO_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        isTimeToRedefineMario = false;
    }

    private void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(40 / MarioDemo.PIXEL_PER_METER, 32 / MarioDemo.PIXEL_PER_METER);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / MarioDemo.PIXEL_PER_METER);
        fixtureDef.filter.categoryBits = MarioDemo.MARIO_BIT;
        fixtureDef.filter.maskBits = MarioDemo.GROUND_BIT | MarioDemo.COIN_BIT | MarioDemo.BRICK_BIT | MarioDemo.COINBRICK_BIT
        | MarioDemo.ENEMY_BIT | MarioDemo.OBJECT_BIT | MarioDemo.ENEMY_HEAD_BIT | MarioDemo.ITEM_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(new Vector2(-2 / MarioDemo.PIXEL_PER_METER, 6 / MarioDemo.PIXEL_PER_METER), new Vector2(2 / MarioDemo.PIXEL_PER_METER, 6 / MarioDemo.PIXEL_PER_METER));
        fixtureDef.shape = edgeShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = MarioDemo.MARIO_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }
}
