package name.qd.game.mario.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.awt.geom.RectangularShape;
import java.util.Stack;

import name.qd.game.mario.MarioDemo;

public class Mario extends Sprite {
    public Body body;

    private enum State {FALLING, JUMPING, STANDING, RUNNING};
    private State currentState;
    private State previousState;

    private World world;
    private TextureRegion marioStand;

    private Animation jumpAnima;
    private Animation runAnima;
    private float stateTimer;
    private boolean isRunningRight;

    public Mario(World world) {
        super(new TextureRegion(new Texture("MarioBros.png")));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        isRunningRight = true;

        Array<TextureRegion> frames = new Array<>();
        for(int i = 1 ; i < 4 ; i++) {
            frames.add(new TextureRegion(getTexture(), getTextureX(i), getTextureY(i), 16, 16));
        }
        runAnima = new Animation(0.1f, frames);
        frames.clear();
        for(int i = 5 ; i < 6 ;  i++) {
            frames.add(new TextureRegion(getTexture(), getTextureX(i), getTextureY(i), 16, 16));
        }
        jumpAnima = new Animation(0.1f, frames);

        defineMario();

        marioStand = new TextureRegion(getTexture(), getTextureX(0), getTextureY(0), 16, 16);
        setBounds(0, 0, 16 / MarioDemo.PIXEL_PER_METER, 16 / MarioDemo.PIXEL_PER_METER);
        setRegion(marioStand);
    }

    private int getTextureX(int i) {
        return 80 + (i * 17);
    }

    private int getTextureY(int i) {
        return 34;
    }

    public void update(float deltaTime) {
        setPosition(body.getPosition().x - (getWidth() / 2), body.getPosition().y - (getHeight() / 2));
        setRegion(getFrame(deltaTime));
    }

    private TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        TextureRegion region;
        switch(currentState) {
            case JUMPING:
                region = (TextureRegion)jumpAnima.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion)runAnima.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
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
        if(body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if(body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if(body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    private void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(40 / MarioDemo.PIXEL_PER_METER, 32 / MarioDemo.PIXEL_PER_METER);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / MarioDemo.PIXEL_PER_METER);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(new Vector2(-2 / MarioDemo.PIXEL_PER_METER, 6 / MarioDemo.PIXEL_PER_METER), new Vector2(2 / MarioDemo.PIXEL_PER_METER, 6 / MarioDemo.PIXEL_PER_METER));
        fixtureDef.shape = edgeShape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("head");
    }
}
