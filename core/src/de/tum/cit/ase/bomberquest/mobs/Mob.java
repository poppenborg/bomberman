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

/**
 * Represents the mob in the game.
 * The mob has a hitbox, so it can collide with other objects in the game.
 */
public class Mob implements Drawable {

    /** Total time elapsed since the game started. We use this for calculating the mob movement and animating it. */
    private float elapsedTime;

    /** The Box2D hitbox of the mob, used for position and collision detection. */
    private final Body hitbox;

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
    protected Body createHitbox(World world, float startX, float startY) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Dynamic bodies are affected by forces and collisions.
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set the initial position of the body.
        bodyDef.position.set(startX, startY);
        // Create the body in the world using the body definition.
        Body body = world.createBody(bodyDef);
        // Now we need to give the body a shape so the physics engine knows how to collide with it.
        // We'll use a circle shape for the mob.
        CircleShape circle = new CircleShape();
        // Give the circle a radius of 0.3 tiles (the mob is 0.6 tiles wide).
        circle.setRadius(0.3f);
        // Attach the shape to the body as a fixture.
        // Bodies can have multiple fixtures, but we only need one for the mob.
        body.createFixture(circle, 1.0f);
        // We're done with the shape, so we should dispose of it to free up memory.
        circle.dispose();
        // Set the mob as the user data of the body so we can look up the mob from the body later.
        body.setUserData(this);
        return body;
    }


    /**
     * Method for the animation of the mob
     * Detects the current velocity of the mob and returns the animation based on the movement direction
     */

    @Override
    public TextureRegion getCurrentAppearance() {
        return Animations.CHARACTER_WALK_DOWN.getKeyFrame(getElapsedTime(), true);
    }

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
}
