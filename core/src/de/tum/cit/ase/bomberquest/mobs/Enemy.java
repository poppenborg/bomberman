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

    /**
     * Method for the animation of the player
     * Detects the current velocity of the player and returns the animation based on the movement direction
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        // Get the current velocity of the player
        float xVelocity = getHitbox().getLinearVelocity().x;
        float yVelocity = getHitbox().getLinearVelocity().y;

        // Return animation based on the movement direction
        if (xVelocity > 0) {
            return Animations.ENEMY_WALK_RIGHT.getKeyFrame(getElapsedTime(), true);
        } else if (xVelocity < 0) {
            return Animations.ENEMY_WALK_LEFT.getKeyFrame(getElapsedTime(), true);
        } else if (yVelocity > 0) {
            return Animations.ENEMY_WALK_UP.getKeyFrame(getElapsedTime(), true);
        } else if (yVelocity < 0) {
            return Animations.ENEMY_WALK_DOWN.getKeyFrame(getElapsedTime(), true);
        } else {
            return Animations.ENEMY_WALK_DOWN.getKeyFrame( 0, false);
        }
    }

}
