package de.tum.cit.ase.bomberquest.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

import java.awt.*;

/**
 * Represents the mob in the game.
 * The mob has a hitbox, so it can collide with other objects in the game.
 */
public abstract class Mob implements Drawable {

    /** Total time elapsed since the game started. We use this for calculating the mob movement and animating it. */
    private float elapsedTime;

    /** The Box2D hitbox of the mob, used for position and collision detection. */
    private final Body hitbox;

    /** A threshold to avoid unintended animations because of colosions */
    private final float VELOCITYTHRESHOLD = 0.1f;

    public Mob(World world, float x, float y) {
        this.hitbox = createHitbox(world, x, y);
    }

    /**
     * Creates a Box2D body for the mob.
     * This is what the physics engine uses to move the mob around and detect collisions with other bodies.
     * @param world The Box2D world to add the body to.
     * @param startX The initial X position.
     * @param startY The initial Y position.
     * @return The created body.
     */
    protected abstract Body createHitbox(World world, float startX, float startY);

    /**
     * Method to let the mob move
     * @param frameTime the time since the last frame.
     */
    abstract public void tick(float frameTime);

    /**
     * Method for the animation of the mob
     * Detects the current velocity of the mob and returns the animation based on the movement direction
     */
    @Override
    abstract public TextureRegion getCurrentAppearance();

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

    public Body getHitbox() {
        return hitbox;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public float getVELOCITYTHRESHOLD() {
        return VELOCITYTHRESHOLD;
    }

}
