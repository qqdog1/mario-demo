package name.qd.game.mario.listener;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import name.qd.game.mario.MarioDemo;
import name.qd.game.mario.items.Item;
import name.qd.game.mario.sprites.Enemy;
import name.qd.game.mario.sprites.InteractiveTileObject;
import name.qd.game.mario.sprites.Mario;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int collisionDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        if(fixtureA.getUserData() == "head" || fixtureA.getUserData() == "head") {
            if(fixtureA.getUserData() instanceof InteractiveTileObject) {
                ((InteractiveTileObject) fixtureA.getUserData()).onHeadHit();
            } else if(fixtureB.getUserData() instanceof InteractiveTileObject) {
                ((InteractiveTileObject) fixtureB.getUserData()).onHeadHit();
            }
        }

        switch(collisionDef) {
            case MarioDemo.MARIO_BIT | MarioDemo.ENEMY_HEAD_BIT:
                if(fixtureA.getFilterData().categoryBits == MarioDemo.ENEMY_HEAD_BIT) {
                    ((Enemy)fixtureA.getUserData()).hitOnHead();
                } else {
                    ((Enemy)fixtureB.getUserData()).hitOnHead();
                }
                break;
            case MarioDemo.ENEMY_BIT | MarioDemo.OBJECT_BIT:
                if(fixtureA.getFilterData().categoryBits == MarioDemo.ENEMY_BIT) {
                    ((Enemy)fixtureA.getUserData()).reverseVelocity(true, false);
                } else {
                    ((Enemy)fixtureB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case MarioDemo.MARIO_BIT | MarioDemo.ENEMY_BIT:

                break;
            case MarioDemo.ENEMY_BIT:
                ((Enemy)fixtureA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixtureB.getUserData()).reverseVelocity(true, false);
                break;
            case MarioDemo.ITEM_BIT | MarioDemo.OBJECT_BIT:
                if(fixtureA.getFilterData().categoryBits == MarioDemo.ITEM_BIT) {
                    ((Item)fixtureA.getUserData()).reverseVelocity(true, false);
                } else {
                    ((Item)fixtureB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case MarioDemo.ITEM_BIT | MarioDemo.MARIO_BIT:
                if(fixtureA.getFilterData().categoryBits == MarioDemo.ITEM_BIT) {
                    ((Item)fixtureA.getUserData()).use((Mario)fixtureB.getUserData());
                } else {
                    ((Item)fixtureB.getUserData()).use((Mario)fixtureA.getUserData());
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
