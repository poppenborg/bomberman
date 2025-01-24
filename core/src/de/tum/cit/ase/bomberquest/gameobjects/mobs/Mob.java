package de.tum.cit.ase.bomberquest.gameobjects.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.gameobjects.GameObject;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

import java.awt.*;

/**
 * Represents the mob in the game.
 * The mob has a hitbox, so it can collide with other objects in the game.
 *  This is an abstract class providing structure for Player and Enemy.
 */
public abstract class Mob extends GameObject {

    /** Total time elapsed since the game started */
    private float elapsedTime;
    /** A threshold to avoid unintended animations because of collisions */
    private static final float VELOCITYTHRESHOLD = 0.1f;

    /**
     * Constructs a Mob with a given starting position.
     * This constructor initializes the mob´s hitbox
     *
     * @param world The Box2D world to which the mob's physics body will be added.
     * @param x The initial X position.
     * @param y The initial Y position.
     */
    public Mob(World world, float x, float y) {
        super(x, y);
        this.hitbox = createHitbox(world, x, y);
    }

    /**
     * Create a Box2D body for the mob.
     *
     * @param world The Box2D world to add the body to.
     * @param startX The initial X position.
     * @param startY The initial Y position.
     * @return The created body.
     */
    protected abstract Body createHitbox(World world, float startX, float startY);

    /**
     * Method to let the mob move
     * Updates the mob´s state mob based on the elapsed time.
     *
     * @param frameTime the time since the last frame.
     */
    abstract public void tick(float frameTime);

    /**
     * Method for the animation of the mob
     * Detects the current velocity of the mob and returns the animation based on the movement direction
     */
    @Override
    abstract public TextureRegion getCurrentAppearance();

    // Getters and Setters
    @Override
    public float getX() {
        // The x-coordinate of the mob is the x-coordinate of the hitbox (this can change every frame).
        return hitbox.getPosition().x;
    }
    @Override
    public float getY() {
        // The y-coordinate of the mob is the y-coordinate of the hitbox (this can change every frame).
        return hitbox.getPosition().y;
    }
    public float getElapsedTime() {
        return elapsedTime;
    }
    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    public float getVELOCITYTHRESHOLD() {
        return VELOCITYTHRESHOLD;
    }

}
