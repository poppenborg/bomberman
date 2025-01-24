package de.tum.cit.ase.bomberquest.gameobjects.mobs;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

/**
 * Represents the enemy character in the game.
 * The enemy has a circular hitbox, so it can collide with other objects in the game.
 * It moves around independently in a circular pattern.
 */
public class Enemy extends Mob implements Drawable {

    /** Radius of the circular body */
    private float radius;
    private float rectangleWidth;
    private float rectangleHeight;
    /** Indicates whether the enemy has been killed. */
    private boolean killed = false;

    /**
     * Construct an Enemy at the specified coordinates in the game.
     *
     * @param world The Box2D world to which the enemy belongs.
     * @param x The x-coordinate of the enemy's starting position.
     * @param y The y-coordinate of the enemy's starting position.
     */
    public Enemy(World world, float x, float y) {
        super(world, x, y);
    }

    /**
     * Create a circular hitbox to detect collision.
     *
     * @param world The Box2D world to add the hitbox to.
     * @param startX The x-coordinate of the hitbox's starting position.
     * @param startY The y-coordinate of the hitbox's starting position.
     * @return The Box2D body for the hitbox.
     */
    @Override
    protected Body createHitbox(World world, float startX, float startY) {
        Body body = createEmptyDynamicBody(world, startX, startY);
        CircleShape circle = new CircleShape();
        circle.setRadius(0.3f);
        radius = circle.getRadius();
        body.createFixture(circle, 1.0f);
        circle.dispose();
        body.setUserData(this);
        return body;
    }

    /**
     * Update the enemy's movement behavior.
     * Let the Enemy move in a circle
     *
     * @param frameTime The time since the last frame.
     */
    @Override
    public void tick(float frameTime) {
        setElapsedTime(getElapsedTime() + frameTime);
        if (killed) { //if the enemy  killed the movement is stopped.
            getHitbox().setLinearVelocity(0,0);
        } else {
            float xVelocity = (float) Math.sin(getElapsedTime()) * 2;
            float yVelocity = (float) Math.cos(getElapsedTime()) * 2;
            getHitbox().setLinearVelocity(xVelocity, yVelocity);
        }
    }

    /**
     * Return current visual representation based on movement and state.
     *
     * @return A TextureRegion for the enemy's current appearance.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        if (killed) {
            return Animations.ENEMY_DYING.getKeyFrame(getElapsedTime(), false);
        }
        // Get the current velocity of the player
        float xVelocity = getHitbox().getLinearVelocity().x;
        float yVelocity = getHitbox().getLinearVelocity().y;
        // Return animation based on the movement direction
        if (xVelocity > 0 && xVelocity > getVELOCITYTHRESHOLD()) {
            return Animations.ENEMY_WALK_RIGHT.getKeyFrame(getElapsedTime(), true);
        } else if (xVelocity < 0 && Math.abs(xVelocity) > getVELOCITYTHRESHOLD()) {
            return Animations.ENEMY_WALK_LEFT.getKeyFrame(getElapsedTime(), true);
        } else if (yVelocity > 0 && yVelocity > getVELOCITYTHRESHOLD()) {
            return Animations.ENEMY_WALK_UP.getKeyFrame(getElapsedTime(), true);
        } else if (yVelocity < 0 && Math.abs(yVelocity) > getVELOCITYTHRESHOLD()) {
            return Animations.ENEMY_WALK_DOWN.getKeyFrame(getElapsedTime(), true);
        } else {
            return Animations.ENEMY_WALK_DOWN.getKeyFrame( 0, false);
        }
    }

    // Getters and Setters
    public float getRadius() {
        return radius;
    }
    public float getRectangleWidth() {
        return rectangleWidth;
    }
    public float getRectangleHeight() {
        return rectangleHeight;
    }
    public boolean isKilled() {
        return killed;
    }
    public void setKilled(boolean killed) {
        this.killed = killed;
    }
}
