package de.tum.cit.ase.bomberquest.gameobjects.walls;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.gameobjects.walls.Wall;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Destructible Wall is a static object preventing dynamic objects to pass.
 * This wall can be destroyed by the blast radius of a bomb.
 * The blast radius of the bomb is blocked after this wall.
 */
public class DestructibleWall extends Wall {

    /** Indicator whether wall has been destroyed */
    private boolean destroyed;
    /** Time of the animation when the wall is destroyed */
    private float animationTime;
    /** Start the destroy animation */
    private boolean playAnimation;

    /**
     * Create a DestructibleWall at the given position.
     * This wall can be destroyed
     *
     * @param world The Box2D world to add the DestructibleWall's hitbox to.
     * @param x The X position.
     * @param y The Y position.
     */
    public DestructibleWall(World world, float x, float y) {
        super(world, x, y);
        this.destroyed = false;
        this.animationTime = 0;
        this.playAnimation = false;
    }

    //Methods

    /**
     * Return the correct texture for the DestructibleWall.
     * Default texture if not destroyed
     * Empty texture if destroyed
     * Animation if being destroyed
     */
    @Override
    public TextureRegion getCurrentAppearance() {

        if (destroyed && playAnimation) {
            TextureRegion currentFrame = Animations.DESTRUCTIBLE_WALL_DESTROY.getKeyFrame(animationTime, false);

            // check if the Animation has finished and stop it
            if (Animations.DESTRUCTIBLE_WALL_DESTROY.isAnimationFinished(animationTime)) {
                playAnimation = false;
            }
            return currentFrame;
        }

        if (!destroyed) {
            return Textures.DESTRUCTIBLE_WALL;
        } else return Textures.EMPTY;
    }

    /**
     * Destroy the DestructibleWall.
     * Sets destroyed to true.
     * Starts the animation.
     *
     * @param world The Box2D world to destroy the body.
     */
    public void destroy(World world) {
        if (!destroyed) {
            destroyed = true;
            playAnimation = true; //Start the animation of the destruction
            animationTime = 0;
            world.destroyBody(hitbox); //Destroy the body and therefore the hitbox of the wall
        }
    }

    /**
     * Update the state of the wall and the elapsed time for the animation
     * Gets called in the GameScreen class
     *
     * @param elapsedTime time that the animation has been going on.
     */
    public void update(float elapsedTime) {
        if (playAnimation) {
            animationTime += elapsedTime;
        }
    }

    //Getters and Setters
    public boolean isDestroyed() {
        return destroyed;
    }
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
