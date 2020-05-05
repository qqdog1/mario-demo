package name.qd.game.mario.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import name.qd.game.mario.MarioDemo;

public class Mario extends Sprite {
    private World world;
    public Body body;
    private TextureRegion marioStand;

    public Mario(World world) {
        super(new TextureRegion(new Texture("MarioBros.png")));
        this.world = world;
        defineMario();

        marioStand = new TextureRegion(getTexture(), 80, 34, 16, 16);
        setBounds(0, 0, 16 / MarioDemo.PIXEL_PER_METER, 16 / MarioDemo.PIXEL_PER_METER);
        setRegion(marioStand);
    }

    public void update(float deltaTime) {
        setPosition(body.getPosition().x - (getWidth() / 2), body.getPosition().y - (getHeight() / 2));
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
    }
}
