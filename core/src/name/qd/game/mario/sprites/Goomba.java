package name.qd.game.mario.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import name.qd.game.mario.MarioDemo;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation animation;
    private Array<TextureRegion> frames;

    public Goomba(World world, float x, float y) {
        super(new TextureRegion(new Texture("MarioEnemies.png")), world, x, y);
        frames = new Array<>();
        for(int i = 0; i < 2 ; i++) {
            frames.add(new TextureRegion(getTexture(), getTextureX(i), getTextureY(i), 16, 16));
        }
        animation = new com.badlogic.gdx.graphics.g2d.Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / MarioDemo.PIXEL_PER_METER, 16 / MarioDemo.PIXEL_PER_METER);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) animation.getKeyFrame(stateTime, true));
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(160 / MarioDemo.PIXEL_PER_METER, 32 / MarioDemo.PIXEL_PER_METER);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / MarioDemo.PIXEL_PER_METER);
        fixtureDef.filter.categoryBits = MarioDemo.ENEMY_BIT;
        fixtureDef.filter.maskBits = MarioDemo.GROUND_BIT | MarioDemo.COIN_BIT | MarioDemo.BRICK_BIT | MarioDemo.COINBRICK_BIT
        | MarioDemo.OBJECT_BIT | MarioDemo.ENEMY_BIT | MarioDemo.MARIO_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }

    private int getTextureX(int i) {
        return 0 + (i * 16);
    }

    private int getTextureY(int i) {
        return 16;
    }
}
