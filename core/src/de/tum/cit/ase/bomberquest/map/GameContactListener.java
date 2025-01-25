package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.gameobjects.exits.Exit;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.PowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.Coordinates;
import de.tum.cit.ase.bomberquest.gameobjects.mobs.Enemy;
import de.tum.cit.ase.bomberquest.gameobjects.mobs.Player;

/**
 * This is a modified contact listener defining to the game logic specific reactions if 2 bodies collide.
 */
public class GameContactListener implements ContactListener {

    // Collision state of the Player with any Enemy
    private boolean playerEnemyCollision = false;
    // Coordinates of the power-up;
    private Coordinates powerUpCoordinates;
    // Indicator for whether the player reached the exit
    private boolean exitReached = false;

    /**
     * Define the consequences of 2 specific bodies colliding
     * Specifically if Player and an Enemy collide the Player dies (via playerEnemyCollision = true),
     * if Player and a PowerUp collide the PowerUp gets picked up (vie .setMarkedForRemoval(true)),
     * if Player and Exit collide exitReached is set to true.
     *
     * @param contact Collision between 2 bodies
     */
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
//            powerUpCoordinates = new Coordinates(((PowerUp) bodyA.getUserData()).getX(), ((PowerUp) bodyA.getUserData()).getY());
        }
        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof PowerUp) {
            ((PowerUp) bodyB.getUserData()).setMarkedForRemoval(true);
//            powerUpCoordinates = new Coordinates(((PowerUp) bodyB.getUserData()).getX(), ((PowerUp) bodyB.getUserData()).getY());
        }
        // check if the 2 colliding bodies are Player and Exit
        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof Exit || bodyA.getUserData() instanceof Exit && bodyB.getUserData() instanceof Player) {
            this.exitReached = true;
        }
    }

    /**
     * Handles the behavior when two bodies stop colliding.
     *
     * @param contact The contact object representing the collision between two bodies.
     */
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

    // Getters and Setters
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
