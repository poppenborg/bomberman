package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.gameobjects.Exits.Exit;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.PowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.Coordinates;
import de.tum.cit.ase.bomberquest.gameobjects.mobs.Enemy;
import de.tum.cit.ase.bomberquest.gameobjects.mobs.Player;

/**
 * This is a modified contact listener defining to the game logic specific reactions if 2 bodies collide
 */
public class GameContactListener implements ContactListener {

    // attribute to represent the collision state of the Player with any Enemy
    private boolean playerEnemyCollision = false;
    // Coordinates of the power-up;
    private Coordinates powerUpCoordinates;
    // indicator for whether the player reached the exit
    private boolean exitReached = false;

    // defines the behaviour if 2 objects collide
    @Override
    public void beginContact(Contact contact) {
        // get the 2 colliding bodies
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        // check if the 2 colliding bodies are Player and Enemy
        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof Enemy || bodyA.getUserData() instanceof Enemy && bodyB.getUserData() instanceof Player) {
            this.playerEnemyCollision = true;
        }
        // check if the 2 colliding bodies are Player and PowerUp
        if (bodyA.getUserData() instanceof PowerUp && bodyB.getUserData() instanceof Player) {
            ((PowerUp) bodyA.getUserData()).setMarkedForRemoval(true);
            powerUpCoordinates = new Coordinates(((PowerUp) bodyA.getUserData()).getX(), ((PowerUp) bodyA.getUserData()).getY());
        }
        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof PowerUp) {
            ((PowerUp) bodyB.getUserData()).setMarkedForRemoval(true);
            powerUpCoordinates = new Coordinates(((PowerUp) bodyB.getUserData()).getX(), ((PowerUp) bodyB.getUserData()).getY());
        }
        // check if the 2 colliding bodies are Player and PowerUp
        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof Exit || bodyA.getUserData() instanceof Exit && bodyB.getUserData() instanceof Player) {
            this.exitReached = true;
        }
    }

    // defines the behaviour if the collision of the 2 objects ends
    @Override
    public void endContact(Contact contact) {
        // get the 2 colliding bodies
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        // check if the 2 colliding bodies are Player and Enemy
        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof Enemy || bodyA.getUserData() instanceof Enemy && bodyB.getUserData() instanceof Player) {
            this.playerEnemyCollision = false;
        }
        // check if the 2 colliding bodies are Player and PowerUp
        else if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof PowerUp || bodyA.getUserData() instanceof PowerUp && bodyB.getUserData() instanceof Player) {
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
    public Coordinates getPowerUpCoordinates() {
        return powerUpCoordinates;
    }
    public boolean isExitReached() {
        return exitReached;
    }
}
