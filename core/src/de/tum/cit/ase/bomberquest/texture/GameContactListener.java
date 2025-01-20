package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.mobs.Enemy;
import de.tum.cit.ase.bomberquest.mobs.Player;

/**
 * This is a modified contact listener defining to the game logic specific reactions if 2 bodies collide
 */
public class GameContactListener implements ContactListener {

    // attribute to represent the collision state of the Player with any Enemy
    private boolean playerEnemyCollision;

    // defines the behaviour if 2 objects collide
    @Override
    public void beginContact(Contact contact) {
        // get the 2 colliding bodies
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        // check if the 2 colliding bodies are Player and Enemy
        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof Enemy || bodyB.getUserData() instanceof Player && bodyA.getUserData() instanceof Enemy) {
            this.playerEnemyCollision = true;
        }
    }

    // defines the behaviour if the collision of the 2 objects ends
    @Override
    public void endContact(Contact contact) {
        // get the 2 colliding bodies
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        // check if the 2 colliding bodies are Player and Enemy
        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof Enemy || bodyB.getUserData() instanceof Player && bodyA.getUserData() instanceof Enemy) {
            this.playerEnemyCollision = false;
        }
    }

    // unused methods form the interface
    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }

    // getters and setters
    public boolean isPlayerEnemyCollision() {
        return playerEnemyCollision;
    }
}
