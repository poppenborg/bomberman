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
 * Represents the enemy character in the game.
 * The enemy has a hitbox, so it can collide with other objects in the game.
 * It moves around independendly
 */
public class Enemy extends Mob implements Drawable {

    public Enemy(World world, float x, float y) {
        super(world, x, y);
    }

    @Override
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
     * Let the enemis move in a circle
     * @param frameTime the time since the last frame.
     */
    @Override
    public void tick(float frameTime) {
        setElapsedTime(getElapsedTime() + frameTime);
        // TODO: implement inteligent movement
        float xVelocity = (float) Math.sin(getElapsedTime()) * 2;
        float yVelocity = (float) Math.cos(getElapsedTime()) * 2;
        getHitbox().setLinearVelocity(xVelocity, yVelocity);
    }

    @Override
    public TextureRegion getCurrentAppearance() {
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

}
