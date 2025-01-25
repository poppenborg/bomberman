package de.tum.cit.ase.bomberquest.gameobjects.powerups;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.audio.Soundeffect;
import de.tum.cit.ase.bomberquest.gameobjects.GameObject;

/**
 * Abstract class for power-ups in the game.
 * Here common behaviour for all kinds of power-ups is defined
 * The power-up has a hitbox for collision detection, but since it´s a sensor Enemies can pass thought it.
 */
public abstract class PowerUp extends GameObject {

    /** Indicate whether the power-up should be removed */
    protected boolean markedForRemoval;
    /** Indicate whether the power-up has been collected by the player. */
    protected boolean taken;

    /**
     * Constructor for the Power-up
     * Object is initialized at the specified position and a hitbox is created in the game world.
     *
     * @param world The Box2D world.
     * @param x x-coordinate in the GameMap
     * @param y y-coordinate in the GameMap
     */
    public PowerUp(World world, float x, float y) {
        super(x, y);
        this.markedForRemoval = false;
        this.taken = false;
        this.hitbox = createHitbox(world);
    }

    /**
     * Create a Box2D rectangular body for the PowerUp at the current player position.
     * To prevent the player from collecting the power-up before the destructible wall above was destroyed the shape is a bit smaller than the wall
     *
     * @param world The Box2D world to add the body to.
     * @return Created Box2D body representing the power-up.
     */
    private Body createHitbox(World world) {
        Body body = createEmptyStaticBody(world, getX(), getY());
        PolygonShape box = new PolygonShape();
        box.setAsBox(0.48f, 0.48f);
        body.createFixture(box, 1f).setSensor(true);
        box.dispose();
        body.setUserData(this);
        return body;
    }

    /**
     * Destroy the hitbox in the game and marks the power-up as taken.
     *
     * @param world The Box2D world.
     */
    public void destroy(World world) {
        if (markedForRemoval) {
            this.taken = true;
            world.destroyBody(hitbox);
        }
    }

    // Getters and Setters
    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }
    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
        // Play sound when power-up is collected
        Soundeffect.POWERUP_SOUND.play(Soundeffect.getVOLUME());
    }
    public boolean isTaken() {
        return taken;
    }
}
