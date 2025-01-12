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
 * Represents the player character in the game.
 * The player has a hitbox, so it can collide with other objects in the game.
 */
public class Player extends Mob implements Drawable {

    public Player(World world, float x, float y) {
        super(world, x, y);
    }

    /**
     * Move the player via the keyboard using the arrow keys OR the WASD as an alternative
     * @param frameTime the time since the last frame.
     */
    public void tick(float frameTime) {
        setElapsedTime(getElapsedTime() + frameTime);

        float xVelocity = 0;
        float yVelocity = 0;

        //Player can be controlled via the keyboard using the arrow keys OR the WASD as an alternative

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            xVelocity -= 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            xVelocity += 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            yVelocity += 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            yVelocity -= 2;
        }

        getHitbox().setLinearVelocity(xVelocity, yVelocity);
    }

    /**
     * Method for the animation of the player
     * Detects the current velocity of the mob and returns the animation based on the movement direction
     */

    @Override
    public TextureRegion getCurrentAppearance() {
        // Get the current velocity of the mob

        float xVelocity = getHitbox().getLinearVelocity().x;
        float yVelocity = getHitbox().getLinearVelocity().y;

        // Return animation based on the movement direction

        if (xVelocity > 0) {

            return Animations.CHARACTER_WALK_RIGHT.getKeyFrame(getElapsedTime(), true);
        } else if (xVelocity < 0) {

            return Animations.CHARACTER_WALK_LEFT.getKeyFrame(getElapsedTime(), true);
        } else if (yVelocity > 0) {

            return Animations.CHARACTER_WALK_UP.getKeyFrame(getElapsedTime(), true);
        } else if (yVelocity < 0) {

            return Animations.CHARACTER_WALK_DOWN.getKeyFrame(getElapsedTime(), true);
        } else {
            return Animations.CHARACTER_WALK_DOWN.getKeyFrame( 0, false);
        }
    }
}
